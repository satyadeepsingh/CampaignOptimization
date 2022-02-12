package com.optily.repo;

import com.optily.domain.model.Campaign;

import java.util.UUID;

public class DataProvisioning {



    public static final String TEST_CAMPAIGN = "TEST_CAMPAIGN";

    public static Campaign createCampaign() {
        Campaign campaign = new Campaign();
        campaign.setBudget(100.00D);
        campaign.setName(TEST_CAMPAIGN);
        campaign.setImpressions(1000);
        campaign.setId(UUID.randomUUID().toString());
        return campaign;
    }
}
