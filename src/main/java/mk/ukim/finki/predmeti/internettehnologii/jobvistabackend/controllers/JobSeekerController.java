package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers;


import lombok.AllArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JobSeekerEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterEditDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.JobSeekerService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/job-seeker")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class JobSeekerController {
    private final JobSeekerService jobSeekerService;

    @GetMapping("/{id}/edit-info")
    public ResponseEntity<?> getJobSeekerEditDetailsById(@PathVariable("id") Long id) {
        JobSeekerEditDetailsDTO jobSeekerEditDetailsDTO = jobSeekerService.getJobSeekerEditDetailsById(id);
        return new ResponseEntity<>(jobSeekerEditDetailsDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/edit-info")
    public ResponseEntity<?> editRecruiterDetailsById(@PathVariable("id") Long id, @RequestBody JobSeekerEditDetailsDTO jobSeekerEditDetailsDTO) {
        JobSeekerEditDetailsDTO jobSeekerEditDetailsDTOresp = jobSeekerService.editJobSeekerDetailsById(id, jobSeekerEditDetailsDTO);
        return new ResponseEntity<>(jobSeekerEditDetailsDTOresp, HttpStatus.OK);
    }

    @PostMapping("/submit-profile-pic")
    public ResponseEntity<?> submitJobSeekerProfilePic(
            @RequestParam("jobSeekerId") Long jobSeekerId,
            @RequestParam("profilePicFile") MultipartFile profilePicFile) {
        jobSeekerService.submitProfilePic(jobSeekerId, profilePicFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/download-profile-pic")
    public ResponseEntity<?> downloadJobSeekerProfilePic(@PathVariable("id") Long id) {
        Resource resource = jobSeekerService.loadProfilePicAsResource(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
