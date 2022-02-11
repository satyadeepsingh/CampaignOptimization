package com.optily.api.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CampaignException extends RuntimeException {

    private int code;
    private String description;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Error: [");
        sb.append("code=").append(code);
        sb.append(", description='").append(description).append('\'');
        sb.append("]");
        return sb.toString();
    }

}
