package com.optily.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@ToString
public class CampaignGroupsResponse {

    List<String> campaignGroups;
    
}
