package com.progettoMP2018.clashers.worldbank.entity;

import java.io.Serializable;

public class FullQuery implements Serializable {
    private Topic topic;
    private Country country;
    private Indicator indicator;


    public FullQuery(Country country, Topic topic, Indicator indicator) {
        this.country = country;
        this.topic = topic;
        this.indicator = indicator;

    }

    public FullQuery() {
        this.country = new Country();
        this.topic = new Topic();
        this.indicator = new Indicator();
    }

    public Topic getTopic() {
        return topic;
    }

    public Country getCountry() {
        return country;
    }

    public Indicator getIndicator() {
        return indicator;
    }


    public void setCountry(Country country) {
        this.country = country;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}
