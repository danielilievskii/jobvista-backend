package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.Role;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByRole(Role role);
}
