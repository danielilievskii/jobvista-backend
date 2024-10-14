package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationStatusDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.ApplicationStatus;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.JobSeekerRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.EmailSenderService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.Application;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.JobAdvertisement;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.ApplicationRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.JobAdvertisementRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.UserRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final Path fileStorageLocation;

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final JobSeekerRepository jobSeekerRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    public ApplicationServiceImpl(@Value("${file.upload-dir}") String uploadDir, UserRepository userRepository, ApplicationRepository applicationRepository, JobAdvertisementRepository jobAdvertisementRepository,
                                  JobSeekerRepository jobSeekerRepository) {
        this.fileStorageLocation = Paths.get(uploadDir + "/applications").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    @Override
    public ApplicationDetailsDTO submitApplication(ApplicationDTO applicationDTO) {

        JobSeeker jobSeeker = jobSeekerRepository.findById(applicationDTO.getJobSeekerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(applicationDTO.getJobAdId())
                .orElseThrow(() -> new IllegalArgumentException("Job advertisement not found."));

        if (applicationDTO.getResumeFile().isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        List<String> answers = new ArrayList<>();
        answers.add(applicationDTO.getAnswerOne());
        answers.add(applicationDTO.getAnswerTwo());
        answers.add(applicationDTO.getAnswerThree());

        Application application = new Application(jobSeeker, jobAdvertisement,
                answers,applicationDTO.getMessageToRecruiter());
        application = applicationRepository.save(application);

        Path filePath = this.fileStorageLocation.resolve(String.valueOf(application.getId())).resolve("resume");
        Path targetLocation = filePath.resolve(applicationDTO.getResumeFile().getOriginalFilename());

        try {
            Files.createDirectories(filePath);
            Files.copy(applicationDTO.getResumeFile().getInputStream(), targetLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String relativePath = Paths.get("uploads","applications",String.valueOf(application.getId()),
                "resume", applicationDTO.getResumeFile().getOriginalFilename()).toString();
        application.setResumeFilePath(relativePath);
        application = applicationRepository.save(application);

        return Application.mapToApplicationDetailsDTO(application);
    }

    @Override
    public ApplicationDetailsDTO updateApplication(Long applicationId, MultipartFile[] additionalFiles) {
        Application application = applicationRepository.findById(applicationId).orElse(null);
        if(application== null) {
            throw new RuntimeException("Application not found.");
        }

        for (MultipartFile additionalFile : additionalFiles) {
            if (additionalFile.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
        }

        Path filesPath = this.fileStorageLocation.resolve(String.valueOf(application.getId())).resolve("additional_files");
        for(MultipartFile additionalFile: additionalFiles) {
            Path targetLocation = filesPath.resolve(additionalFile.getOriginalFilename());

            try {
                Files.createDirectories(filesPath);
                Files.copy(additionalFile.getInputStream(), targetLocation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String relativePath = Paths.get("uploads","applications",String.valueOf(application.getId()),
                    "additional_files", additionalFile.getOriginalFilename()).toString();
            List<String> currentAdditionalFilePaths = application.getAdditionalFilePaths();
            currentAdditionalFilePaths.add(relativePath);
            application.setAdditionalFilePaths(currentAdditionalFilePaths);
            application = applicationRepository.save(application);
        }
        return Application.mapToApplicationDetailsDTO(application);
    }

    @Override
    public List<ApplicationDetailsDTO> findAllByJobAdvertisementId(Long jobId) {
        List<Application> applications =  applicationRepository.findAllByJobAdvertisementId(jobId);
        return applications.stream().map(Application::mapToApplicationDetailsDTO).toList();
    }

    @Override
    public List<ApplicationDetailsDTO> filterByJobAdvertisementId(Long jobId, String status) {
        List<Application> applications =  applicationRepository.findAllByJobAdvertisementId(jobId);
        String statusTrimmed = status.subSequence(0, status.length()-1).toString();

        if(statusTrimmed.equals("ALL")) {
            applications =  applicationRepository.findAllByJobAdvertisementId(jobId);
        } else {
            applications = applications.stream().filter(application -> application.getStatus().name().equals(statusTrimmed)).toList();
        }
        return applications.stream().map(Application::mapToApplicationDetailsDTO).toList();
    }

    @Override
    public List<ApplicationDetailsDTO> findAllByJobSeekerId(Long jobSeekerId) {
       List<Application> applications = applicationRepository.findAllByJobSeekerId(jobSeekerId);
       return applications.stream().map(Application::mapToApplicationDetailsDTO).toList();
    }

    @Override
    public List<ApplicationDetailsDTO> filterByJobSeekerId(Long jobSeekerId, String status) {
        List<Application> applications = applicationRepository.findAllByJobSeekerId(jobSeekerId);
        String statusTrimmed = status.subSequence(0, status.length()-1).toString();
        if(statusTrimmed.equals("ALL")) {
            applications =  applicationRepository.findAllByJobSeekerId(jobSeekerId);
        } else {
            applications = applications.stream().filter(application -> application.getStatus().name().equals(statusTrimmed)).toList();
        }
        return applications.stream().map(Application::mapToApplicationDetailsDTO).toList();
    }

    @Override
    public Resource loadResumeAsResource(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).
                orElseThrow(() -> new IllegalArgumentException("Application not found"));

        String relativeFilePath = application.getResumeFilePath();
        Path filePath = fileStorageLocation.getParent().getParent().resolve(relativeFilePath).normalize();

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + relativeFilePath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("File path is invalid: " + relativeFilePath, ex);
        }
    }

    public List<String> loadAdditionalFilesAsUrls(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        List<String> fileUrls = new ArrayList<>();
        List<String> relativeFilePaths = application.getAdditionalFilePaths();

        for (String relativeFilePath : relativeFilePaths) {
            //TO DO: refactor
            Path filePath = Paths.get(fileStorageLocation.getParent().getParent().toString(), relativeFilePath).normalize();
            String relativePath = filePath.toString().replace("\\", "/").replaceFirst("^.+uploads", "uploads");

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/")
                    .path(relativePath)
                    .toUriString();
            fileUrls.add(fileUrl);
        }

        return fileUrls;
    }

   /* @Override
    public List<Resource> loadAdditionalFilesAsZippedResource(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).
                orElseThrow(() -> new IllegalArgumentException("Application not found"));

        List<Resource> resources = new ArrayList<>();

        List<String> relativeFilePaths = application.getAdditionalFilePaths();
        for(String relativeFilePath: relativeFilePaths) {
            Path filePath = fileStorageLocation.getParent().getParent().resolve(relativeFilePath).normalize();

            try {
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists()) {
                    resources.add(resource);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return resources;
    }*/

    @Override
    public List<ApplicationStatusDTO> updateApplications(List<ApplicationStatusDTO> updates) {
        List<ApplicationStatusDTO> updatedApplications = new ArrayList<>();

        for(ApplicationStatusDTO applicationStatusDTO : updates) {
            Application application = applicationRepository.findById(applicationStatusDTO.getId()).orElse(null);
            if(application != null) {
                application.setStatus(ApplicationStatus.valueOf(applicationStatusDTO.getStatus()));
                application.setResponse(applicationStatusDTO.getResponse());
                applicationRepository.save(application);
                updatedApplications.add(applicationStatusDTO);

                String email = application.getJobSeeker().getEmail();
                String subject = application.getJobAdvertisement().getRecruiter().getName() + ": " + application.getJobAdvertisement().getTitle() + " - STATUS UPDATE";
                String text = "Dear " + application.getJobSeeker().getName() + ",\n\n";

                switch (applicationStatusDTO.getStatus()) {
                    case "ACCEPTED":
                        text += "Great news! Your application has been accepted.\n\n";
                        break;
                    case "DENIED":
                        text += "We regret to inform you that your application has been denied. We appreciate your interest and effort.\n\n";
                        break;
                    case "PROPOSED":
                        text += "Your application status has been updated to 'Proposed'. We're considering your application for the next phase.\n\n";
                        break;
                    case "UNDER_REVIEW":
                        text += "Your application is currently under review.\n\n";
                        break;
                }

                if(!applicationStatusDTO.getResponse().isEmpty()) {
                    text += "Response: " + applicationStatusDTO.getResponse() + "\n\n";
                }
               text += "Thank you.";
           emailSenderService.sendEmail(email, subject, text);
            }
        }
        return updatedApplications;
    }

    @Override
    public ApplicationStatusDTO updateApplicationStatus(Long id, String status) {
       Application application = applicationRepository.findById(id).orElse(null);
        System.out.println(status);
       application.setStatus(ApplicationStatus.valueOf(status));
       applicationRepository.save(application);
       return new ApplicationStatusDTO(id, status, "");
    }
}
