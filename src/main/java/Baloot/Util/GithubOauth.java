package Baloot.Util;

import Baloot.Model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import Baloot.Service.MarketService;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

@Component
public class GithubOauth {
    private static final String GITHUB_USER_API_URL = "https://api.github.com/user";
    private final MarketService marketService;
    public GithubOauth(MarketService marketService){this.marketService=marketService;}
    public String getUserDetails(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(GITHUB_USER_API_URL));
        ResponseEntity<GithubUser> response = restTemplate.exchange(request, GithubUser.class);
        GithubUser user = response.getBody();
        if (response.getStatusCode().is2xxSuccessful()) {
            User balootUser =marketService.getUserByUsername(user.username);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");;
            LocalDateTime dateTime = LocalDateTime.parse(user.createdAt,formatter);
            LocalDateTime newDateTime = dateTime.minusYears(18);
            Date birthDate = Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
            if(balootUser!=null) {
                int credit = balootUser.getCredit();
                balootUser.updateUser(null, user.email,birthDate, user.address, credit);
                return balootUser.getUsername();
            }
            else {
                marketService.addUser(user.username,null,user.email,birthDate, user.address,0);
                return user.username;
            }
        } else {
            throw new RuntimeException("Failed to fetch user details from GitHub API");
        }
    }

    public static class GithubUser {
        @JsonProperty("login")
        public String username;

        @JsonProperty("email")
        public String email;

        @JsonProperty("created_at")
        public String createdAt;

        @JsonProperty("location")
        public String address;

        // Getters and setters (or lombok annotations) omitted for brevity
    }
}
