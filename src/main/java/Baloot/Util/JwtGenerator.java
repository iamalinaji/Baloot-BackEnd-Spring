package Baloot.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtGenerator {

    public static String generateJwt(String username, String secretKey) {
        // Set the expiration time
        Date expiration = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 1 day

        // Build the JWT
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("my_issuer")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

