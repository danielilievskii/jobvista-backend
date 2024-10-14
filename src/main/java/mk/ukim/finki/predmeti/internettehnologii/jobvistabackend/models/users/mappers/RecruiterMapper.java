package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterAdminDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;

public class RecruiterMapper {

    public static RecruiterDTO mapToRecruiterDTO(Recruiter recruiter) {
        return new RecruiterDTO(
                recruiter.getEmail(),
                recruiter.getPassword(),
                recruiter.getCompanyName(),
                recruiter.getContactPhoneNumber()
        );
    }

    public static RecruiterDetailsDTO mapToRecruiterDetailsDTO(Recruiter recruiter) {
        return new RecruiterDetailsDTO(
                recruiter.getCompanyName(),
                recruiter.getCompanyDescription(),
                recruiter.getContactEmailAddress(),
                recruiter.getContactPhoneNumber(),
                recruiter.getReceptionist()
        );
    }
    public static RecruiterEditDetailsDTO mapToRecruiterEditDetailsDTO(Recruiter recruiter) {
        return new RecruiterEditDetailsDTO(
                recruiter.getEmail(),
                recruiter.getCompanyName(),
                recruiter.getCompanyDescription(),
                recruiter.getContactEmailAddress(),
                recruiter.getContactPhoneNumber(),
                recruiter.getReceptionist()
        );
    }

    public static RecruiterAdminDetailsDTO mapToRecruiterAdminDetailsDTO(Recruiter recruiter) {
        return new RecruiterAdminDetailsDTO(
                recruiter.getId(),
                recruiter.getEmail(),
                recruiter.getCompanyName(),
                recruiter.getCompanyDescription(),
                recruiter.getContactEmailAddress(),
                recruiter.getContactPhoneNumber(),
                recruiter.getReceptionist(),
                recruiter.isHasAccess(),
                recruiter.getRegisteredOn()
        );
    }

    public static Recruiter mapToRecruiter(RecruiterDTO recruiterDTO) {
        return new Recruiter(
                recruiterDTO.getEmail(),
                recruiterDTO.getPassword(),
                recruiterDTO.getCompanyName(),
                recruiterDTO.getPhoneNumber()
        );
    }

//    Using MapStruct:
//    RecruiterMapper INSTANCE = Mappers.getMapper(RecruiterMapper.class);
//
//    @Mapping(target = "email", source = "recruiterDTO.email")
//    @Mapping(target = "password", source = "recruiterDTO.password")
//    @Mapping(target = "companyName", source = "recruiterDTO.companyName")
//    @Mapping(target = "phoneNumber", source = "recruiterDTO.phoneNumber")
//    Recruiter mapToRecruiter(RecruiterDTO recruiterDTO);
//
//    @Mapping(target = "email", source = "recruiter.email")
//    @Mapping(target = "password", source = "recruiter.password")
//    @Mapping(target = "companyName", source = "recruiter.companyName")
//    @Mapping(target = "phoneNumber", source = "recruiter.phoneNumber")
//    RecruiterDTO mapToRecruiterDTO(Recruiter recruiter);
//
//    RecruiterDTO recruiterDTO = RecruiterMapper.INSTANCE.mapToRecruiterDTO(recruiter);
//    Recruiter recruiter = RecruiterMapper.INSTANCE.mapToRecruiter(recruiterDTO);
}
