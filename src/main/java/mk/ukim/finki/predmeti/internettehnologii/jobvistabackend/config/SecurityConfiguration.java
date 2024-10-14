package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.config;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.models.enumerations.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/api/job-advertisements/**",
                                "/api/applications/**",
                                "/api/recruiter/**",
                                "/api/job-seeker/**",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasAnyAuthority(Role.ROLE_ADMIN.name())
                        .requestMatchers("/api/recruiter/{id}/edit-info").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/recruiter/submit-logo").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/job-seeker/{id}/edit-info").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/job-seeker/submit-profile-pic").hasAnyAuthority(Role.ROLE_JOBSEEKER.name())
                        .requestMatchers("/api/job-advertisements/add").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/job-advertisements/edit/{id}").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/job-advertisements/delete/{id}").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/applications/{id}/update").hasAnyAuthority(Role.ROLE_JOBSEEKER.name())
                        .requestMatchers("/api/applications/update").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/uploads/applications/**").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/job-advertisements/{advertisement_id}/applications").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/job-advertisements/{advertisement_id}/applications/filtered").hasAnyAuthority(Role.ROLE_RECRUITER.name())
                        .requestMatchers("/api/applications/submit").hasAnyAuthority(Role.ROLE_JOBSEEKER.name())
                        .requestMatchers("/api/my-applications/{id}").hasAnyAuthority(Role.ROLE_JOBSEEKER.name())
                        .requestMatchers("/api/my-applications/{id}/filtered").hasAnyAuthority(Role.ROLE_JOBSEEKER.name())
                .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/api/auth/google", true)
                )
        ;

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
