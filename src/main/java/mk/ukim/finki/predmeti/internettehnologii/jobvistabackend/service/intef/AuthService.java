package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.SignInDTO;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.JwtAuthResponse;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.DTO.RefreshTokenRequest;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.JobSeeker;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.Recruiter;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.users.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;

public interface AuthService {
    User signUpJobSeeker(JobSeeker jobSeeker);
    User signUpRecruiter(Recruiter recruiter);
    JwtAuthResponse signIn(SignInDTO signInDTO);
    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    JwtAuthResponse googleSignIn(Map<String, String> token);
    OAuth2AuthenticationToken getAuthentication(String tokenId);
    void submitGoogleProfilePic(Long jobSeekerId, String googleProfilePicUrl);
}
