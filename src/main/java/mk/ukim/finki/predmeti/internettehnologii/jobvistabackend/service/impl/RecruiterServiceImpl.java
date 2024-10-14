package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers.RecruiterMapper;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.RecruiterRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final Path logoStorageLocation;

    @Autowired
    RecruiterServiceImpl(@Value("./uploads") String uploadDir, RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;

        this.logoStorageLocation = Paths.get(uploadDir + "/recruiters").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.logoStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    @Override
    public RecruiterEditDetailsDTO editRecruiterDetailsById(Long recruiterId, RecruiterEditDetailsDTO recruiterEditDetailsDTO) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        recruiter.setEmail(recruiterEditDetailsDTO.getEmail());
        recruiter.setCompanyName(recruiterEditDetailsDTO.getCompanyName());
        recruiter.setCompanyDescription(recruiterEditDetailsDTO.getCompanyDescription());
        recruiter.setContactEmailAddress(recruiterEditDetailsDTO.getContactEmail());
        recruiter.setContactPhoneNumber(recruiterEditDetailsDTO.getContactPhoneNumber());
        recruiter.setReceptionist(recruiterEditDetailsDTO.getReceptionist());
        recruiterRepository.save(recruiter);
        return RecruiterMapper.mapToRecruiterEditDetailsDTO(recruiter);
    }

    @Override
    public RecruiterEditDetailsDTO getRecruiterEditDetailsById(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        return RecruiterMapper.mapToRecruiterEditDetailsDTO(recruiter);
    }


    @Override
    public RecruiterDetailsDTO getRecruiterDetailsById(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        return RecruiterMapper.mapToRecruiterDetailsDTO(recruiter);
    }

    @Override
    public void submitLogo(Long recruiterId, MultipartFile logoFile) {

        Path recruiterLogoDir = this.logoStorageLocation.resolve(String.valueOf(recruiterId)).resolve("logos");
        try {
            Files.createDirectories(recruiterLogoDir);
            String originalFilename = logoFile.getOriginalFilename();

            if (originalFilename != null) {
                Path targetLocation = recruiterLogoDir.resolve(originalFilename);

                Files.copy(logoFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                Recruiter recruiter = recruiterRepository.findById(recruiterId)
                        .orElseThrow(() -> new RuntimeException("Recruiter not found"));
                String relativePath = Paths.get("uploads","recruiters", String.valueOf(recruiterId), "logos", originalFilename).toString();
                recruiter.setLogoFilePath(relativePath);
                recruiterRepository.save(recruiter);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }

      
    }

    public Resource loadLogoAsResource(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        try {
            String relativeLogoPath = recruiter.getLogoFilePath();
            Path logoPath = this.logoStorageLocation.getParent().getParent().resolve(relativeLogoPath);
            Resource resource = new UrlResource(logoPath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + logoPath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("File not found " + ex);
        }

    }

}
