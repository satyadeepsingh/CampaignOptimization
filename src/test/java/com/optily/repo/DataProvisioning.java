package com.optily.repo;

import com.optily.domain.constants.EOptimizationStatus;
import com.optily.domain.model.Campaign;
import com.optily.domain.model.CampaignGroup;
import com.optily.domain.model.Optimization;

import java.util.List;
import java.util.UUID;

public class DataProvisioning {



    public static final String TEST_CAMPAIGN = "TEST_CAMPAIGN";
    public static final String TEST_CAMPAIGN_C1G1 = "TEST_CAMP1_G1";
    public static final String TEST_CAMPAIGN_C2G1 = "TEST_CAMP1_G1";
    public static final String TEST_CAMPAIGN_C1G2 = "TEST_CAMP1_G2";
    public static final String TEST_CAMP_GROUP_1 = "TEST_CAMP_GROUP_1";
    public static final String TEST_CAMP_GROUP_2 = "TEST_CAMP_GROUP_2";


    public static Campaign createCampaign(String name) {
        Campaign campaign = new Campaign();
        campaign.setBudget(100.00D);
        campaign.setName(name);
        campaign.setImpressions(1000);
        campaign.setId(UUID.randomUUID().toString());
        return campaign;
    }

    public static List<CampaignGroup> createCampaignsAndGroups() {

        Campaign c1g1 = createCampaign(TEST_CAMPAIGN_C1G1);
        Campaign c2g1 = createCampaign(TEST_CAMPAIGN_C2G1);
        Campaign c1g2 = createCampaign(TEST_CAMPAIGN_C1G2);

        CampaignGroup cg1 = new CampaignGroup();
        cg1.setId(UUID.randomUUID().toString());
        cg1.setName(TEST_CAMP_GROUP_1);
        cg1.getCampaigns().add(c1g1);
        cg1.getCampaigns().add(c2g1);
        cg1.setOptimization(createOptimization());

        CampaignGroup cg2 = new CampaignGroup();
        cg2.setId(UUID.randomUUID().toString());
        cg2.setName(TEST_CAMP_GROUP_2);
        cg2.getCampaigns().add(c1g2);
        cg2.setOptimization(createOptimization());

        return List.of(cg1, cg2);
    }

    public static Optimization createOptimization() {

        Optimization optimization = new Optimization();
        optimization.setId(UUID.randomUUID().toString());
        optimization.setStatus(EOptimizationStatus.NON_OPTIMIZED);

        return optimization;
    }
}
