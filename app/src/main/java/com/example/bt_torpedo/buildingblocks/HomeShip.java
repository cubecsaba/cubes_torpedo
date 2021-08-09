package com.example.bt_torpedo.buildingblocks;

import java.util.ArrayList;
import java.util.List;

import static com.example.bt_torpedo.game.TorpedoActivity.ShipSize;
import static com.example.bt_torpedo.game.TorpedoActivity.homeFleet;
import static com.example.bt_torpedo.game.TorpedoActivity.eh;
import static com.example.bt_torpedo.game.TorpedoActivity.homeShip;


public class HomeShip {

    public List<Element> elements;
    private Boolean sunk;

    public HomeShip() {
        elements = new ArrayList<>();
        sunk = false;
    } // constructor 1

    public HomeShip(List<Element> elements) {
        this.elements = elements;
        sunk = false;
    } // constructor 2

    public boolean allowElement(Element element) {
        boolean repeated = false;

        if (elements.size() == 0 && (ShipSize != -1)) {
            // if first element of a ship and inside then checks if neighbours or matches an element of other ships
            if (InSide(eh)) {
                for (int i = 0; i < homeFleet.getFleet().size(); i++) {
                    for (int j = 0; j < homeFleet.getFleet().get(i).elements.size(); j++) {
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() // total match
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex()) {
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex()
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() + 1) { // one below
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex()
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() - 1) { // one above
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex()
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() + 1) { // one to the right
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex()
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() - 1) { // one to the left
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() + 1
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() + 1
                                && (homeFleet.getFleet().get(i).elements.get(j).getxIndex() != 0
                                && homeFleet.getFleet().get(i).elements.get(j).getyIndex() != 0)) { // right below except for upper left corner
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() + 1
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() - 1) { // right above
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() - 1
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() - 1) { // left above
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() + 1
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() - 1) { // left below
                            return false;
                        }
                    }
                }
                return true;
            } else return false;
            // allowed if fits into shipSize, neighbours and inside of the court
        } else if (elements.size() < ShipSize && neighbour(eh)) {

            if (InSide(eh) ) {
                for (int i = 0; i < homeFleet.getFleet().size(); i++) {
                    for (int j = 0; j < homeFleet.getFleet().get(i).elements.size(); j++) {
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() // total match
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex()) {
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex()
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() + 1) { // one below
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex()
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() - 1) { // one above
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex()
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() + 1) { // one to the right
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex()
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() - 1) { // one to the left
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() + 1
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() + 1 &&
                                homeFleet.getFleet().get(i).elements.get(j).getxIndex() != 0 &&
                                homeFleet.getFleet().get(i).elements.get(j).getyIndex() != 0
                        ) { // right below except for 1,1
                            return false;
                        }
                        if (eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() + 1
                                && eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() - 1) { // right above
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() - 1
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() - 1) { // left above
                            return false;
                        }
                        if (eh.getyIndex() == homeFleet.getFleet().get(i).elements.get(j).getyIndex() + 1
                                && eh.getxIndex() == homeFleet.getFleet().get(i).elements.get(j).getxIndex() - 1) { // left below
                            return false;
                        }
                    }
                }

                for (int i = 0; i < homeShip.elements.size(); i++) {        // check if this element was already part of the ship
                    if (homeShip.elements.get(i).getxIndex() == eh.getxIndex() &&
                            homeShip.elements.get(i).getyIndex() == eh.getyIndex()) {
                        repeated = true;
                    }
                }
                if (!repeated) {
                    return true;
                } // returns true if inside and on allowed field (and not repeated)
            } else return false;
        }
        return false;
    } // checks if neighbours or matches an element of other ships

    public void addElement(Element e) {
        elements.add(e);
    }

    public boolean neighbour(Element e) {

        for (int i = 0; i < elements.size(); i++) {
            if (((elements.get(i).getxIndex() - 1 == e.getxIndex()) && (elements.get(i).getyIndex() == e.getyIndex()))
                    || ((elements.get(i).getxIndex() + 1 == e.getxIndex()) && (elements.get(i).getyIndex() == e.getyIndex()))
                    || ((elements.get(i).getyIndex() - 1 == e.getyIndex()) && (elements.get(i).getxIndex() == e.getxIndex()))
                    || ((elements.get(i).getyIndex() + 1 == e.getyIndex()) && (elements.get(i).getxIndex() == e.getxIndex()))
            ) {
                return true;
            }
        }
        return false;
    } // checks if given element neighbours of an existing element of the same ship

    public Boolean getSunk() {
        return sunk;
    }

    public void setSunk(Boolean sunk) {
        this.sunk = sunk;
    }

    public boolean InSide(Element e) {

        if (e.getxIndex() == 0 || e.getxIndex() == 11 || e.getyIndex() == 0 || e.getyIndex() == 11) {
            return false;
        } else return true;
    } // checks if inside court

    public boolean Used(HomeFleet eList, Element ee) { // check if the element is already taken as an element in a ship
        boolean used = false;
        if (eList.getFleet() != null) {
            for (int j = 0; j < eList.getFleet().size(); j++) {
                if (eList.getFleet().get(j) != null && eList.getFleet().get(j).elements != null) { //if ship exists and has elements
                    for (int i = 0; i < eList.getFleet().get(j).elements.size(); i++) {
                        if (j == 0) { //first ship of the fleet is a dummy, no ne for check

                        } else {
                            if (eList.getFleet().get(j).elements.get(i).getxIndex() == ee.getxIndex() &&
                                    eList.getFleet().get(j).elements.get(i).getyIndex() == ee.getyIndex()) {
                                used = true;
                            }
                        }
                    }
                }
            }
        }
        if (used) {
            return true;
        } else return false;
    }

}
