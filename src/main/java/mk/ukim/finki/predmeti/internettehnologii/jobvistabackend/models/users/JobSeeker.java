package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.Role;

import java.nio.file.Paths;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_seekers")
public class JobSeeker extends User {

    public JobSeeker (String email, String password, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;

        String relativeProfilePicFilePath = Paths.get("uploads", "job-seekers", "profile-pics", "default-profile-pic.png").toString();
        this.profilePicFilePath = relativeProfilePicFilePath;

        this.role = Role.ROLE_JOBSEEKER;
    }

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String profilePicFilePath;

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
