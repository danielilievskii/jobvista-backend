package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.config.GoogleOAuth2Properties;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.controllers.AuthController;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.Role;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.SignInDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JwtAuthResponse;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RefreshTokenRequest;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.JobSeekerRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.RecruiterRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.repositories.UserRepository;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.AuthService;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.JobSeekerService;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RecruiterRepository recruiterRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JobSeekerService jobSeekerService;
    private final JwtService jwtService;
    private final GoogleOAuth2Properties googleOAuth2Properties;

    @Override
    public User signUpJobSeeker(JobSeeker jobSeeker) {
        jobSeeker.setPassword(passwordEncoder.encode(jobSeeker.getPassword()));
        jobSeeker.setHasAccess(true);
        jobSeeker.setRegisteredOn(LocalDateTime.now());
        return jobSeekerRepository.save(jobSeeker);
    }

    public User signUpRecruiter(Recruiter recruiter) {
        recruiter.setPassword(passwordEncoder.encode(recruiter.getPassword()));
        recruiter.setHasAccess(false);
        recruiter.setRegisteredOn(LocalDateTime.now());
        return recruiterRepository.save(recruiter);
    }

    public JwtAuthResponse signIn(SignInDTO signInDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword()));

        User user = userRepository.findByEmail(signInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Email or password is incorrect"));
        String jwt = jwtService.generateToken(user);
        String refreshJwt = jwtService.generateRefreshToken(new HashMap<>(), user);

        return new JwtAuthResponse(user.getId(), user.getEmail(), user.getName(), user.getRole().name(), user.isHasAccess(), jwt, refreshJwt);
    }

    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            String jwt = jwtService.generateToken(user);

            return new JwtAuthResponse(user.getId(), user.getEmail(), user.getName(), user.getRole().name(), user.isHasAccess(), jwt, refreshTokenRequest.getToken());
        }
        return null;
    }

    @Override
    public JwtAuthResponse googleSignIn(Map<String, String> token) {
        OAuth2AuthenticationToken authentication = getAuthentication(token.get("tokenId"));

        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        JobSeeker jobSeeker = jobSeekerRepository.findByEmail(email)
                .orElseGet(() -> {
                    JobSeeker newJobSeeker = new JobSeeker();
                    newJobSeeker.setEmail(email);
                    newJobSeeker.setFirstName(oAuth2User.getAttribute("given_name"));
                    newJobSeeker.setLastName(oAuth2User.getAttribute("family_name"));
                    newJobSeeker.setPassword("");
                    newJobSeeker.setRole(Role.ROLE_JOBSEEKER);
                    newJobSeeker.setHasAccess(true);
                    jobSeekerRepository.save(newJobSeeker);

                    String googleProfilePicUrl = oAuth2User.getAttribute("picture");
                    submitGoogleProfilePic(newJobSeeker.getId(), googleProfilePicUrl);

                    return newJobSeeker;
                });

        String jwt = jwtService.generateToken(jobSeeker);

        return new JwtAuthResponse(
                jobSeeker.getId(),
                jobSeeker.getEmail(),
                jobSeeker.getFirstName() + " " + jobSeeker.getLastName(),
                jobSeeker.getRole().name(),
                jobSeeker.isHasAccess(),
                jwt,
                null
        );
    }

    public OAuth2AuthenticationToken getAuthentication(String tokenId) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(googleOAuth2Properties.getClientId()))
                    .build();

            GoogleIdToken idToken = verifier.verify(tokenId);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String familyName = Optional.ofNullable((String) payload.get("family_name")).orElse("");
                String givenName = (String) payload.get("given_name");

                Map<String, Object> attributes = Map.of(
                        "sub", userId,
                        "email", email,
                        "email_verified", emailVerified,
                        "name", name,
                        "picture", pictureUrl,
                        "family_name", familyName,
                        "given_name", givenName
                );

                OAuth2User oAuth2User = new DefaultOAuth2User(
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_JOBSEEKER")),
                        attributes,
                        "sub"
                );

                return new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), "google");
            } else {
                throw new IllegalArgumentException("Invalid ID token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify token", e);
        }
    }

    public void submitGoogleProfilePic(Long jobSeekerId, String googleProfilePicUrl) {
        try {
            URL url = new URL(googleProfilePicUrl);
            BufferedImage image = ImageIO.read(url);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            MultipartFile multipartFile = new InMemoryMultipartFile("profilePicFile",
                    "google-profile-pic.jpg", "image/jpeg", imageBytes);

            jobSeekerService.submitProfilePic(jobSeekerId, multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InMemoryMultipartFile implements MultipartFile {

        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            throw new UnsupportedOperationException("This method is not implemented");
        }
    }
}
