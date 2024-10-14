package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterAdminDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RecruiterDetailsDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/change-access/{recruiter_id}")
    public ResponseEntity<?> changeAccess(@PathVariable("recruiter_id") Long recruiterId, @RequestBody boolean access) {
        RecruiterAdminDetailsDTO recruiterAdminDetailsDTO = adminService.changeAccess(recruiterId, access);
        return new ResponseEntity<>(recruiterAdminDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("/recruiters")
    public ResponseEntity<?> findAllRecruiters() {
        List<RecruiterAdminDetailsDTO> recruiterAdminDetailsDTOList = adminService.findAllRecruiters();
        return new ResponseEntity<>(recruiterAdminDetailsDTOList, HttpStatus.OK);
    }
}
