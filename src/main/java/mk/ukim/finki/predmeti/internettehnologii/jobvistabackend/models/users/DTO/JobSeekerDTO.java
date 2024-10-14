package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
