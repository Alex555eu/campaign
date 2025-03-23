package com.example.campaign;

import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.User;
import com.example.campaign.repository.EmeraldWalletRepository;
import com.example.campaign.repository.UserRepository;
import com.example.campaign.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final EmeraldWalletRepository emeraldWalletRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {

        EmeraldWallet emeraldWallet = EmeraldWallet.builder()
                .balance(new BigDecimal("500.0"))
                .build();
        emeraldWalletRepository.save(emeraldWallet);

        User user = User.builder()
                .emailAddress("user@email.com")
                .firstName("user")
                .password(passwordEncoder.encode("user"))
                .emeraldWallet(emeraldWallet)
                .build();
        userRepository.save(user);
    }

}
