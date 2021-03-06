package com.optily.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.management.loading.MLetContent;

@Getter
@AllArgsConstructor
public enum EErrorCodes {

    CAMPAIGN_NOT_FOUND(1001, "CAMPAIGN DOES NOT EXIST in DB"),
    CAMPAIGN_GROUP_NOT_FOUND(1002, "CAMPAIGN GROUP DOES NOT EXIST in DB"),
    CAMPAIGN_ALREADY_OPTIMIZED(1003, "CAMPAIGN IS ALREADY OPTIMIZED"),
    CAMPAIGN_ALREADY_ADDED(1004, "CAMPAIGN IS ALREADY ADDED IN DB");

    private int code;
    private String value;
}
