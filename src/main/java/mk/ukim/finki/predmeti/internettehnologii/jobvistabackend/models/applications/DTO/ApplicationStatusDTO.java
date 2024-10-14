package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationStatusDTO {
    Long id;
    String status;
    String response;
}
