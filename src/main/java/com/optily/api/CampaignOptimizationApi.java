package com.optily.api;

import com.optily.domain.model.CampaignGroup;
import com.optily.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.optily.api.constants.ApiConstants.CAMPAIGN_API;

@RestController
@RequestMapping(CAMPAIGN_API)
@RequiredArgsConstructor
@Slf4j
public class CampaignOptimizationApi {

    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<List<CampaignGroup>> getAllCampaignsAndGroups() {
        return ResponseEntity.ok(campaignService.getAllCampaignsAndGroups());
    }


}
