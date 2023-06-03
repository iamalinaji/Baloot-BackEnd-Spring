package Baloot.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtGenerator {

    public static String generateJwt(String username,String secretKey) {

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
    public boolean validateToken(String token,String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}

