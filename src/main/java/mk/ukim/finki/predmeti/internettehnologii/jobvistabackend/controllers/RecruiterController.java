package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers;

import lombok.AllArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.RecruiterService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recruiter")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class RecruiterController {

    private final RecruiterService recruiterService;

    @GetMapping("/{id}/info")
    public ResponseEntity<?> getRecruiterDetailsById(@PathVariable("id") Long id) {
        RecruiterDetailsDTO recruiterDetailsDTO = recruiterService.getRecruiterDetailsById(id);
        return new ResponseEntity<>(recruiterDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/edit-info")
    public ResponseEntity<?> getRecruiterEditDetailsById(@PathVariable("id") Long id) {
        RecruiterEditDetailsDTO recruiterEditDetailsDTO = recruiterService.getRecruiterEditDetailsById(id);
        return new ResponseEntity<>(recruiterEditDetailsDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/edit-info")
    public ResponseEntity<?> editRecruiterDetailsById(@PathVariable("id") Long id, @RequestBody RecruiterEditDetailsDTO recruiterEditDetailsDTO) {
        RecruiterEditDetailsDTO recruiterEditDetailsDTOresp = recruiterService.editRecruiterDetailsById(id, recruiterEditDetailsDTO);
        return new ResponseEntity<>(recruiterEditDetailsDTOresp, HttpStatus.OK);
    }

    @PostMapping("/submit-logo")
    public ResponseEntity<?> submitRecruiterLogo(
            @RequestParam("recruiterId") Long recruiterId,
            @RequestParam("logoFile") MultipartFile logoFile) {
        recruiterService.submitLogo(recruiterId, logoFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/download-logo")
    public ResponseEntity<?> downloadRecruiterLogo(@PathVariable("id") Long id) {
        Resource resource = recruiterService.loadLogoAsResource(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
