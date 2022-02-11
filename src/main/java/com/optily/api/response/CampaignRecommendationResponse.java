package com.optily.api.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CampaignRecommendationResponse {

    private String campaignName;
    private Double recommendedBudget;
}
