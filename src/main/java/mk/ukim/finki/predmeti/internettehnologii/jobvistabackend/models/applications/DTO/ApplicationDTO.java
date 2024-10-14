package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ApplicationDTO {
    private Long jobSeekerId;
    private Long jobAdId;
    private MultipartFile resumeFile;
    private String answerOne;
    private String answerTwo;
    private String answerThree;
    private String messageToRecruiter;
}
