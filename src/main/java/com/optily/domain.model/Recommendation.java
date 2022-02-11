package com.optily.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Recommendation {

    private String id;
    private Double budget;
}
