package com.example.campaign.controller;

import com.example.campaign.dto.PostCampaignRequest;
import com.example.campaign.dto.PutCampaignRequest;
import com.example.campaign.model.Campaign;
import com.example.campaign.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/campaign")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("")
    public ResponseEntity<List<Campaign>> getAllUserCampaigns() {
        List<Campaign> campaigns = campaignService.getAllUserCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("")
    public ResponseEntity<Campaign> postCampaign(@RequestBody PostCampaignRequest request) {
        Campaign campaign = campaignService.postCampaign(request);
        return ResponseEntity.ok(campaign);
    }

    @PutMapping("")
    public ResponseEntity<Campaign> putCampaign(@RequestBody PutCampaignRequest request) {
        Campaign campaign = campaignService.putCampaign(request);
        return ResponseEntity.ok(campaign);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteCampaign(@RequestParam UUID campaignId) {
        campaignService.deleteCampaign(campaignId);
        return ResponseEntity.ok().build();
    }

}
