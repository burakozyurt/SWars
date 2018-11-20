package com.app.selfiewars.selfiewars;

public class StoreJoker {
    String jokerValueNumber;
    String jokerPrice;
    int jokerImage;
    int jokerid;

    public StoreJoker() {
    }

    public StoreJoker(String jokerValueNumber, String jokerPrice, int jokerImage, int jokerid) {
        this.jokerValueNumber = jokerValueNumber;
        this.jokerPrice = jokerPrice;
        this.jokerImage = jokerImage;
        this.jokerid = jokerid;
    }

    public String getJokerValueNumber() {
        return jokerValueNumber;
    }

    public void setJokerValueNumber(String jokerValueNumber) {
        this.jokerValueNumber = jokerValueNumber;
    }

    public String getJokerPrice() {
        return jokerPrice;
    }

    public void setJokerPrice(String jokerPrice) {
        this.jokerPrice = jokerPrice;
    }

    public int getJokerImage() {
        return jokerImage;
    }

    public void setJokerImage(int jokerImage) {
        this.jokerImage = jokerImage;
    }

    public int getJokerid() {
        return jokerid;
    }

    public void setJokerid(int jokerid) {
        this.jokerid = jokerid;
    }
}
