package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JobSeekerDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JobSeekerEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;

public class JobSeekerMapper {

    public static JobSeekerDTO mapToJobSeekerDTO(JobSeeker jobSeeker) {
        return new JobSeekerDTO(
                jobSeeker.getEmail(),
                jobSeeker.getPassword(),
                jobSeeker.getFirstName(),
                jobSeeker.getLastName(),
                jobSeeker.getPhoneNumber()
        );
    }

    public static JobSeeker mapToJobSeeker(JobSeekerDTO jobSeekerDTO) {
        return new JobSeeker(
                jobSeekerDTO.getEmail(),
                jobSeekerDTO.getPassword(),
                jobSeekerDTO.getFirstName(),
                jobSeekerDTO.getLastName(),
                jobSeekerDTO.getPhoneNumber()
        );
    }

    public static JobSeekerEditDetailsDTO mapToJobSeekerEditDetailsDTO(JobSeeker jobSeeker) {
        return new JobSeekerEditDetailsDTO(
                jobSeeker.getEmail(),
                jobSeeker.getFirstName(),
                jobSeeker.getLastName(),
                jobSeeker.getPhoneNumber()
        );
    }

}
