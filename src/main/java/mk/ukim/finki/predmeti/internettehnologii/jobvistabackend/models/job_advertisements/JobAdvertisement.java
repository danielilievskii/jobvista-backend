package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.EmploymentStatus;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.JobType;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "job_advertisements")
public class JobAdvertisement {

    public JobAdvertisement(Recruiter recruiter, String title, String description, String industry, int startingSalary, LocalDate activeUntil, JobType jobType, EmploymentStatus employmentStatus) {
        this.recruiter = recruiter;
        this.title = title;
        this.description = description;
        this.industry = industry;
        this.startingSalary = startingSalary;
        this.postedOn = LocalDateTime.now();
        this.activeUntil = activeUntil;
        this.isActive = true;
        this.jobType = jobType;
        this.employmentStatus = employmentStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recruiter recruiter;

    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    private String industry;
    private int startingSalary;
    private LocalDateTime postedOn;
    private LocalDate activeUntil;
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    JobType jobType;

    @Enumerated(EnumType.STRING)
    EmploymentStatus employmentStatus;

    public boolean isJobAdActive() {
        LocalDate date = LocalDate.now();
        return date.isBefore(activeUntil);
    }

    public static JobAdDetailsDTO mapToJobAdDetailsDTO(JobAdvertisement jobAdvertisement) {

        return new JobAdDetailsDTO(
                jobAdvertisement.getRecruiter().getName(),
                jobAdvertisement.getRecruiter().getId(),
                jobAdvertisement.getId(),
                jobAdvertisement.getTitle(),
                jobAdvertisement.getDescription(),
                jobAdvertisement.getIndustry(),
                jobAdvertisement.getStartingSalary(),
                jobAdvertisement.getPostedOn().toString(),
                jobAdvertisement.getActiveUntil().toString(),
                jobAdvertisement.isJobAdActive(),
                jobAdvertisement.getJobType().name(),
                jobAdvertisement.getEmploymentStatus().name()
        );
    }

}
