package com.example.campaign;

import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.Keyword;
import com.example.campaign.model.Town;
import com.example.campaign.model.User;
import com.example.campaign.repository.EmeraldWalletRepository;
import com.example.campaign.repository.KeywordRepository;
import com.example.campaign.repository.TownRepository;
import com.example.campaign.repository.UserRepository;
import com.example.campaign.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final EmeraldWalletRepository emeraldWalletRepository;
    private final KeywordRepository keywordRepository;
    private final TownRepository townRepository;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
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

        try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/keywords.txt"))) {
            lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(this::loadKeywordToDatabase);
        }

        try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/towns.txt"))) {
            lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(this::loadTownToDatabase);
        }


    }

    private void loadKeywordToDatabase(String value) {
        Keyword keyword = Keyword.builder()
                .keyword(value)
                .build();
        keywordRepository.save(keyword);
    }

    private void loadTownToDatabase(String value) {
        Town town = Town.builder()
                .name(value)
                .build();
        townRepository.save(town);
    }

}
