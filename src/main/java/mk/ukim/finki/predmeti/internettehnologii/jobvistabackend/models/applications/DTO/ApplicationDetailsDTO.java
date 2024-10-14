package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApplicationDetailsDTO {
    private Long id;
    private Long jobSeekerId;
    private String jobSeekerName;
    private String jobSeekerEmail;
    private String jobSeekerPhoneNumber;
    private Long recruiterId;
    private String recruiterName;
    private String recruiterEmail;
    private String recruiterPhoneNumber;
    private Long jobAdId;
    private String jobAdTitle;
    private List<String> questionAnswers;
    private String fileName;
    private String message;
    private LocalDateTime submittedOn;
    private String status;
    private String response;
    private List<String> additionalFileNames;
}
