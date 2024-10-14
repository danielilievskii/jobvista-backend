package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.JobAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobAdvertisementRepository extends JpaRepository<JobAdvertisement, Long> {
    List<JobAdvertisement> findAllByRecruiterId(Long id);
}
