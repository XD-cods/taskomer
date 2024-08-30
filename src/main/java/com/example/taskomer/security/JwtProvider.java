package com.example.taskomer.security;

import com.example.taskomer.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
  private final SecretKey jwtAccessSecret;
  private final SecretKey jwtRefreshSecret;


  public JwtProvider(
          @Value("${jwt.secret.access}") String jwtAccessSecret,
          @Value("${jwt.secret.refresh}") String jwtRefreshSecret
  ) {
    this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
  }

  public String generateAccessToken(User user) {
    LocalDateTime now = LocalDateTime.now();
    Instant accessExpirationTime = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
    Date accessExpiration = Date.from(accessExpirationTime);

    return Jwts.builder()
            .subject(user.getName())
            .expiration(accessExpiration)
            .signWith(jwtAccessSecret)
            .claim("roles", user.getRole())
            .claim("username", user.getName())
            .compact();
  }

  public String generateRefreshToken(User user) {
    LocalDateTime now = LocalDateTime.now();
    Instant accessExpirationTime = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
    Date accessExpiration = Date.from(accessExpirationTime);

    return Jwts.builder()
            .subject(user.getName())
            .expiration(accessExpiration)
            .signWith(jwtAccessSecret)
            .compact();
  }

  public boolean validateAccessToken(String token) {
    return validateToken(token, jwtAccessSecret);
  }

  public boolean validateRefreshToken(String token) {
    return validateToken(token, jwtRefreshSecret);
  }

  private boolean validateToken(String token, SecretKey secret) {
    try {
      Jwts.parser()
              .verifyWith(secret)
              .build()
              .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException expEx) {
      log.error("Token expired", expEx);
    } catch (UnsupportedJwtException unsEx) {
      log.error("Unsupported jwt", unsEx);
    } catch (MalformedJwtException mjEx) {
      log.error("Malformed jwt", mjEx);
    } catch (SignatureException sEx) {
      log.error("Invalid signature", sEx);
    } catch (Exception e) {
      log.error("invalid token", e);
      return false;
    }
    return true;
  }

  public Claims getAccessClaims(String token) {
    return getClaims(token, jwtAccessSecret);
  }

  public Claims getRefreshClaims(String token) {
    return getClaims(token, jwtRefreshSecret);
  }

  private Claims getClaims(String token, SecretKey secret) {
    return Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

}
