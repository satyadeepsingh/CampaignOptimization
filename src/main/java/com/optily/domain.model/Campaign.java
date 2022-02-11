package com.optily.domain.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Campaign {

    private String id;
    private String name;
    private Double budget;
    private Integer impressions;
    private Double revenue;
    private Recommendation recommendation;
}
