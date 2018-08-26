package com.progettoMP2018.clashers.worldbank.entity;

public class IndicatorGraph {
    private String id;
    private String value;

    public IndicatorGraph(String id, String value) {
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
