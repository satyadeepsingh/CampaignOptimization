package com.optily.repository;

import com.optily.api.error.CampaignException;
import com.optily.api.error.EErrorCodes;
import com.optily.csv.constants.CsvConstants;
import com.optily.domain.constants.EOptimizationStatus;
import com.optily.domain.model.Campaign;
import com.optily.domain.model.CampaignGroup;
import com.optily.domain.model.Optimization;
import com.optily.repository.utils.CampaignUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Repository
public class CampaignGroupRepo {

    private final List<CampaignGroup> campaignGroups = new ArrayList<>();

    public CampaignGroupRepo() {
    }

    public void addCampaignGroups(List<Campaign> campaigns, String campaignGroupName) {

        Optional<CampaignGroup> campaignGroupOp = campaignGroups.stream()
                .filter(cg -> cg.getName().equalsIgnoreCase(campaignGroupName))
                .findAny();
        if(campaignGroupOp.isPresent()) {
            campaignGroupOp.get().getCampaigns().addAll(campaigns);
            return;
        }
        campaignGroups.add(addNewCampaignGroups(campaigns, campaignGroupName));

    }

    private CampaignGroup addNewCampaignGroups(List<Campaign> campaigns,String campaignGroupName) {
        CampaignGroup campaignGroup = new CampaignGroup();
        campaignGroup.setId(CampaignUtils.getCampaignGroupId(campaignGroups.size()));
        campaignGroup.setName(campaignGroupName);
        Optimization optimization = new Optimization();
        optimization.setId(CampaignUtils.getOptimizationId(campaignGroup.getId()));
        optimization.setStatus(EOptimizationStatus.NON_OPTIMIZED);
        campaignGroup.setOptimization(optimization);
        campaignGroup.getCampaigns().addAll(campaigns);
        return campaignGroup;
    }


    public void loadCsvTestData() throws IOException {

            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/campaigns.csv")),
                    StandardCharsets.UTF_8));
                 CSVParser csvParser = new CSVParser(fileReader, CSVFormat.EXCEL.builder()
                         .setHeader(CsvConstants.Headers.NAME,
                                 CsvConstants.Headers.BUDGET,
                                 CsvConstants.Headers.IMPRESSIONS,
                                 CsvConstants.Headers.CAMPAIGN_GROUP_NAME)
                         .build())) {
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecords) {
                    Campaign campaign = new Campaign();
                    campaign.setId(UUID.randomUUID().toString());
                    campaign.setName(csvRecord.get(CsvConstants.Headers.NAME));
                    campaign.setBudget(Double.valueOf(csvRecord.get(CsvConstants.Headers.BUDGET)));
                    campaign.setImpressions(Integer.valueOf(csvRecord.get(CsvConstants.Headers.IMPRESSIONS)));
                    addCampaignGroups(List.of(campaign), csvRecord.get(CsvConstants.Headers.CAMPAIGN_GROUP_NAME));
                }

            }
        }

        public List<CampaignGroup> getAllCampaignGroups() {
            return campaignGroups;
        }

    public Optional<List<Campaign>> getCampaignsByGroup(String campaignGroupName) {

        return campaignGroups.stream()
                .filter(cg -> cg.getName().equalsIgnoreCase(campaignGroupName))
                .map(CampaignGroup::getCampaigns)
                .findFirst();
    }

    public Optional<Campaign> getCampaignsByGroupAndName(String campaignGroupName, String campaignName) {

        return campaignGroups.stream()
                .filter(cg -> cg.getName().equalsIgnoreCase(campaignGroupName))
                .flatMap(cg -> cg.getCampaigns().stream())
                .filter(campaigns -> campaigns.getName().equalsIgnoreCase(campaignName))
                .findFirst();
    }

    public Optional<Campaign> getCampaignByName(String campaignName) {

        return campaignGroups.stream().flatMap(campaignGroup -> campaignGroup.getCampaigns().stream())
                .filter(campaign -> campaign.getName().equalsIgnoreCase(campaignName))
                .findAny();
    }

    public CampaignGroup getCampaignGroupByCampaign(String campaignName) {

        return this.campaignGroups.stream().filter(cg ->
                cg.getCampaigns()
                       .stream()
                       .anyMatch(campaign -> campaign.getName().equalsIgnoreCase(campaignName))
            ).findAny()
                .orElseThrow(() -> new CampaignException(EErrorCodes.CAMPAIGN_NOT_FOUND.getCode(),
                        EErrorCodes.CAMPAIGN_NOT_FOUND.getValue()));
    }
}
