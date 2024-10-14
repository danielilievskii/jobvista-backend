package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterDetailsDTO {
    private String companyName;
    private String companyDescription;
    private String contactEmail;
    private String contactPhoneNumber;
    private String receptionist;
}
