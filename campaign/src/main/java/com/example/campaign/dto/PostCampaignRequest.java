package com.example.campaign.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record PostCampaignRequest(
        String campaignName,
        Set<UUID> keywordIds,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        String productUrl,
        Boolean status,
        UUID townId,
        Integer radius
) {
}
