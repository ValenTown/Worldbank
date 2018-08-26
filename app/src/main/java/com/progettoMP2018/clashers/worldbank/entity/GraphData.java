package com.progettoMP2018.clashers.worldbank.entity;

public class GraphData {
    private IndicatorGraph indicator;
    private CountryGraph country;
    private String countryiso3code;
    private String date;
    private float value;
    private String unit;
    private String obs_status;
    private int decimal;

    public GraphData(String date, float value) {
        this.date = date;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public CountryGraph getCountry() {
        return country;
    }

    public IndicatorGraph getIndicator() {
        return indicator;
    }

    public int getDecimal() {
        return decimal;
    }

    public String getCountryiso3code() {
        return countryiso3code;
    }

    public String getObs_status() {
        return obs_status;
    }

    public String getUnit() {
        return unit;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setIndicator(IndicatorGraph indicator) {
        this.indicator = indicator;
    }

    public void setCountry(CountryGraph country) {
        this.country = country;
    }

    public void setCountryiso3code(String countryiso3code) {
        this.countryiso3code = countryiso3code;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public void setObs_status(String obs_status) {
        this.obs_status = obs_status;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

