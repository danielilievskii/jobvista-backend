package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterEditDetailsDTO {
    private String email;
    private String companyName;
    private String companyDescription;
    private String contactEmail;
    private String contactPhoneNumber;
    private String receptionist;
}
