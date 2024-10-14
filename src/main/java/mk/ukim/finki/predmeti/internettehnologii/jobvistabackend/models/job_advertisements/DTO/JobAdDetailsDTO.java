package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobAdDetailsDTO {
    private String recruiterName;
    private Long recruiterId;
    private final Long id;
    private String title;
    private String description;
    private String industry;
    private int startingSalary;
    private String postedOn;
    private String activeUntil;
    private boolean isActive;
    private String jobType;
    private String employmentStatus;
}
