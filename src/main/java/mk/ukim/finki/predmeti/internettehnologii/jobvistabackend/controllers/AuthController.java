package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers;

import lombok.AllArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.*;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers.JobSeekerMapper;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.mappers.RecruiterMapper;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping("/signup/job-seeker")
    public ResponseEntity<?> signUpJobSeeker(@RequestBody JobSeekerDTO jobSeekerDTO) {
        JobSeeker jobSeeker = JobSeekerMapper.mapToJobSeeker(jobSeekerDTO);
        authenticationService.signUpJobSeeker(jobSeeker);
        return new ResponseEntity<>(jobSeeker, HttpStatus.CREATED);
    }

    @PostMapping("/signup/recruiter")
    public ResponseEntity<?> signUpRecruiter(@RequestBody RecruiterDTO recruiterDTO) {
        Recruiter recruiter = RecruiterMapper.mapToRecruiter(recruiterDTO);
        authenticationService.signUpRecruiter(recruiter);
        return new ResponseEntity<>(recruiter, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignInDTO signInDTO) {
        return ResponseEntity.ok(authenticationService.signIn(signInDTO));
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleSignIn(@RequestBody Map<String, String> token) {
        return ResponseEntity.ok(authenticationService.googleSignIn(token));
    }
}
