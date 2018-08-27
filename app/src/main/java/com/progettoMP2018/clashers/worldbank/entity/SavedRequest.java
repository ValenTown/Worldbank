package com.progettoMP2018.clashers.worldbank.entity;

import java.io.Serializable;

public class SavedRequest implements Serializable {
    String json;
    String topicName;
    String indicatorName;
    String countryName;
    String indicatorId;
    String countryIso2Code;

    public SavedRequest(String json, String topicName, String indicatorName, String countryName, String indicatorId, String countryIso2Code) {
        this.json = json;
        this.topicName = topicName;
        this.indicatorName = indicatorName;
        this.countryName = countryName;
        this.indicatorId = indicatorId;
        this.countryIso2Code = countryIso2Code;
    }

    public String getCountryIso2Code() {
        return countryIso2Code;
    }

    public String getIndicatorId() {
        return indicatorId;
    }

    public String getJson() {
        return json;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setCountryIso2Code(String countryIso2Code) {
        this.countryIso2Code = countryIso2Code;
    }

    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }
}
