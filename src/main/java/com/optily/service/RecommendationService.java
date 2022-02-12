package com.optily.service;

import com.optily.domain.model.Campaign;
import com.optily.domain.model.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RecommendationService {


    public void applyOptimizationAndRecommendBudget(List<Campaign> campaigns, Campaign campaign) {
        log.info("applying recommendation for campaign");
        int sumImpressions = campaigns.stream()
                .mapToInt(Campaign::getImpressions)
                .sum();
        double sumBudget = campaigns.stream()
                .mapToDouble(Campaign::getBudget)
                .sum();

        Recommendation recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID().toString());
        recommendation.setBudget(((double) campaign.getImpressions() / sumImpressions) * sumBudget);
        campaign.setRecommendation(recommendation);
    }

}
