package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.Role;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Admin;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class JobvistaBackendApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(JobvistaBackendApplication.class, args);
	}

	public void run(String... args) throws Exception {
		Admin adminAccount = (Admin) userRepository.findByRole(Role.ROLE_ADMIN);
		if(adminAccount == null) {
			Admin admin = new Admin();
			admin.setRole(Role.ROLE_ADMIN);
			admin.setEmail("admin@admin.com");
			admin.setHasAccess(true);
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(admin);
		}
	}
}
