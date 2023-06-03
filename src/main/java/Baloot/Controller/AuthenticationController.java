package Baloot.Controller;

import Baloot.Service.MarketService;
import Baloot.Util.GithubOauth;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


import Baloot.Util.JwtGenerator;

@RestController
public class AuthenticationController {
    @Value("${secretKey}")
    private String secretKey;
    @Value("${github.clientId}")
    private String clientId;

    @Value("${github.clientSecret}")
    private String clientSecret;

    private final MarketService marketService;
    private final GithubOauth githubUtil;

    public AuthenticationController(MarketService marketService, GithubOauth githubUtil) {
        this.marketService = marketService;
        this.githubUtil = githubUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        try {
            marketService.login(username, password);
            String jwt = JwtGenerator.generateJwt(username, secretKey);
            Map<String, Object> response = new HashMap<>();
            response.put("jwt", jwt);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String address = request.get("address");
        String birthdate = request.get("birthDate");
        try {
            marketService.signup(username, password, email, birthdate, address);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> checkLogin(HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        int cart = marketService.getBuyList(username).size();
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("cart", cart);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/github")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String accessTokenUrl = "https://github.com/login/oauth/access_token";
        String accessTokenParams = "?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code;
        String apiUrl = accessTokenUrl + accessTokenParams;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> apiResponse = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
        );
        String authResponse = apiResponse.getBody();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(authResponse);
            String accessToken = (String) jsonObject.get("access_token");
            String username = githubUtil.getUserDetails(accessToken);
            Map<String, Object> response = new HashMap<>();
            String jwt = JwtGenerator.generateJwt(username, secretKey);
            response.put("jwt", jwt);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> controllerResponse = new HashMap<>();
            controllerResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(controllerResponse);
        }
    }

}
