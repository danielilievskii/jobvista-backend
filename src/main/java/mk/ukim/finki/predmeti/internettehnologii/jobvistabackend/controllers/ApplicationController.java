package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers;

import lombok.AllArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.Application;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.applications.DTO.ApplicationStatusDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/my-applications/{id}")
    public ResponseEntity<?> findAllApplicationsByJobSeekerId(@PathVariable Long id) {
        List<ApplicationDetailsDTO> applicationList = applicationService.findAllByJobSeekerId(id);
        return new ResponseEntity<>(applicationList, HttpStatus.OK);
    }

    @PostMapping("/my-applications/{id}/filtered")
    public ResponseEntity<?> filterApplicationsByJobSeekerId(@PathVariable Long id, @RequestBody String status) {
        List<ApplicationDetailsDTO> applicationList = applicationService.filterByJobSeekerId(id, status);
        return new ResponseEntity<>(applicationList, HttpStatus.OK);
    }

    @GetMapping("/job-advertisements/{advertisement_id}/applications")
    public ResponseEntity<?> findAllApplicationsByJobAdvertisementId(@PathVariable("advertisement_id") Long advertisementId) {
        List<ApplicationDetailsDTO> applicationList = applicationService.findAllByJobAdvertisementId(advertisementId);
        return new ResponseEntity<>(applicationList, HttpStatus.OK);
    }

    @PostMapping("/job-advertisements/{advertisement_id}/applications/filtered")
    public ResponseEntity<?> filterApplicationsByJobAdvertisementId(@PathVariable("advertisement_id") Long advertisementId, @RequestBody String status) {
        List<ApplicationDetailsDTO> applicationList = applicationService.filterByJobAdvertisementId(advertisementId, status);
         return new ResponseEntity<>(applicationList, HttpStatus.OK);
    }

    @PostMapping("/applications/{id}/update/NOT-IN-USE")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable("id") Long applicaitonId, @RequestBody ApplicationStatusDTO appStatusDTO) {
        ApplicationStatusDTO applicationStatusDTO = applicationService.updateApplicationStatus(applicaitonId,appStatusDTO.getStatus());
        return new ResponseEntity<>(applicationStatusDTO, HttpStatus.OK);
    }

    @PostMapping("/applications/update")
    public ResponseEntity<?> updateApplications(@RequestBody List<ApplicationStatusDTO> changes) {
       List<ApplicationStatusDTO> updatedApplications = applicationService.updateApplications(changes);
       return new ResponseEntity<>(updatedApplications, HttpStatus.OK);
    }

    @GetMapping("/applications/{id}/download-resume")
    public ResponseEntity<Resource> downloadResume(@PathVariable("id") Long applicationId) {
        Resource resource = applicationService.loadResumeAsResource(applicationId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/applications/submit")
    public ResponseEntity<ApplicationDetailsDTO> submitApplication(
            @RequestParam("jobSeekerId") Long jobSeekerId,
            @RequestParam("jobAdId") Long jobAdId,
            @RequestParam("resumeFile") MultipartFile resumeFile,
            @RequestParam("answerOne") String answerOne,
            @RequestParam("answerTwo") String answerTwo,
            @RequestParam("answerThree") String answerThree,
            @RequestParam("messageToRecruiter") String messageToRecruiter) {

        ApplicationDTO applicationDTO = new ApplicationDTO(jobSeekerId, jobAdId,
                resumeFile, answerOne, answerTwo, answerThree, messageToRecruiter);
        ApplicationDetailsDTO applicationDetailsDTO = applicationService.submitApplication(applicationDTO);
        return new ResponseEntity<>(applicationDetailsDTO, HttpStatus.OK);
    }

    @PostMapping("/applications/{id}/update")
    public ResponseEntity<ApplicationDetailsDTO> updateApplication(
            @PathVariable("id") Long applicationId,
            @RequestParam("additionalFiles") MultipartFile[] additionalFiles) {
        ApplicationDetailsDTO applicationDetailsDTO = applicationService.updateApplication(applicationId, additionalFiles);
        return new ResponseEntity<>(applicationDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("/applications/{id}/download-additional-files")
    public ResponseEntity<List<String>> getAdditionalFilesUrls(@PathVariable("id") Long applicationId) {
        List<String> fileUrls = applicationService.loadAdditionalFilesAsUrls(applicationId);
        return ResponseEntity.ok(fileUrls);
    }

}
