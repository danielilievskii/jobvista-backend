package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterDTO {
    private String email;
    private String password;
    private String companyName;
    private String phoneNumber;
}
