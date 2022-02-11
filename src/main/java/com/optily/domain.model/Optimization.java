package com.optily.domain.model;

import com.optily.domain.constants.EOptimizationStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Optimization {

    private String id;
    private EOptimizationStatus status;
    private Recommendation recommendation;
}
