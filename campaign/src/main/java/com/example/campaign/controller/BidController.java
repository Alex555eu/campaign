package com.example.campaign.controller;

import com.example.campaign.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bid")
public class BidController {

    private final CampaignService campaignService;

    @PostMapping("")
    public ResponseEntity<String> redirectToProduct(@RequestParam UUID campaignId) {
        String url = campaignService.bidFromCampaign(campaignId);
        return ResponseEntity.ok(url);
    }


}
