package com.app.selfiewars.selfiewars;

public class StoreDiamond {

    String diamondValueNumber;
    String diamondPrice;
    int diamondImage;

    public StoreDiamond() {
    }
    public StoreDiamond(String diamondValueNumber, String diamondPrice, int diamondImage) {
        this.diamondValueNumber = diamondValueNumber;
        this.diamondPrice = diamondPrice;
        this.diamondImage = diamondImage;
    }

    public String getDiamondValueNumber() {
        return diamondValueNumber;
    }

    public void setDiamondValueNumber(String diamondValueNumber) {
        this.diamondValueNumber = diamondValueNumber;
    }

    public String getDiamondPrice() {
        return diamondPrice;
    }

    public void setDiamondPrice(String diamondPrice) {
        this.diamondPrice = diamondPrice;
    }


    public int getDiamondImage() {
        return diamondImage;
    }

    public void setDiamondImage(int diamondImage) {
        this.diamondImage = diamondImage;
    }
}
