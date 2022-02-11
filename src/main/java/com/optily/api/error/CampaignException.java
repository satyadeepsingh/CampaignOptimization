package com.optily.api.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
@NoArgsConstructor
public class CampaignException extends RuntimeException {

    private int code;
    private String description;


}
