package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInDTO { //SignInRequest


    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
