package Baloot.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtGenerator {
    public static String generateJwt() {
        // Generate a secure key
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Set the expiration time
        Date expiration = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 1 day

        // Build the JWT
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("my_issuer")
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String keyString = key.toString();

        return jwt;
    }
}

