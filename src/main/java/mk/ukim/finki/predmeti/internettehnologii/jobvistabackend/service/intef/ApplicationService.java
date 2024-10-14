package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;


import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.Application;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationStatusDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApplicationService {
    ApplicationDetailsDTO submitApplication(ApplicationDTO applicationDTO);
    ApplicationDetailsDTO updateApplication(Long applicationId, MultipartFile[] additionalFiles);
    List<ApplicationDetailsDTO> findAllByJobAdvertisementId(Long jobId);
    List<ApplicationDetailsDTO> filterByJobAdvertisementId(Long jobId, String status);
    List<ApplicationDetailsDTO> findAllByJobSeekerId(Long jobSeekerId);
    List<ApplicationDetailsDTO> filterByJobSeekerId(Long jobSeekerId, String status);
    Resource loadResumeAsResource(Long applicationId);
    List<String> loadAdditionalFilesAsUrls(Long applicationId);
    ApplicationStatusDTO updateApplicationStatus(Long id, String status);
    List<ApplicationStatusDTO> updateApplications(List<ApplicationStatusDTO> updates);
}
