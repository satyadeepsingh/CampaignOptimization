package com.optily.api;

import com.optily.api.response.CampaignGroupsResponse;
import com.optily.api.response.CampaignRecommendationResponse;
import com.optily.api.response.CampaignResponse;
import com.optily.domain.model.CampaignGroup;
import com.optily.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.optily.api.constants.ApiConstants.*;

@RestController
@RequestMapping(CAMPAIGN_GROUP_API)
@RequiredArgsConstructor
@Slf4j
public class CampaignOptimizationApi {

    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<CampaignGroupsResponse> getAllCampaignGroups() {
        return ResponseEntity.ok(campaignService.getAllCampaignGroups());
    }

    @GetMapping(CAMPAIGN_API)
    public ResponseEntity<CampaignResponse> getCampaignsAndGroups(@RequestParam(value= CAMPAIGN_GROUP_NAME_PARAM)String campaignGroupName) {
        return ResponseEntity.ok(campaignService.getCampaignsAndService(campaignGroupName));
    }

    @GetMapping(CAMPAIGN_API + RECOMMEDATION_API)
    public ResponseEntity<CampaignRecommendationResponse> getCampaignRecommendedBudget(@RequestParam(value= CAMPAIGN_NAME_PARAM)String campaignName) {
        return ResponseEntity.ok(campaignService.getCampaignRecommendation(campaignName));
    }

    @GetMapping(RECOMMEDATION_API)
    public ResponseEntity<CampaignGroup> getCampaignGroupRecommendedBudget(@RequestParam(value= CAMPAIGN_GROUP_NAME_PARAM)String campaignGroupName) {
        return ResponseEntity.ok(campaignService.getCampaignGroupRecommendation(campaignGroupName));
    }

    @PatchMapping(CAMPAIGN_API)
    public void applyOptimization(@RequestParam(value= CAMPAIGN_GROUP_NAME_PARAM)String campaignGroupName,
                                  @RequestParam(value= CAMPAIGN_NAME_PARAM)String campaignName) {

        this.campaignService.applyOptimization(campaignGroupName, campaignName);
    }

    @PutMapping(CAMPAIGN_API)
    public void applyOptimizationForAllCampaigns(@RequestParam(value= CAMPAIGN_GROUP_NAME_PARAM)String campaignGroupName) {

        this.campaignService.applyOptimizationForAllInGroup(campaignGroupName);
    }

}
