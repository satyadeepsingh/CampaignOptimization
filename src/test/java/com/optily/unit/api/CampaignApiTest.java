package com.optily.unit.api;

import com.optily.api.response.CampaignGroupOptimizationResponse;
import com.optily.api.response.CampaignGroupsResponse;
import com.optily.api.response.CampaignRecommendationResponse;
import com.optily.api.response.CampaignResponse;
import com.optily.domain.model.CampaignGroup;
import com.optily.domain.model.Recommendation;
import com.optily.repo.DataProvisioning;
import com.optily.service.CampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.optily.api.constants.ApiConstants.*;
import static com.optily.repo.DataProvisioning.TEST_CAMPAIGN_C1G1;
import static com.optily.repo.DataProvisioning.TEST_CAMP_GROUP_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampaignApiTest {

    @MockBean
    private CampaignService campaignService;
    @Autowired
    private TestRestTemplate restTemplate;

    private List<CampaignGroup> campaignGroups;

    @BeforeEach
    void setUp() {
        campaignGroups = DataProvisioning.createCampaignsAndGroups();
    }

    @Test
    void getAllCampaignGroupsThenReturnResponse() throws URISyntaxException {

        CampaignGroupsResponse campaignGroupsResponse = new CampaignGroupsResponse();
        campaignGroupsResponse.setCampaignGroups(campaignGroups.stream()
                .map(CampaignGroup::getName)
                .collect(Collectors.toList()));

        when(campaignService.getAllCampaignGroups()).thenReturn(campaignGroupsResponse);

        URI getURI = new URI(CAMPAIGN_GROUP_API);

        ResponseEntity<CampaignGroupsResponse> response = this.restTemplate.getForEntity(getURI, CampaignGroupsResponse.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CampaignGroupsResponse responseBody = response.getBody();
        assertEquals(campaignGroupsResponse, responseBody);
    }

    @Test
    void getCampaignAndGroupsThenReturnResponse() throws URISyntaxException {

        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaigns(Optional.of(campaignGroups.get(0).getCampaigns()));

        when(campaignService.getCampaignsForGroup(TEST_CAMP_GROUP_1)).thenReturn(campaignResponse);

        StringBuilder sb = new StringBuilder();
        sb.append(CAMPAIGN_GROUP_API);
        sb.append(CAMPAIGN_API);
        sb.append("?");
        sb.append(CAMPAIGN_GROUP_NAME_PARAM);
        sb.append("=");
        sb.append(TEST_CAMP_GROUP_1);

        URI getURI = new URI(sb.toString());
        ResponseEntity<CampaignResponse> response = this.restTemplate.getForEntity(getURI, CampaignResponse.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(campaignResponse, response.getBody());

    }

    @Test
    void getCampaignRecommendedBudgetThenReturnResponse() throws URISyntaxException {
        CampaignRecommendationResponse optimizationResponse = new CampaignRecommendationResponse();
        optimizationResponse.setCampaignName(TEST_CAMPAIGN_C1G1);
        optimizationResponse.setRecommendedBudget(100.00D);

        when(campaignService.getCampaignRecommendation(anyString())).thenReturn(optimizationResponse);

        StringBuilder sb = new StringBuilder();
        sb.append(CAMPAIGN_GROUP_API);
        sb.append(CAMPAIGN_API);
        sb.append(RECOMMENDATIONS_API);
        sb.append("?");
        sb.append(CAMPAIGN_NAME_PARAM);
        sb.append("=");
        sb.append(TEST_CAMPAIGN_C1G1);

        URI getURI = new URI(sb.toString());
        ResponseEntity<CampaignRecommendationResponse> response = this.restTemplate.getForEntity(getURI, CampaignRecommendationResponse.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(optimizationResponse, response.getBody());


    }

    @Test
    void getCampaignGroupRecommendedBudgetThenReturnResponse() throws URISyntaxException {
        CampaignGroupOptimizationResponse optimizationResponse = new CampaignGroupOptimizationResponse();
        optimizationResponse.setCampaignGroupName(TEST_CAMP_GROUP_1);
        Recommendation recommendation = new Recommendation();
        recommendation.setId(UUID.randomUUID().toString());
        recommendation.setBudget(200.00D);
        optimizationResponse.setRecommendation(recommendation);
        when(campaignService.getCampaignGroupRecommendation(anyString())).thenReturn(optimizationResponse);

        StringBuilder sb = new StringBuilder();
        sb.append(CAMPAIGN_GROUP_API);
        sb.append(OPTIMIZATION_API);
        sb.append("?");
        sb.append(CAMPAIGN_GROUP_NAME_PARAM);
        sb.append("=");
        sb.append(TEST_CAMP_GROUP_1);

        URI getURI = new URI(sb.toString());
        ResponseEntity<CampaignGroupOptimizationResponse> response = this.restTemplate.getForEntity(getURI, CampaignGroupOptimizationResponse.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(optimizationResponse, response.getBody());


    }

    @Test
    void viewUnoptimizedCampaignGroupsThenReturnResponse() throws URISyntaxException {
        CampaignGroupsResponse groupsResponse = new CampaignGroupsResponse();

        groupsResponse.setCampaignGroups(campaignGroups.stream()
                .map(CampaignGroup::getName)
                .collect(Collectors.toList()));

        when(campaignService.getUnoptimizedCampaignGroups()).thenReturn(groupsResponse);

        StringBuilder sb = new StringBuilder();
        sb.append(CAMPAIGN_GROUP_API);
        sb.append(RECOMMENDATIONS_API);
        
        URI getURI = new URI(sb.toString());
        ResponseEntity<CampaignGroupsResponse> response = this.restTemplate.getForEntity(getURI, CampaignGroupsResponse.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(groupsResponse, response.getBody());


    }
}
