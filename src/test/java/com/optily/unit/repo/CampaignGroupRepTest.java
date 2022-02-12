package com.optily.unit.repo;

import com.optily.api.error.CampaignException;
import com.optily.api.error.EErrorCodes;
import com.optily.domain.model.Campaign;
import com.optily.domain.model.CampaignGroup;
import com.optily.repo.DataProvisioning;
import com.optily.repository.CampaignGroupRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.optily.repo.DataProvisioning.TEST_CAMPAIGN;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CampaignGroupRepTest {

    public static final String TEST_CAMPAIGN_GROUP = "TEST_CAMPAIGN_GROUP";
    @Autowired
    private CampaignGroupRepo campaignGroupRepo;

    @Test
    void addCampaignGroupsThenGetAllCampaignGroups() {
        Campaign campaign = provision();
        List<CampaignGroup> campaigns = campaignGroupRepo.getAllCampaignGroups();

        assertNotNull(campaigns);
        assertEquals(1, campaigns.size());
        assertEquals(campaign, campaigns.get(0).getCampaigns().get(0));
        assertEquals(TEST_CAMPAIGN_GROUP, campaigns.get(0).getName());
        assertNotNull(campaigns.get(0).getId());
       assertNull(campaigns.get(0).getCampaigns().get(0).getRecommendation());
    }

    @Test
    void getCampaignsByGroupThenReturnCampaigns() {
        provision();

        Optional<List<Campaign>> optionalCampaigns = campaignGroupRepo.getCampaignsByGroup(TEST_CAMPAIGN_GROUP);
        assertTrue(optionalCampaigns.isPresent());
        List<Campaign> campaigns = optionalCampaigns.get();
        assertNotNull(campaigns);
        assertEquals(1, campaigns.size());
        assertEquals(TEST_CAMPAIGN, campaigns.get(0).getName());
        assertNotNull(campaigns.get(0).getId());

    }


    @Test
    void getCampaignByNameThenReturnCampaign() {
        Campaign campaign = provision();
        Optional<Campaign> campaignOptional = campaignGroupRepo.getCampaignByName(TEST_CAMPAIGN);
        assertTrue(campaignOptional.isPresent());
        assertEquals(campaign, campaignOptional.get());
        assertEquals(TEST_CAMPAIGN, campaignOptional.get().getName());
    }

    @Test
    void getCampaignGroupByNameThenReturnGroup() {
        provision();

        CampaignGroup cg = campaignGroupRepo.getCampaignGroupByCampaign(TEST_CAMPAIGN);
        assertNotNull(cg);
        assertNotNull(cg.getCampaigns());
        assertNotNull(cg.getId());
        assertNotNull(cg.getOptimization());
        assertEquals(TEST_CAMPAIGN_GROUP, cg.getName());
    }


    @Test
    void getCampaignByNameThenThrowException() {
        CampaignException ex = assertThrows(CampaignException.class, () ->
            campaignGroupRepo.getCampaignGroupByCampaign(TEST_CAMPAIGN)
        );

        assertNotNull(ex);
        assertEquals(EErrorCodes.CAMPAIGN_NOT_FOUND.getCode(), ex.getCode());
    }


    private Campaign provision() {
        Campaign campaign = DataProvisioning.createCampaign(TEST_CAMPAIGN);
        campaignGroupRepo.addCampaignGroups(List.of(campaign),
                TEST_CAMPAIGN_GROUP);
        return campaign;
    }

}
