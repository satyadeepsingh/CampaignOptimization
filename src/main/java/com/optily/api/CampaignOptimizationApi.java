package com.optily.api;

import com.optily.api.response.CampaignGroupOptimizationResponse;
import com.optily.api.response.CampaignGroupsResponse;
import com.optily.api.response.CampaignRecommendationResponse;
import com.optily.api.response.CampaignResponse;
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
        return ResponseEntity.ok(campaignService.getCampaignsForGroup(campaignGroupName));
    }

    @GetMapping(CAMPAIGN_API + RECOMMENDATIONS_API)
    public ResponseEntity<CampaignRecommendationResponse> getCampaignRecommendedBudget(@RequestParam(value= CAMPAIGN_NAME_PARAM)String campaignName) {
        return ResponseEntity.ok(campaignService.getCampaignRecommendation(campaignName));
    }

    @GetMapping(OPTIMIZATION_API)
    public ResponseEntity<CampaignGroupOptimizationResponse> getCampaignGroupRecommendedBudget(@RequestParam(value= CAMPAIGN_GROUP_NAME_PARAM)String campaignGroupName) {
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

    @GetMapping(RECOMMENDATIONS_API)
    public ResponseEntity<CampaignGroupsResponse> viewUnoptimizedCampaignGroups() {
        return ResponseEntity.ok(this.campaignService.getUnoptimizedCampaignGroups());
    }

}
