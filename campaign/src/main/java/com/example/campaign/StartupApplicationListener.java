package com.example.campaign;

import com.example.campaign.model.User;
import com.example.campaign.repository.UserRepository;
import com.example.campaign.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {

        User user = User.builder()
                .emailAddress("user@email.com")
                .firstName("user")
                .password(passwordEncoder.encode("user"))
                .build();
        userRepository.save(user);
    }

}
