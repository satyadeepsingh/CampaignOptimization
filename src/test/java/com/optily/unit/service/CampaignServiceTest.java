package com.optily.unit.service;

import com.optily.api.response.CampaignGroupsResponse;
import com.optily.api.response.CampaignResponse;
import com.optily.domain.constants.EOptimizationStatus;
import com.optily.domain.model.Campaign;
import com.optily.domain.model.CampaignGroup;
import com.optily.domain.model.Recommendation;
import com.optily.repo.DataProvisioning;
import com.optily.repository.CampaignGroupRepo;
import com.optily.service.CampaignService;
import com.optily.service.RecommendationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.optily.repo.DataProvisioning.*;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CampaignServiceTest {

    @MockBean
    private CampaignGroupRepo campaignGroupRepo;

    @Autowired
    private RecommendationService recommendationService;

    @InjectMocks
    @Autowired
    private CampaignService campaignService;

    private static List<CampaignGroup> campaignGroups;

    @BeforeAll
    static void setUp() {
        campaignGroups = DataProvisioning.createCampaignsAndGroups();
    }

    @Test
    void getAllCampaignGroupsThenReturnCampaignGroupResponse() {

        when(campaignGroupRepo.getAllCampaignGroups()).thenReturn(campaignGroups);

        CampaignGroupsResponse response = campaignService.getAllCampaignGroups();

        assertNotNull(response);
        assertNotNull(response.getCampaignGroups());
        assertFalse(response.getCampaignGroups().isEmpty());
        assertEquals(2, response.getCampaignGroups().size());
        assertEquals(TEST_CAMP_GROUP_1,response.getCampaignGroups().get(0));
        assertEquals(TEST_CAMP_GROUP_2,response.getCampaignGroups().get(1));

    }

    @Test
    void getCampaignsForGroupThenReturnCampaignResponse() {

        Optional<List<Campaign>> optionalCampaigns = Optional.of(campaignGroups.get(0).getCampaigns());

        when(campaignGroupRepo.getCampaignsByGroup(anyString())).thenReturn(optionalCampaigns);

        CampaignResponse response = campaignService.getCampaignsForGroup(TEST_CAMP_GROUP_1);
        assertNotNull(response);
        assertTrue(response.getCampaigns().isPresent());
        List<Campaign> campaigns = response.getCampaigns().get();
        assertFalse(campaigns.isEmpty());
        assertEquals(2, campaigns.size());
        assertEquals(TEST_CAMPAIGN_C1G1, campaigns.get(0).getName());
        assertEquals(TEST_CAMPAIGN_C2G1, campaigns.get(1).getName());
    }

    @Test
    void testApplyOptimizationOneCampaignThenCheckCampaignRecommendation() {

        Optional<List<Campaign>> campaignsInGroupOp = Optional.of(campaignGroups.get(0)
                .getCampaigns());

        when(campaignGroupRepo.getCampaignsByGroup(TEST_CAMP_GROUP_1))
                .thenReturn(campaignsInGroupOp);
        when(campaignGroupRepo.getCampaignGroupByCampaign(anyString())).thenReturn(campaignGroups.get(0));
        campaignService.applyOptimization(TEST_CAMP_GROUP_1, TEST_CAMPAIGN_C1G1);
        Campaign c1g1 = campaignsInGroupOp.get().get(0);

        assertNotNull(c1g1.getRecommendation());
        Recommendation recommendation = c1g1.getRecommendation();
        assertNotNull(recommendation.getId());
        assertEquals(100.0D, recommendation.getBudget());
        assertNull(campaignGroups.get(0).getOptimization().getRecommendation());
        assertEquals(EOptimizationStatus.NON_OPTIMIZED, campaignGroups.get(0).getOptimization().getStatus());
    }

}
