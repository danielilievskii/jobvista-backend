package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.ApplicationStatus;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.JobAdvertisement;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JobSeeker jobSeeker;

    @ManyToOne
    private JobAdvertisement jobAdvertisement;

    @Column(name = "resume_file_name", nullable = true)
    private String resumeFilePath;

    @ElementCollection
    private List<String> questionAnswers;

    private String message;

    private LocalDateTime submittedOn;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String response;

    @ElementCollection
    private List<String> additionalFilePaths;

    public Application(JobSeeker jobSeeker, JobAdvertisement jobAdvertisement, List<String> answers, String message) {
        this.jobSeeker = jobSeeker;
        this.jobAdvertisement = jobAdvertisement;
        this.resumeFilePath = "";
        this.questionAnswers = answers;
        this.message = message;
        submittedOn = LocalDateTime.now();
        this.status = ApplicationStatus.PROPOSED;
        this.response = "";
        this.additionalFilePaths = new ArrayList<>();
    }

    public static ApplicationDetailsDTO mapToApplicationDetailsDTO (Application application) {
        return new ApplicationDetailsDTO(
                application.getId(),
                application.getJobSeeker().getId(),
                application.getJobSeeker().getName(),
                application.getJobSeeker().getEmail(),
                application.getJobSeeker().getPhoneNumber(),
                application.getJobAdvertisement().getRecruiter().getId(),
                application.getJobAdvertisement().getRecruiter().getName(),
                application.getJobAdvertisement().getRecruiter().getContactEmailAddress(),
                application.getJobAdvertisement().getRecruiter().getContactPhoneNumber(),
                application.getJobAdvertisement().getId(),
                application.getJobAdvertisement().getTitle(),
                application.getQuestionAnswers(),
                application.getResumeFilePath(),
                application.getMessage(),
                application.getSubmittedOn(),
                application.getStatus().name(),
                application.getResponse(),
                application.getAdditionalFilePaths()
        );
    }
}
