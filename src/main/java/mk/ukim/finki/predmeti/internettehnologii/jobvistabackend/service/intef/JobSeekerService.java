package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JobSeekerEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface JobSeekerService {

    JobSeekerEditDetailsDTO editJobSeekerDetailsById(Long jobSeekerId, JobSeekerEditDetailsDTO jobSeekerEditDetailsDTO);
    JobSeekerEditDetailsDTO getJobSeekerEditDetailsById(Long jobSeekerId);

    void submitProfilePic(Long jobSeekerId, MultipartFile logoFile);
    Resource loadProfilePicAsResource(Long jobSeekerId);
}
