package com.example.campaign.service;


import com.example.campaign.dto.AuthenticationRequest;
import com.example.campaign.dto.AuthenticationResponse;
import com.example.campaign.dto.PostRefreshTokenRequest;
import com.example.campaign.dto.RegisterRequest;
import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.Token;
import com.example.campaign.model.User;
import com.example.campaign.repository.EmeraldWalletRepository;
import com.example.campaign.repository.TokenRepository;
import com.example.campaign.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final EmeraldWalletRepository emeraldWalletRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmailAddress(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = "Bearer " + jwtService.generateToken(new HashMap<>(), user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        EmeraldWallet emeraldWallet = EmeraldWallet.builder()
                .balance(new BigDecimal("500.0"))
                .build();
        emeraldWalletRepository.save(emeraldWallet);

        User user = User.builder()
                .firstName(request.firstName())
                .emailAddress(request.emailAddress())
                .password(passwordEncoder.encode(request.password()))
                .emeraldWallet(emeraldWallet)
                .build();
        userRepository.save(user);

        String jwtToken = "Bearer " + jwtService.generateToken(new HashMap<>(), user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(
            PostRefreshTokenRequest request
    ) {
        final String refreshToken = request.refreshToken();
        String userEmail = null;
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (ExpiredJwtException e) {
            log.error("Expired jwt exception");
        }

        if (userEmail != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            Optional<Token> persistedToken = tokenRepository.findByUser((User) userDetails);
            if (jwtService.isTokenValid(refreshToken, userDetails) && persistedToken.isPresent() && persistedToken.get().isValid()) {
                revokeAllUserTokens((User) userDetails);
                String jwtToken = "Bearer " + jwtService.generateToken(new HashMap<>(), userDetails);
                String newRefreshToken = jwtService.generateRefreshToken(userDetails);
                saveUserToken((User) userDetails, jwtToken);
                AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                        .token(jwtToken)
                        .refreshToken(newRefreshToken)
                        .build();
                return ResponseEntity.ok(authenticationResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        String stripped = jwtToken.substring(7);
        Token token = Token.builder()
                .user(user)
                .jwtToken(stripped)
                .isValid(true)
                .tokenType("Bearer")
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.removeAllByUser(user);
    }
}