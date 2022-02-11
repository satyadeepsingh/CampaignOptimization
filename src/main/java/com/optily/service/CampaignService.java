package com.optily.service;

import com.optily.domain.model.CampaignGroup;
import com.optily.repository.CampaignGroupRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {

    private final CampaignGroupRepo campaignGroupRepo;

    public List<CampaignGroup> getAllCampaignsAndGroups() {
        return this.campaignGroupRepo.getAllCampaignGroups();
    }
}
