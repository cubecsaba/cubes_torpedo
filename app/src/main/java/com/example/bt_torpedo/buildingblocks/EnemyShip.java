package com.example.bt_torpedo.buildingblocks;

import static com.example.bt_torpedo.game.TorpedoActivity.ee;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyFleet;


public class EnemyShip extends HomeShip {


    public boolean allowElement(Element element) {

        if (InSide(element) && !Used(enemyFleet, ee))
            return true; //   if inside and not yet taken as a shot

        else return false;
    } // checks if inside


}
