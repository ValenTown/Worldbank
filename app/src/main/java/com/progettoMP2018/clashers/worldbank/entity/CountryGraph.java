package com.progettoMP2018.clashers.worldbank.entity;

public class CountryGraph {
    private String id;
    private String value;

    public CountryGraph(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }
}
