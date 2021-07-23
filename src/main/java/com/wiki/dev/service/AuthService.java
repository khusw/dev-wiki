package com.wiki.dev.service;

import com.wiki.dev.dto.AuthenticationResponse;
import com.wiki.dev.dto.LoginRequest;
import com.wiki.dev.dto.RefreshTokenRequest;
import com.wiki.dev.dto.RegisterRequest;
import com.wiki.dev.dto.NotificationEmail;
import com.wiki.dev.entity.User;
import com.wiki.dev.entity.VerificationToken;
import com.wiki.dev.exception.DevWikiException;
import com.wiki.dev.repository.UserRepository;
import com.wiki.dev.repository.VerificationTokenRepository;
import com.wiki.dev.security.JwtProvider;
import com.wiki.dev.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        int endIdx = email.indexOf("@");
        String defaultUsername= email.substring(0, endIdx);

        User user = new User();
        user.setUsername(defaultUsername);
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setEmail(email);
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        String link = Constants.ACTIVATION_EMAIL + "/" + token;
        String message = mailContentBuilder.build(link);

        mailService.sendMail(new NotificationEmail("계정 활성화를 진행해주세요.", email, message));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationTokenOptional.orElseThrow(() -> new DevWikiException("Invalid Token")));
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DevWikiException("user not found with : " + email));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authenticationToken = jwtProvider.generateToken(authentication);

        return AuthenticationResponse.builder()
                .authenticationToken(authenticationToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .email(loginRequest.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<Object, Object>> getCurrentUser() {
        Map<Object, Object> responseBody = new HashMap<>();

        org.springframework.security.core.userdetails.User principal
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = userRepository.findByEmail(principal.getUsername());

        if (optionalUser.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "user not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        responseBody.put("data", optionalUser.get());
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .email(refreshTokenRequest.getEmail())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
