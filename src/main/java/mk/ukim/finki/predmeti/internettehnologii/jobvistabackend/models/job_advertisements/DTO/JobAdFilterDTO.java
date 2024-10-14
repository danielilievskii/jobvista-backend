package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobAdFilterDTO {
    String searchTerm;
    String industry;
    String sortOrder;
}
