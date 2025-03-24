package com.example.campaign;

import com.example.campaign.model.*;
import com.example.campaign.repository.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final EmeraldWalletRepository emeraldWalletRepository;
    private final KeywordRepository keywordRepository;
    private final TownRepository townRepository;
    private final CampaignRepository campaignRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

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

        Path filePath = Paths.get(getClass().getClassLoader().getResource("keywords.txt").toURI());
        try (Stream<String> lines = Files.lines(filePath)) {
            lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(this::loadKeywordToDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path filePath2 = Paths.get(getClass().getClassLoader().getResource("towns.txt").toURI());
        try (Stream<String> lines = Files.lines(filePath2)) {
            lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(this::loadTownToDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Keyword keyword = Keyword.builder()
                .keyword("elegance")
                .build();
        keywordRepository.save(keyword);

        Town town = Town.builder()
                .name("Olkusz")
                .build();
        townRepository.save(town);

        EmeraldWallet emeraldWallet2 = EmeraldWallet.builder()
                .balance(new BigDecimal("100"))
                .build();
        emeraldWalletRepository.save(emeraldWallet2);

        Campaign campaign = Campaign.builder()
                .status(true)
                .radius(5)
                .campaignFund(new BigDecimal("100"))
                .campaignName("My Awesome Campaign for Google.com")
                .keywords(List.of(keyword))
                .productUrl("https://google.com")
                .bidAmount(new BigDecimal("1"))
                .emeraldWallet(emeraldWallet2)
                .user(user)
                .town(town)
                .build();
        campaignRepository.save(campaign);

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .stamp(LocalDateTime.now())
                .transactionType(TransactionType.PURCHASE)
                .amount(new BigDecimal("100"))
                .emeraldWallet(emeraldWallet)
                .build();
        transactionHistoryRepository.save(transactionHistory);

        TransactionHistory transactionHistory2 = TransactionHistory.builder()
                .stamp(LocalDateTime.now())
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("100"))
                .emeraldWallet(emeraldWallet2)
                .build();
        transactionHistoryRepository.save(transactionHistory2);
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
