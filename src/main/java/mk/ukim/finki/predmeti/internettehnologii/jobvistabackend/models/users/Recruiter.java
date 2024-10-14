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
@Table(name = "recruiters")
public class Recruiter extends User {

    public Recruiter(String email, String password, String companyName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.companyDescription = "";
        this.contactEmailAddress = email;
        this.contactPhoneNumber = phoneNumber;
        this.receptionist = "";

        String relativeLogoFilePath = Paths.get("uploads", "logo", "default-company-logo.png").toString();
        this.logoFilePath = relativeLogoFilePath;
        this.role = Role.ROLE_RECRUITER;
    }

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_description")
    private String companyDescription;

    private String contactEmailAddress;

    private String contactPhoneNumber;

    private String receptionist;

    private String logoFilePath;

    @Override
    public String getName() {
        return companyName;
    }
}
