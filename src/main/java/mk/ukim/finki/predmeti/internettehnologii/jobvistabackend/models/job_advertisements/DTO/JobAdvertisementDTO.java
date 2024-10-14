package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAdvertisementDTO {
    private Long id;
    private String title;
    private String description;
    private String industry;
    private int startingSalary;
    private String activeUntil;
    private String jobType;
    private String employmentStatus;
}
