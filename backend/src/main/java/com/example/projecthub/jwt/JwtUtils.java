package com.example.projecthub.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    // Access token expiration: 15 minutes
    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    // Refresh token expiration: 7 days
    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;

    //for api

    public String generateAccessToken(UserDetails userDetails) {
        return accessTokenToken(new HashMap<>(), userDetails);
    }

    public String accessTokenToken(Map<String, Object> extraClaims,UserDetails userDetails) {
        return buildToken(extraClaims,userDetails,accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration );
    }

    public boolean isTokenValid(String token,UserDetails userDetails) {
        String username = extractUsername(token);
      return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    //   .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(userDetails.getPassword())))
    private String buildToken(
            Map<String, Object> claims,
            UserDetails userDetails,
            long expirationTime) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSignInKey() {
        byte[] secretKeys = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeys);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
      return  extractClaims(token, Claims::getSubject);
    }

    public <T>T extractClaims(String token, Function<Claims, T> claimsResolver) {
       final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     * This decodes and parses the JWT
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
}
