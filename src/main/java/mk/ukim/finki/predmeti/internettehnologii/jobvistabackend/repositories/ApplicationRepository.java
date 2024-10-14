package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByJobAdvertisementId(Long jobId);
    List<Application> findAllByJobSeekerId(Long jobId);
}
