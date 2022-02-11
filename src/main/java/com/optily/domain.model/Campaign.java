package com.optily.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Campaign {

    private String id;
    private String name;
    private Double budget;
    private Integer impressions;
    private Double revenue;

    @JsonIgnore
    private Recommendation recommendation;
}
