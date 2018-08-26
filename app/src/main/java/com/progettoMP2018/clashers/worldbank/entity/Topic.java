package com.progettoMP2018.clashers.worldbank.entity;

import java.io.Serializable;

public class Topic implements Serializable {
    String id;
    String value;
    String sourceNote;

    public String getId() {
        return id;
    }

    public String getSourceNote() {
        return sourceNote;
    }

    public String getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
