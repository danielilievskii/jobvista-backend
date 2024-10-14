package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
    private boolean hasAccess;
    private String token;
    private String refreshToken;
}
