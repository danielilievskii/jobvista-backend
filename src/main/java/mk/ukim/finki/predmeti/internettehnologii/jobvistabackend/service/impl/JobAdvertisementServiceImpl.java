package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.EmploymentStatus;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.JobType;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdFilterDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdvertisementDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.JobAdvertisement;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.JobAdvertisementRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.RecruiterRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.UserRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.JobAdvertisementService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobAdvertisementServiceImpl implements JobAdvertisementService {
    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final RecruiterRepository recruiterRepository;

    @Override
    public JobAdDetailsDTO addJobAdvertisement(JobAdvertisementDTO jobAdvertisementDTO) {
        Recruiter recruiter = recruiterRepository.findById(jobAdvertisementDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        JobAdvertisement jobAdvertisement = new JobAdvertisement(
                recruiter,
                jobAdvertisementDTO.getTitle(),
                jobAdvertisementDTO.getDescription(),
                jobAdvertisementDTO.getIndustry(),
                jobAdvertisementDTO.getStartingSalary(),
                LocalDate.parse(jobAdvertisementDTO.getActiveUntil()),
                JobType.valueOf(jobAdvertisementDTO.getJobType()),
                EmploymentStatus.valueOf(jobAdvertisementDTO.getEmploymentStatus())
        );
        jobAdvertisementRepository.save(jobAdvertisement);
        return JobAdvertisement.mapToJobAdDetailsDTO(jobAdvertisement);
    }

    @Override
    public JobAdDetailsDTO editJobAdvertisement(Long id, JobAdvertisementDTO jobAdvertisementDTO) {
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job Advertisement not found"));
        jobAdvertisement.setTitle(jobAdvertisementDTO.getTitle());
        jobAdvertisement.setDescription(jobAdvertisementDTO.getDescription());
        jobAdvertisement.setIndustry(jobAdvertisementDTO.getIndustry());
        jobAdvertisement.setStartingSalary(jobAdvertisementDTO.getStartingSalary());
        jobAdvertisement.setActiveUntil(LocalDate.parse(jobAdvertisementDTO.getActiveUntil()));
        jobAdvertisement.setActive(jobAdvertisement.isJobAdActive());
        jobAdvertisement.setJobType(JobType.valueOf(jobAdvertisementDTO.getJobType()));
        jobAdvertisement.setEmploymentStatus(EmploymentStatus.valueOf(jobAdvertisementDTO.getEmploymentStatus()));
        jobAdvertisementRepository.save(jobAdvertisement);
        return JobAdvertisement.mapToJobAdDetailsDTO(jobAdvertisement);
    }

    @Override
    public void deleteJobAdvertisement(Long jobAdvertisementId) {
        jobAdvertisementRepository.deleteById(jobAdvertisementId);
    }

    @Override
    public List<JobAdDetailsDTO> findAllJobAdvertisements() {
        List<JobAdvertisement> jobAdvertisementList = jobAdvertisementRepository.findAll();
        jobAdvertisementList.forEach(jobAdvertisement -> {
            if (!jobAdvertisement.isJobAdActive() && jobAdvertisement.isActive()) {
                jobAdvertisement.setActive(false);
                jobAdvertisementRepository.save(jobAdvertisement);
            } else if (jobAdvertisement.isJobAdActive() && !jobAdvertisement.isActive()) {
                jobAdvertisement.setActive(true);
                jobAdvertisementRepository.save(jobAdvertisement);
            }
        });

        return jobAdvertisementList.stream()
                .map(JobAdvertisement::mapToJobAdDetailsDTO)
                .toList();
    }

    @Override
    public List<JobAdDetailsDTO> filterJobAdvertisements(JobAdFilterDTO jobAdFilterDTO) {
        List<JobAdvertisement> filteredJobAds = jobAdvertisementRepository.findAll();
        filteredJobAds = filter(filteredJobAds, jobAdFilterDTO);

        return filteredJobAds.stream()
                .map(JobAdvertisement::mapToJobAdDetailsDTO)
                .toList();
    }

    private List<JobAdvertisement> filter(List<JobAdvertisement> filteredJobAdvertisements, JobAdFilterDTO jobAdFilterDTO) {
        return filteredJobAdvertisements.stream()
                .filter(jobAd -> jobAd.getTitle().toLowerCase().contains(jobAdFilterDTO.getSearchTerm().toLowerCase()))
                .filter(jobAd -> jobAdFilterDTO.getIndustry().equals("all") || jobAd.getIndustry().equals(jobAdFilterDTO.getIndustry()))
                .sorted(getComparator(jobAdFilterDTO.getSortOrder()))
                .toList();
    }

    private Comparator<JobAdvertisement> getComparator(String sortOrder) {
        return switch (sortOrder) {
            case "date_newest" -> Comparator.comparing(JobAdvertisement::getPostedOn).reversed();
            case "date_oldest" -> Comparator.comparing(JobAdvertisement::getPostedOn);
            case "salary_highest" -> Comparator.comparing(JobAdvertisement::getStartingSalary).reversed();
            case "salary_lowest" -> Comparator.comparing(JobAdvertisement::getStartingSalary);
            default -> Comparator.comparing(JobAdvertisement::getPostedOn); // Default sorting order
        };
    }


    @Override
    public List<JobAdDetailsDTO> findAllJobAdvertisementsByRecruiterId(Long recruiterId) {
        List<JobAdvertisement> jobAdvertisementList = jobAdvertisementRepository.findAllByRecruiterId(recruiterId);

        return jobAdvertisementList.stream()
                .map(JobAdvertisement::mapToJobAdDetailsDTO)
                .toList();
    }

    @Override
    public List<JobAdDetailsDTO> filterJobAdvertisementsByRecruiterId(Long recruiterId, JobAdFilterDTO jobAdFilterDTO) {
        List<JobAdvertisement> filteredJobAds = jobAdvertisementRepository.findAllByRecruiterId(recruiterId);
        filteredJobAds = filter(filteredJobAds, jobAdFilterDTO);
        return filteredJobAds.stream()
                .map(JobAdvertisement::mapToJobAdDetailsDTO)
                .toList();
    }

    @Override
    public JobAdDetailsDTO findJobAdvertisementById(Long id) {
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(id).orElse(null);
        return JobAdvertisement.mapToJobAdDetailsDTO(jobAdvertisement);
    }
}
