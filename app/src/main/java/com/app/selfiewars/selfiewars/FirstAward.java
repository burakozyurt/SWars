package com.app.selfiewars.selfiewars;

public class FirstAward {
    private Integer doubleDipValue;
    private Integer fiftyFiftyValue;
    private Integer healthValue;
    private Integer diamondValue;

    public FirstAward() {
    }

    public FirstAward(Integer doubleDipValue, Integer fiftyFiftyValue, Integer healthValue, Integer diamondValue) {
        this.doubleDipValue = doubleDipValue;
        this.fiftyFiftyValue = fiftyFiftyValue;
        this.healthValue = healthValue;
        this.diamondValue = diamondValue;
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

    public Integer getDiamondValue() {
        return diamondValue;
    }

    public void setDiamondValue(Integer diamondValue) {
        this.diamondValue = diamondValue;
    }
}
