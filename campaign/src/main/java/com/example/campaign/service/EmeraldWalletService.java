package com.example.campaign.service;


import com.example.campaign.exception.NegativeBalanceException;
import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.TransactionHistory;
import com.example.campaign.model.TransactionType;
import com.example.campaign.repository.EmeraldWalletRepository;
import com.example.campaign.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EmeraldWalletService {

    private final EmeraldWalletRepository emeraldWalletRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public EmeraldWallet depositEmeralds(EmeraldWallet userWallet, BigDecimal funding) {
        return this.changeWalletBalance(userWallet, funding, TransactionType.DEPOSIT);
    }

    public EmeraldWallet purchaseCampaignWithEmeralds(EmeraldWallet userWallet, BigDecimal campaignFund) {
        this.changeWalletBalance(userWallet, campaignFund.negate(), TransactionType.PURCHASE);
        EmeraldWallet campaignWallet = EmeraldWallet.builder()
                .balance(BigDecimal.ZERO)
                .build();
        return this.changeWalletBalance(campaignWallet, campaignFund, TransactionType.DEPOSIT);
    }

    public EmeraldWallet addFundingToCampaign(EmeraldWallet userWallet, EmeraldWallet campaignWallet, BigDecimal funding) {
        this.changeWalletBalance(userWallet, funding.negate(), TransactionType.PURCHASE);
        return this.changeWalletBalance(campaignWallet, funding, TransactionType.DEPOSIT);
    }

    public EmeraldWallet subtractFundingFromCampaign(EmeraldWallet userWallet, EmeraldWallet campaignWallet, BigDecimal funding) {
        this.changeWalletBalance(userWallet, funding, TransactionType.REFUND);
        return this.changeWalletBalance(campaignWallet, funding.negate(), TransactionType.REFUND);
    }

    public EmeraldWallet refundCampaign(EmeraldWallet userWallet, EmeraldWallet campaignWallet) {
        BigDecimal campaignWalletBalance = campaignWallet.getBalance();
        this.changeWalletBalance(campaignWallet, campaignWalletBalance.negate(), TransactionType.REFUND);
        return this.changeWalletBalance(userWallet, campaignWalletBalance, TransactionType.REFUND);
    }

    public EmeraldWallet bidFromCampaign(EmeraldWallet campaignWallet, BigDecimal bidAmount) {
        return this.changeWalletBalance(campaignWallet, bidAmount.negate(), TransactionType.BID);
    }

    private EmeraldWallet changeWalletBalance(EmeraldWallet emeraldWallet, BigDecimal value, TransactionType type) {
        BigDecimal currentBalance = emeraldWallet.getBalance();
        BigDecimal newBalance = currentBalance.add(value);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new NegativeBalanceException("Not enough funds to complete this operation");

        emeraldWallet.setBalance(newBalance);
        emeraldWalletRepository.save(emeraldWallet);

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .emeraldWallet(emeraldWallet)
                .amount(value)
                .transactionType(type)
                .stamp(LocalDateTime.now())
                .build();
        transactionHistoryRepository.save(transactionHistory);

        return emeraldWallet;
    }

}
