package com.optily.api.response;

import com.optily.domain.model.Campaign;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Data
@ToString
public class CampaignResponse {

    private Optional<List<Campaign>> campaigns;
}
