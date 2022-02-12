package com.optily.api.response;

import com.optily.domain.model.Recommendation;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CampaignGroupOptimizationResponse {

    private String id;
    private String campaignGroupName;
    private Recommendation recommendation;
}
