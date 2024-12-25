package ba.unsa.etf.cineapp.service.auth;

import ba.unsa.etf.cineapp.repository.auth.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

  @Value("${jwt.secret}")
  private String secret;
  private final TokenRepository tokenRepository;

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder().setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 5))
        .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsTFunction.apply(claims);
  }

  private Key getSignatureKey() {
    byte[] key = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(key);
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    boolean isValid = tokenRepository.findByToken(token).map(t->!t.getLoggedOut()).orElseThrow();
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isValid);
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder().setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() * 604800000))
        .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
        .compact();
  }
}
