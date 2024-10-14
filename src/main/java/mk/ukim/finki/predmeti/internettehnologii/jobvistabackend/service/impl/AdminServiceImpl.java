package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterAdminDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers.RecruiterMapper;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.RecruiterRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.AdminService;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.RecruiterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final RecruiterService recruiterService;
    private final RecruiterRepository recruiterRepository;

    @Override
    public RecruiterAdminDetailsDTO changeAccess(long recruiterId, boolean access) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        if (recruiter != null) {
            recruiter.setHasAccess(access);
            recruiterRepository.save(recruiter);
            return RecruiterMapper.mapToRecruiterAdminDetailsDTO(recruiter);
        }
        return null;

    }

    @Override
    public List<RecruiterAdminDetailsDTO> findAllRecruiters() {
        List<Recruiter> recruiterList = recruiterRepository.findAll();
        return recruiterList.stream().map(RecruiterMapper::mapToRecruiterAdminDetailsDTO).toList();
    }
}
