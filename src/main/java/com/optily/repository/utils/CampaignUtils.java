package com.optily.repository.utils;

public class CampaignUtils {


    public static String getCampaignGroupId(int size) {
        return String.valueOf(size + 1);
    }

    public static String getOptimizationId(String id) {
        return "OP_" + id;
    }
}
