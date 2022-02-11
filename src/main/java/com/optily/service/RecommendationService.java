package com.optily.service;

import com.optily.domain.model.Campaign;
import com.optily.domain.model.Recommendation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {


    public void applyOptimizationAndRecommendBudget(List<Campaign> campaigns, Campaign campaign) {
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
