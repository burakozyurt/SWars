package com.app.selfiewars.selfiewars;

public class Wildcards {
    private Integer doubleDipValue;
    private Integer fiftyFiftyValue;
    private Integer healthValue;

    public Wildcards() {

    }

    public Wildcards(Integer doubleDipValue, Integer fiftyFiftyValue, Integer healthValue) {
        this.doubleDipValue = doubleDipValue;
        this.fiftyFiftyValue = fiftyFiftyValue;
        this.healthValue = healthValue;
    }

    public Integer getDoubleDipValue() {
        return doubleDipValue;
    }

    public void setDoubleDipValue(Integer doubleDipValue) {
        this.doubleDipValue = doubleDipValue;
    }

    public Integer getFiftyFiftyValue() {
        return fiftyFiftyValue;
    }

    public void setFiftyFiftyValue(Integer fiftyFiftyValue) {
        this.fiftyFiftyValue = fiftyFiftyValue;
    }

    public Integer getHealthValue() {
        return healthValue;
    }

    public void setHealthValue(Integer healthValue) {
        this.healthValue = healthValue;
    }
}
