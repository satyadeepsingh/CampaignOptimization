package com.optily.unit.api;

import com.optily.api.response.CampaignGroupsResponse;
import com.optily.domain.model.CampaignGroup;
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
import java.util.stream.Collectors;

import static com.optily.api.constants.ApiConstants.CAMPAIGN_GROUP_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
