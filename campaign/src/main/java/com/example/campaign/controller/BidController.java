package com.example.campaign.controller;

import com.example.campaign.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bid")
public class BidController {

    private final CampaignService campaignService;

    @PutMapping("")
    public ResponseEntity<Void> redirectToProduct(@RequestParam UUID campaignId) {
        String url = campaignService.bidFromCampaign(campaignId);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .location(URI.create(url))
                .build();
    }


}
