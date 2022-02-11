package com.optily.domain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CampaignGroup {

    private String id;
    private String name;
    private final List<Campaign> campaigns = new ArrayList<>();
    private Optimization optimization;

}
