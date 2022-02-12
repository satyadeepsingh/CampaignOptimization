package com.optily.service;

import com.optily.api.error.CampaignException;
import com.optily.api.error.EErrorCodes;
import com.optily.api.response.CampaignGroupOptimizationResponse;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {

    private final CampaignGroupRepo campaignGroupRepo;
    private final RecommendationService recommendationService;

    public CampaignGroupsResponse getAllCampaignGroups() {
        log.info("getting all campaign groups in DB");
        List<CampaignGroup> campaignGroups =  this.campaignGroupRepo.getAllCampaignGroups();

        CampaignGroupsResponse response = new CampaignGroupsResponse();
        response.setCampaignGroups(campaignGroups.stream()
                .map(CampaignGroup::getName)
                .collect(toList()));
        return response;
    }

    public CampaignResponse getCampaignsForGroup(String campaignGroupName) {

        log.info("getting campaigns of group {}", campaignGroupName);
        Optional<List<Campaign>> campaigns =  this.campaignGroupRepo.getCampaignsByGroup(campaignGroupName);
        CampaignResponse response = new CampaignResponse();
        response.setCampaigns(campaigns);
        return response;

    }

    public void applyOptimization(String campaignGroupName, String campaignName) {

        log.info("optimizing campaign {} in group {}", campaignName, campaignGroupName);
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
        optimizeCampaignGroup(campaign);
    }


    public CampaignRecommendationResponse getCampaignRecommendation(String campaignName) {
        log.info("getting campaign recommendation: {}", campaignName);
        Optional<Campaign> campaignOptional = this.campaignGroupRepo.getCampaignByName(campaignName);
        if(campaignOptional.isEmpty()) throw new CampaignException(EErrorCodes.CAMPAIGN_NOT_FOUND.getCode(), EErrorCodes.CAMPAIGN_NOT_FOUND.getValue());
        Campaign campaign = campaignOptional.get();

        if(campaign.getRecommendation() == null) {
            return null;
        }

        CampaignRecommendationResponse response = new CampaignRecommendationResponse();
        response.setCampaignName(campaignName);
        response.setRecommendedBudget(campaign.getRecommendation().getBudget());
        return response;
    }

    public CampaignGroupOptimizationResponse getCampaignGroupRecommendation(String campaignGroupName) {
        log.info("getting recommendations for campaign group {}", campaignGroupName);
        CampaignGroupOptimizationResponse response = new CampaignGroupOptimizationResponse();
        CampaignGroup campaignGroup =  this.campaignGroupRepo.getAllCampaignGroups().stream()
                .filter(cg -> cg.getName().equalsIgnoreCase(campaignGroupName))
                .findAny()
                .orElseThrow(() ->  new CampaignException(EErrorCodes.CAMPAIGN_GROUP_NOT_FOUND.getCode(), EErrorCodes.CAMPAIGN_GROUP_NOT_FOUND.getValue()));
        response.setRecommendation(campaignGroup.getOptimization().getRecommendation());
        response.setCampaignGroupName(campaignGroupName);
        response.setId(campaignGroup.getId());
        return response;
    }

    private void optimizeCampaignGroup(Campaign campaign) {

        CampaignGroup campaignGroup = this.campaignGroupRepo.getCampaignGroupByCampaign(campaign.getName());
        markOptimized(campaignGroup);

    }

    private void optimizeAllCampaignsOfGroup(CampaignGroup campaignGroup) {
        campaignGroup.getCampaigns().forEach(campaign -> applyOptimization(campaignGroup.getName(), campaign.getName()));
        if(campaignGroup.getOptimization().getStatus() == EOptimizationStatus.OPTIMIZED) return;
        markOptimized(campaignGroup);
    }

    private void markOptimized(CampaignGroup campaignGroup) {
        if(validateAllCampaignsAreOptimized(campaignGroup)) {
            log.info("all campaigns not optimized for campaign group {}", campaignGroup);
            return;
        }
        log.info("marking campaign group {} as optimized", campaignGroup);
        log.info("optimizing campaign group {} as all campaigns are optimized", campaignGroup);
        Optimization optimization = campaignGroup.getOptimization();
        Recommendation recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID().toString());
        recommendation.setBudget(campaignGroup.getCampaigns()
                .stream()
                .mapToDouble(camp -> camp.getRecommendation().getBudget())
                .sum());
        optimization.setRecommendation(recommendation);
        optimization.setStatus(EOptimizationStatus.OPTIMIZED);
    }

    private boolean validateAllCampaignsAreOptimized(CampaignGroup campaignGroup) {
        Optional<Campaign> nonRecommendedCampOp = campaignGroup.getCampaigns().stream()
                .filter(campaign -> campaign.getRecommendation() == null)
                .findAny();
        return nonRecommendedCampOp.isPresent();
    }

    public void applyOptimizationForAllInGroup(String campaignGroupName) {
        log.info("applying optimization for campaigns in group {}", campaignGroupName);
        this.optimizeAllCampaignsOfGroup(this.campaignGroupRepo.getAllCampaignGroups()
                .stream()
                .filter(cg -> cg.getName().equalsIgnoreCase(campaignGroupName))
                .findAny()
                .orElseThrow(() -> new CampaignException(EErrorCodes.CAMPAIGN_NOT_FOUND.getCode(), EErrorCodes.CAMPAIGN_NOT_FOUND.getValue())));
    }

    public CampaignGroupsResponse getUnoptimizedCampaignGroups() {
        CampaignGroupsResponse response = new CampaignGroupsResponse();
        response.setCampaignGroups(this.campaignGroupRepo.getAllCampaignGroups().stream()
                .filter(cg -> cg.getOptimization().getStatus().equals(EOptimizationStatus.NON_OPTIMIZED))
                        .map(CampaignGroup::getName)
                .collect(Collectors.toList()));
        return response;

    }
}
