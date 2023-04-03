package EmployeeDataSystem.security;

import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Service
@NoArgsConstructor
public class JwtService {

    @Value("${jwt.secret-code}")
    private String secretKey;

    @Value("${jwt.life-time}")
    private Long lifeTime;

    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        EmployeeDetails employeeDetails = (EmployeeDetails) userDetails;

        claims.put("id", employeeDetails.getId());
        claims.put("email", employeeDetails.getEmail());
        claims.put("roles", employeeDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(employeeDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + lifeTime))
                .signWith(HS512, secretKey)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return (String)this.getClaimFromToken(token, Claims::getSubject);
    }

    public List<String> getRolesFromToken(String token) {
        return (List)this.getClaimFromToken(token, (claims) -> {
            return (List)claims.get("roles", List.class);
        });
    }

    public String getEmailFromToken(String token) {
        return (String)this.getClaimFromToken(token, (claims) -> {
            return (String)claims.get("email", String.class);
        });
    }

    public Long getIdFromToken(String token) {
        return (Long)this.getClaimFromToken(token, (claims) -> {
            return (Long)claims.get("id", Long.class);
        });
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(this.getAllClaimsFromToken(token));
    }

    public TokenData parseToken(String token) {
        return TokenData.builder()
                .id(this.getIdFromToken(token))
                .email(this.getEmailFromToken(token))
                .token(token)
                .userName(this.getUserNameFromToken(token))
                .authorities((List)this
                        .getRolesFromToken(token)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return (Claims) Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (IllegalArgumentException var3) {
            throw new IllegalArgumentException("Error! Wrong argument passed!");
        } catch (SignatureException var4) {
            throw new SignatureException("Error! Invalid signature");
        } catch (MalformedJwtException var5) {
            throw new MalformedJwtException("Error! JWT is invalid!");
        } catch (UnsupportedJwtException var6) {
            throw new UnsupportedJwtException("Error! JWT is not supported!");
        } catch (ExpiredJwtException var7) {
            throw new RuntimeException("Error! JWT has expired!");
        }
    }
}
