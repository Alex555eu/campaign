package com.example.campaign.service;


import com.example.campaign.exception.NegativeBalanceException;
import com.example.campaign.model.EmeraldWallet;
import com.example.campaign.model.TransactionHistory;
import com.example.campaign.model.TransactionType;
import com.example.campaign.repository.EmeraldWalletRepository;
import com.example.campaign.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmeraldWalletService {

    private final EmeraldWalletRepository emeraldWalletRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional
    public EmeraldWallet depositEmeralds(EmeraldWallet userWallet, BigDecimal funding) {
        return this.changeWalletBalance(userWallet, funding, TransactionType.DEPOSIT);
    }

    @Transactional
    public EmeraldWallet purchaseCampaignWithEmeralds(EmeraldWallet userWallet, BigDecimal campaignFund) {
        this.changeWalletBalance(userWallet, campaignFund.negate(), TransactionType.PURCHASE);
        EmeraldWallet campaignWallet = EmeraldWallet.builder()
                .balance(BigDecimal.ZERO)
                .build();
        return this.changeWalletBalance(campaignWallet, campaignFund, TransactionType.DEPOSIT);
    }

    @Transactional
    public EmeraldWallet addFundingToCampaign(EmeraldWallet userWallet, EmeraldWallet campaignWallet, BigDecimal funding) {
        this.changeWalletBalance(userWallet, funding.negate(), TransactionType.PURCHASE);
        return this.changeWalletBalance(campaignWallet, funding, TransactionType.DEPOSIT);
    }

    @Transactional
    public EmeraldWallet subtractFundingFromCampaign(EmeraldWallet userWallet, EmeraldWallet campaignWallet, BigDecimal funding) {
        this.changeWalletBalance(userWallet, funding, TransactionType.REFUND);
        return this.changeWalletBalance(campaignWallet, funding.negate(), TransactionType.REFUND);
    }

    @Transactional
    public EmeraldWallet refundCampaign(EmeraldWallet userWallet, EmeraldWallet campaignWallet) {
        BigDecimal campaignWalletBalance = campaignWallet.getBalance();
        this.changeWalletBalance(campaignWallet, campaignWalletBalance.negate(), TransactionType.REFUND);
        return this.changeWalletBalance(userWallet, campaignWalletBalance, TransactionType.REFUND);
    }

    @Transactional
    public EmeraldWallet bidFromCampaign(EmeraldWallet campaignWallet, BigDecimal bidAmount) {
        return this.changeWalletBalance(campaignWallet, bidAmount.negate(), TransactionType.BID);
    }

    @Transactional
    public void deleteEmeraldWalletRecords(EmeraldWallet emeraldWallet) {
        List<TransactionHistory> list = transactionHistoryRepository.findAllByEmeraldWalletId(emeraldWallet.getId());
        transactionHistoryRepository.deleteAll(list);
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
