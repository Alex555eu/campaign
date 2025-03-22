package com.example.campaign.repository;

import com.example.campaign.model.Token;
import com.example.campaign.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    Optional<Token> findByJwtToken(String token);

    Optional<Token> findByUser(User user);

    @Transactional
    void removeAllByUser(User user);
}
