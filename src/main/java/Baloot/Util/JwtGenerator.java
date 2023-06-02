package Baloot.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtGenerator {
    @Value("${secretKey}")
    private static String secretKey;
    public static String generateJwt(String username) {

        // Set the expiration time
        Date expiration = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 1 day

        // Build the JWT
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("my_issuer")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return jwt;
    }

}

