package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterAdminDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;

import java.util.List;

public interface AdminService {
    RecruiterAdminDetailsDTO changeAccess(long recruiterId, boolean access);
    List<RecruiterAdminDetailsDTO> findAllRecruiters();
}
