package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdFilterDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdvertisementDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.JobAdvertisement;

import java.util.List;
import java.util.Optional;

public interface JobAdvertisementService {
    JobAdDetailsDTO addJobAdvertisement(JobAdvertisementDTO jobAdvertisementDTO);
    JobAdDetailsDTO editJobAdvertisement(Long id, JobAdvertisementDTO jobAdvertisementDTO);
    void deleteJobAdvertisement(Long jobAdvertisementId);

    List<JobAdDetailsDTO> findAllJobAdvertisements();
    List<JobAdDetailsDTO> filterJobAdvertisements(JobAdFilterDTO jobAdFilterDTO);
    List<JobAdDetailsDTO> findAllJobAdvertisementsByRecruiterId(Long recruiterId);
    List<JobAdDetailsDTO> filterJobAdvertisementsByRecruiterId(Long recruiterId, JobAdFilterDTO jobAdFilterDTO);
    JobAdDetailsDTO findJobAdvertisementById(Long id);


}
