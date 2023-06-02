package Baloot.Controller;

import Baloot.Service.MarketService;
import Baloot.Util.GithubOauth;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


import Baloot.Util.JwtGenerator;
import Baloot.Util.JwtGenerator;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
    @Value("${github.clientId}")
    private String clientId;

    @Value("${github.clientSecret}")
    private String clientSecret;

    private final MarketService marketService;
    private GithubOauth githubUtil;

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
            int cart = marketService.getBuyList(username).size();
            String jwt = JwtGenerator.generateJwt(username);
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("cart", cart);
            response.put("jwt", jwt);
            return ResponseEntity.ok(response);
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
        if (marketService.getUserByEmail(email) != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "This email already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (marketService.getUserByUsername(username) != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "This username already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            try {
                marketService.signup(username, password, email, address, birthdate);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "User created successfully.");
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        if (!marketService.isUserLoggedIn()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User not logged in");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else {
            marketService.logout();
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Logout successful");
            return ResponseEntity.ok(responseBody);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> checkLogin() {
        if (marketService.isUserLoggedIn()) {
            String username = marketService.getLoggedInUser();
            int cart = marketService.getBuyList(username).size();
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestParam("code") String code) {

        String accessTokenUrl = "https://github.com/login/oauth/access_token";
        String accessTokenParams = "?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code;
        String apiUrl = accessTokenUrl + accessTokenParams;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        // Send the GET request
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
        );
        String authResponse = response.getBody();
        System.out.println(authResponse);
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(authResponse);
            String accessToken = (String) jsonObject.get("access_token");
            String username = githubUtil.getUserDetails(accessToken);
            String jwt = JwtGenerator.generateJwt(username);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            Map<String, Object> controllerResponse = new HashMap<>();
            controllerResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(controllerResponse);
        }
    }


}
