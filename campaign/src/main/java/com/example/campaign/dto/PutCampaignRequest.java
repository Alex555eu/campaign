package com.example.campaign.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PutCampaignRequest(
        UUID campaignId,
        String campaignName,
        List<UUID> keywordIds,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        String productUrl,
        Boolean status,
        UUID townId,
        Integer radius
) {
}
