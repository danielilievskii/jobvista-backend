package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String generateToken(User user);
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);

}
