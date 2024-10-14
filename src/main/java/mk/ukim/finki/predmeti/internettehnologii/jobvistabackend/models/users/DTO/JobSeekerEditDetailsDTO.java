package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerEditDetailsDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
