package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends User {

//    private String name;
//    private String surname;

    @Override
    public String getName() {
        return "Admin";
    }
}
