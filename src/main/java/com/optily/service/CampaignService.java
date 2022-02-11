package com.optily.service;

import com.optily.api.error.CampaignException;
import com.optily.api.error.EErrorCodes;
import com.optily.api.response.CampaignGroupsResponse;
import com.optily.api.response.CampaignRecommendationResponse;
import com.optily.api.response.CampaignResponse;
import com.optily.domain.constants.EOptimizationStatus;
import com.optily.domain.model.Campaign;
import com.optily.domain.model.CampaignGroup;
import com.optily.domain.model.Optimization;
import com.optily.domain.model.Recommendation;
import com.optily.repository.CampaignGroupRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {

    private final CampaignGroupRepo campaignGroupRepo;
    private final RecommendationService recommendationService;

    public CampaignGroupsResponse getAllCampaignGroups() {
        List<CampaignGroup> campaignGroups =  this.campaignGroupRepo.getAllCampaignGroups();

        CampaignGroupsResponse response = new CampaignGroupsResponse();
        response.setCampaignGroups(campaignGroups.stream()
                .map(CampaignGroup::getName)
                .collect(toList()));
        return response;
    }

    public CampaignResponse getCampaignsAndService(String campaignGroupName) {

        Optional<List<Campaign>> campaigns =  this.campaignGroupRepo.getCampaignsByGroup(campaignGroupName);
        CampaignResponse response = new CampaignResponse();
        response.setCampaigns(campaigns);
        return response;

    }

    public void applyOptimization(String campaignGroupName, String campaignName) {

        Optional<List<Campaign>> campaignsInGroup =  this.campaignGroupRepo.getCampaignsByGroup(campaignGroupName);

        if(campaignsInGroup.isEmpty()) {
            throw new CampaignException(EErrorCodes.CAMPAIGN_GROUP_NOT_FOUND.getCode(), EErrorCodes.CAMPAIGN_GROUP_NOT_FOUND.getValue());
        }

        List<Campaign> campaigns = campaignsInGroup.get();

        Optional<Campaign> campaignOp =  campaigns.stream()
                .filter(campaign -> campaign.getName().equalsIgnoreCase(campaignName))
                .findAny();

        if(campaignOp.isEmpty()) {
            throw new CampaignException(EErrorCodes.CAMPAIGN_NOT_FOUND.getCode(), EErrorCodes.CAMPAIGN_NOT_FOUND.getValue());
        }

        Campaign campaign = campaignOp.get();
        if(Objects.nonNull(campaign.getRecommendation())) {
            throw new CampaignException(EErrorCodes.CAMPAIGN_ALREADY_OPTIMIZED.getCode(), EErrorCodes.CAMPAIGN_ALREADY_OPTIMIZED.getValue());
        }

        recommendationService.applyOptimizationAndRecommendBudget(campaigns, campaign);

    }


    public CampaignRecommendationResponse getCampaignRecommendation(String campaignName) {

        Optional<Campaign> campaignOptional = this.campaignGroupRepo.getCampaignByName(campaignName);
        if(campaignOptional.isEmpty()) throw new CampaignException(EErrorCodes.CAMPAIGN_NOT_FOUND.getCode(), EErrorCodes.CAMPAIGN_NOT_FOUND.getValue());
        Campaign campaign = campaignOptional.get();

        if(campaign.getRecommendation() == null) {
            return null;
        }

        CampaignRecommendationResponse response = new CampaignRecommendationResponse();
        response.setCampaignName(campaignName);
        response.setRecommendedBudget(campaign.getRecommendation().getBudget());
        optimizeCampaignGroup(campaign);
        return response;
    }

    private void optimizeCampaignGroup(Campaign campaign) {

        CampaignGroup campaignGroup = this.campaignGroupRepo.getCampaignGroupByName(campaign.getName());
        markOptimized(campaignGroup);

    }

    public void optimizeAllCampaignsOfGroup(CampaignGroup campaignGroup) {
        campaignGroup.getCampaigns().forEach(campaign -> applyOptimization(campaignGroup.getName(), campaign.getName()));
        if(campaignGroup.getOptimization().getStatus() == EOptimizationStatus.OPTIMIZED) return;
        markOptimized(campaignGroup);
    }

    private void markOptimized(CampaignGroup campaignGroup) {
        if(validateAllCampaignsAreOptimized(campaignGroup)) return;
        Optimization optimization = campaignGroup.getOptimization();
        Recommendation recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID().toString());
        recommendation.setBudget(campaignGroup.getCampaigns()
                .stream()
                .mapToDouble(camp -> camp.getRecommendation().getBudget())
                .sum());
        optimization.setStatus(EOptimizationStatus.OPTIMIZED);
    }

    private boolean validateAllCampaignsAreOptimized(CampaignGroup campaignGroup) {
        Optional<Campaign> nonRecommendedCampOp = campaignGroup.getCampaigns().stream()
                .filter(campaign -> campaign.getRecommendation() == null)
                .findAny();
        return nonRecommendedCampOp.isPresent();
    }
}
