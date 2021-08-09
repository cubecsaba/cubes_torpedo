package com.example.bt_torpedo.buildingblocks;

import java.util.ArrayList;
import java.util.List;

public class HomeFleet {

    private List<HomeShip> Fleet;
    private int FourShip;
    private int ThreeShip;
    private int TwoShip;
    private int OneShip;
    public static int NrOfSunkenShips;

    public HomeFleet() {
        Fleet = new ArrayList<>();
        FourShip = 0;
        ThreeShip = 0;
        TwoShip = 0;
        OneShip = 0;
        NrOfSunkenShips = 0;
    } // contructor

    public List<HomeShip> getFleet() {
        return Fleet;
    }

    public int getFourShip() {
        return FourShip;
    }

    public void setFourShip(int fourShip) {
        FourShip = fourShip;
    }

    public int getThreeShip() {
        return ThreeShip;
    }

    public void setThreeShip(int threeShip) {
        ThreeShip = threeShip;
    }

    public int getTwoShip() {
        return TwoShip;
    }

    public void setTwoShip(int twoShip) {
        TwoShip = twoShip;
    }

    public int getOneShip() {
        return OneShip;
    }

    public void setOneShip(int oneShip) {
        OneShip = oneShip;
    }

    public void addShip(HomeShip hS) {
        Fleet.add(hS);
    }

}
