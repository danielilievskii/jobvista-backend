package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JobSeekerEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers.JobSeekerMapper;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.JobSeekerRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {

    private final JobSeekerRepository jobSeekerRepository;
    private final Path profilePicStorageLocation;

    @Autowired
    JobSeekerServiceImpl(@Value("./uploads") String uploadDir, JobSeekerRepository jobSeekerRepository) {
        this.jobSeekerRepository = jobSeekerRepository;

        this.profilePicStorageLocation = Paths.get(uploadDir + "/job-seekers").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.profilePicStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public JobSeekerEditDetailsDTO editJobSeekerDetailsById(Long jobSeekerId, JobSeekerEditDetailsDTO jobSeekerEditDetailsDTO) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        jobSeeker.setEmail(jobSeekerEditDetailsDTO.getEmail());
        jobSeeker.setFirstName(jobSeekerEditDetailsDTO.getFirstName());
        jobSeeker.setLastName(jobSeekerEditDetailsDTO.getLastName());
        jobSeeker.setPhoneNumber(jobSeekerEditDetailsDTO.getPhoneNumber());
        jobSeekerRepository.save(jobSeeker);
        return JobSeekerMapper.mapToJobSeekerEditDetailsDTO(jobSeeker);
    }

    @Override
    public JobSeekerEditDetailsDTO getJobSeekerEditDetailsById(Long jobSeekerId) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        return JobSeekerMapper.mapToJobSeekerEditDetailsDTO(jobSeeker);
    }

    @Override
    public void submitProfilePic(Long jobSeekerId, MultipartFile profilePicFile) {
        Path jobSeekerProfilePicDir = this.profilePicStorageLocation.resolve(String.valueOf(jobSeekerId)).resolve("profile-pics");

        try {
            Files.createDirectories(jobSeekerProfilePicDir);
            String originalFileName = profilePicFile.getOriginalFilename();

            if(originalFileName != null) {
                Path targetLocation = jobSeekerProfilePicDir.resolve(originalFileName);
                Files.copy(profilePicFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
                String relativePath = Paths.get("uploads", "job-seekers", String.valueOf(jobSeekerId), "profile-pics", originalFileName).toString();

                jobSeeker.setProfilePicFilePath(relativePath);
                jobSeekerRepository.save(jobSeeker);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource loadProfilePicAsResource(Long jobSeekerId) {
       JobSeeker jobSeeker =  jobSeekerRepository.findById(jobSeekerId)
               .orElseThrow(() -> new RuntimeException("Job Seeker not found"));

       try {
           String relativeProfilePicPath = jobSeeker.getProfilePicFilePath();
           Path profilePicPath = profilePicStorageLocation.getParent().getParent().resolve(relativeProfilePicPath);
            Resource resource = new UrlResource(profilePicPath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Could not find profile pic at " + profilePicPath);
            }
       } catch (IOException ex) {
           throw new RuntimeException(ex);
       }


    }
}
