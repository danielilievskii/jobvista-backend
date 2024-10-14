package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers;


import lombok.AllArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.Application;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdFilterDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdvertisementDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.DTO.JobAdDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.job_advertisements.JobAdvertisement;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.ApplicationService;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.JobAdvertisementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/job-advertisements")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class JobAdvertisementController {

    private final JobAdvertisementService jobAdvertisementService;
    private final ApplicationService applicationService;

    @PostMapping("/add")
    public ResponseEntity<?> addJobAdvertisement(@RequestBody JobAdvertisementDTO jobAdvertisementDTO) {
        JobAdDetailsDTO jobAdDetailsDTO = jobAdvertisementService.addJobAdvertisement(jobAdvertisementDTO);
        return new ResponseEntity<>(jobAdDetailsDTO, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editJobAdvertisement(@PathVariable Long id, @RequestBody JobAdvertisementDTO JobAdvertisementDTO) {
        JobAdDetailsDTO jobAdDetailsDTO = jobAdvertisementService.editJobAdvertisement(id, JobAdvertisementDTO);
        return new ResponseEntity<>(jobAdDetailsDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJobAdvertisement(@PathVariable Long id) {
        jobAdvertisementService.deleteJobAdvertisement(id);
        return new ResponseEntity<>("Job Advertisement with id: "+ id + "deleted.", HttpStatus.OK);
    }

    @GetMapping("/recruiter/{id}")
    public ResponseEntity<?> findAllJobAdvertisementsByRecruiterId(@PathVariable Long id) {
        List<JobAdDetailsDTO> jobAdDetailsDTOS = jobAdvertisementService.findAllJobAdvertisementsByRecruiterId(id);
        return new ResponseEntity<>(jobAdDetailsDTOS, HttpStatus.OK);
    }

    @PostMapping("/recruiter/{id}/filtered")
    public ResponseEntity<?> filterJobAdvertisementsByRecruiterId(@PathVariable Long id, @RequestBody JobAdFilterDTO jobAdFilterDTO) {
        List<JobAdDetailsDTO> jobAdDetailsDTOS = jobAdvertisementService.filterJobAdvertisementsByRecruiterId(id, jobAdFilterDTO);
        return new ResponseEntity<>(jobAdDetailsDTOS, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllJobAdvertisements() {
        List<JobAdDetailsDTO> jobAdDetailsDTOS = jobAdvertisementService.findAllJobAdvertisements();
        return new ResponseEntity<>(jobAdDetailsDTOS, HttpStatus.OK);
    }

    @PostMapping("/filtered")
    public ResponseEntity<?> filterJobAdvertisements(@RequestBody JobAdFilterDTO jobAdFilterDTO) {
        List<JobAdDetailsDTO> jobAdDetailsDTOS = jobAdvertisementService.filterJobAdvertisements(jobAdFilterDTO);
        return new ResponseEntity<>(jobAdDetailsDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findJobAdvertisementById(@PathVariable Long id) {
        JobAdDetailsDTO jobAdDetailsDTO = jobAdvertisementService.findJobAdvertisementById(id);
        return new ResponseEntity<>(jobAdDetailsDTO, HttpStatus.OK);
    }



}
