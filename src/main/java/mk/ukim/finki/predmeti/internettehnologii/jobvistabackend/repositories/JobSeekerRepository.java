package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    Optional<JobSeeker> findByEmail(String email);
}
