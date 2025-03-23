package com.example.campaign.repository;

import com.example.campaign.model.EmeraldWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmeraldWalletRepository extends JpaRepository<EmeraldWallet, UUID> {
}
