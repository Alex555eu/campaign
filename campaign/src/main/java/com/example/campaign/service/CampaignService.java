package com.example.campaign.service;

import com.example.campaign.dto.PostCampaignRequest;
import com.example.campaign.dto.PutCampaignRequest;
import com.example.campaign.exception.EntityNotFoundException;
import com.example.campaign.model.*;
import com.example.campaign.repository.CampaignRepository;
import com.example.campaign.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CampaignService {

    private final UserService userService;
    private final TownService townService;
    private final KeywordService keywordService;
    private final EmeraldWalletService emeraldWalletService;

    private final CampaignRepository campaignRepository;

    public List<Campaign> getAllUserCampaigns() {
        User user = userService.getUserFromSecurityContext();
        return campaignRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public Campaign postCampaign(PostCampaignRequest request) {
        User user = userService.getUserFromSecurityContext();
        Optional<Town> town = townService.getTownById(request.townId());
        List<Keyword> keywords = keywordService.getAllKeywordsByIds(request.keywordIds());

        if (town.isPresent() && !keywords.isEmpty()) {
            EmeraldWallet campaignWallet =
                    emeraldWalletService.purchaseCampaignWithEmeralds(
                            user.getEmeraldWallet(),
                            request.campaignFund()
                    );
            Campaign campaign = Campaign.builder()
                    .campaignName(request.campaignName())
                    .bidAmount(request.bidAmount())
                    .campaignFund(request.campaignFund())
                    .status(request.status())
                    .radius(request.radius())
                    .keywords(keywords)
                    .productUrl(request.productUrl())
                    .town(town.get())
                    .user(user)
                    .emeraldWallet(campaignWallet)
                    .build();
            campaignRepository.save(campaign);

            return campaign;
        }
        throw new EntityNotFoundException("Requested data doesn't exist");
    }

    @Transactional
    public Campaign putCampaign(PutCampaignRequest request) {
        User user = userService.getUserFromSecurityContext();
        Optional<Town> townOpt = townService.getTownById(request.townId());
        List<Keyword> keywords = keywordService.getAllKeywordsByIds(request.keywordIds());
        Optional<Campaign> campaignOpt = campaignRepository.findById(request.campaignId());

        if (campaignOpt.isPresent() &&
                townOpt.isPresent() &&
                !keywords.isEmpty() &&
                campaignOpt.get().getUser().getId().equals(user.getId()) // if requested campaign belongs to the user
        ) {
            Campaign campaign = campaignOpt.get();

            if (request.bidAmount().compareTo(BigDecimal.ONE) < 0)
                throw new IllegalArgumentException("Min amount for bid is: 1");

            BigDecimal fundingAddOrSubtract = request.campaignFund().subtract(campaign.getCampaignFund());
            BigDecimal funding = fundingAddOrSubtract.abs();

            if (fundingAddOrSubtract.compareTo(BigDecimal.ZERO) > 0) {
                emeraldWalletService.addFundingToCampaign(user.getEmeraldWallet(), campaign.getEmeraldWallet(), funding);

            } else if (fundingAddOrSubtract.compareTo(BigDecimal.ZERO) < 0) {
                emeraldWalletService.subtractFundingFromCampaign(user.getEmeraldWallet(), campaign.getEmeraldWallet(), funding);
            }

            campaign.setCampaignName(request.campaignName());
            campaign.setRadius(request.radius());
            campaign.setKeywords(keywords);
            campaign.setTown(townOpt.get());
            campaign.setStatus(request.status());
            campaign.setBidAmount(request.bidAmount());
            campaign.setCampaignFund(request.campaignFund());
            campaign.setProductUrl(request.productUrl());

            campaignRepository.save(campaign);

            return campaign;
        }
        throw new EntityNotFoundException("Requested data doesn't exist");
    }

    @Transactional
    public void deleteCampaign(UUID campaignId) {
        User user = userService.getUserFromSecurityContext();
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (campaignOpt.isPresent() &&
            campaignOpt.get().getUser().getId().equals(user.getId())
        ) {
            Campaign campaign = campaignOpt.get();
            emeraldWalletService.refundCampaign(user.getEmeraldWallet(), campaign.getEmeraldWallet());
            emeraldWalletService.deleteEmeraldWalletRecords(campaign.getEmeraldWallet());
            campaign.getKeywords().clear();
            campaignRepository.delete(campaign);
            return;
        }
        throw new EntityNotFoundException("Data to be deleted not found");
    }

    public String bidFromCampaign(UUID campaignId) {
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (campaignOpt.isPresent() &&
                campaignOpt.get().getStatus()
        ) {
            Campaign campaign = campaignOpt.get();

            emeraldWalletService.bidFromCampaign(campaign.getEmeraldWallet(), campaign.getBidAmount());

            return campaign.getProductUrl();
        }
        throw new EntityNotFoundException("Campaign offline");
    }


}
