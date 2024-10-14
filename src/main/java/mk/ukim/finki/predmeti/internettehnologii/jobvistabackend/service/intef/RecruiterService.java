package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface RecruiterService {
    RecruiterDetailsDTO getRecruiterDetailsById(Long recruiterId);
    RecruiterEditDetailsDTO editRecruiterDetailsById(Long recruiterId, RecruiterEditDetailsDTO recruiterEditDetailsDTO);
    RecruiterEditDetailsDTO getRecruiterEditDetailsById(Long recruiterId);

    void submitLogo(Long recruiterId, MultipartFile logoFile);
    Resource loadLogoAsResource(Long recruiterId);
}
