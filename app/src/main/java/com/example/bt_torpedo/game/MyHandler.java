package com.example.bt_torpedo.game;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.bt_torpedo.buildingblocks.Element;

import static com.example.bt_torpedo.entry.BT_Activity.STATE_MESSAGE_RECEIVED;
import static com.example.bt_torpedo.entry.StartActivity.sendReceive;
import static com.example.bt_torpedo.entry.BT_Activity.sharedPrefs;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyFleet;
import static com.example.bt_torpedo.game.TorpedoActivity.homeFleet;
import static com.example.bt_torpedo.game.TorpedoActivity.paintE;
import static com.example.bt_torpedo.game.TorpedoActivity.vibr;
import static com.example.bt_torpedo.game.TorpedoActivity.explosion;
import static com.example.bt_torpedo.game.TorpedoActivity.boathorn;
import static com.example.bt_torpedo.buildingblocks.HomeFleet.NrOfSunkenShips;
import static com.example.bt_torpedo.game.TorpedoActivity.build;
import static com.example.bt_torpedo.game.TorpedoActivity.ad;


public class MyHandler extends Handler {
    public static int a, b;
    public static float MAX, MAY;
    public static boolean startGame;
    private Element e;
    private int SunkenShip;
    private int SumSunkenShip;

    // constructor
    public MyHandler(@NonNull Looper looper) {
        super(looper);
        SumSunkenShip = 0;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {

            case STATE_MESSAGE_RECEIVED:
                byte[] readBuff = (byte[]) msg.obj;
                String tempMsg = new String(readBuff, 0, msg.arg1);

                // if the message is "start" (enemy has placed his ships), then the game starts
                if (tempMsg.equals("start")) {
                    startGame = true;
                    // start the bistabil multivibrator
                    TorpedoActivity.Game.start();
                    if (!sharedPrefs.getBoolean("silent", false)) {
                        boathorn.start();
                    }
                }
                // in case this player has hit it must represent it in its EnemyView by replacing a square by an X
                else if (tempMsg.equals("hit")) {
                    e = enemyFleet.getFleet().get(enemyFleet.getFleet().size() - 1).elements.get(enemyFleet.getFleet().get(enemyFleet.getFleet().size() - 1).elements.size() - 1);
                    e.setHit(true);
                    enemyFleet.getFleet().get(enemyFleet.getFleet().size() - 1).elements.set(enemyFleet.getFleet().get(enemyFleet.getFleet().size() - 1).elements.size() - 1, e);


                    vibr.vibrate(250); //vibrate
                    // short break to avoid vibration conjunction
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!sharedPrefs.getBoolean("silent", false)) {
                        explosion.start();
                    }

                }
                //-- If message stats with " ShipIsDown": decompose message, get the sunken ships' elements' indexes out of it and set corresponding ship in enemyFleet (consisiting of the same elements) as sunken
                else if (tempMsg.startsWith("ShipIsDown")) {
                    String[] parts = tempMsg.split("x");
                    for (int u = 0; u < (parts.length - 1); u = u + 2) { // first member is "ShipIsDown", it is not required, others are needed in pairs

                        for (int v = 0; v < enemyFleet.getFleet().size(); v++) {

                            if ((enemyFleet.getFleet().get(v) != null) && (enemyFleet.getFleet().get(v).elements != null)) {
                                {
                                    // nothing to do as the first enemy ship is a dummy one, or because that ship has already marked as sunken
                                    if (v == 0 || enemyFleet.getFleet().get(v).getSunk() == true) {

                                    }
                                    // first member of the message is "ShipIsDown", it is not required, but all others to be checked
                                    else if (enemyFleet.getFleet().get(v).elements.get(0).getxIndex() == Integer.parseInt(parts[u + 1]) &&
                                            enemyFleet.getFleet().get(v).elements.get(0).getyIndex() == Integer.parseInt(parts[u + 2])) {
                                        enemyFleet.getFleet().get(v).setSunk(true);    // set the corresponding enemy ship "sunken"
                                        v = enemyFleet.getFleet().size();
                                    }
                                }
                            }
                        }
                    }

                    if (!sharedPrefs.getBoolean("silent", false)) {
                        explosion.start();
                    }
                    SumSunkenShip++; // increase counter of NrSunkenShips

                    // Show Dialog if it was the last ship to sink
                    if (SumSunkenShip == 10) {
                        build.setText("Game\nover");
                        ad.show();
                        SumSunkenShip = 0;
                    }
                    //**
                }


                // either the screen size is transmitted or coordinates from the enemy
                else {
                    String[] parts = tempMsg.split("x");
                    if (parts.length > 2) {
                        MAX = Float.parseFloat(parts[0]);
                        MAY = Float.parseFloat(parts[1]);

                    } else {
                        a = Integer.parseInt(parts[0]);
                        b = Integer.parseInt(parts[1]);

                        // checking if an enemy shot (with coordinates a,b) has hit an own ship, if yes, send a "hit" message to the enemy and mark the element in HomeFleetShip as hit
                        if (homeFleet.getFleet() != null) {
                            for (int j = 0; j < homeFleet.getFleet().size(); j++) {
                                if (homeFleet.getFleet().get(j) != null && homeFleet.getFleet().get(j).elements != null) {
                                    for (int i = 0; i < homeFleet.getFleet().get(j).elements.size(); i++) {
                                        //first ship of the fleet is a dummy, no ned to check
                                        if (j == 0) {

                                        }
                                        //all other ships are to be checked
                                        else {
                                            // if the enemy shot an element, its colour changes to painE to display as hit
                                            if (homeFleet.getFleet().get(j).elements.get(i).getxIndex() == a &&
                                                    homeFleet.getFleet().get(j).elements.get(i).getyIndex() == b) {
                                                homeFleet.getFleet().get(j).elements.get(i).setPaint(paintE);
                                                homeFleet.getFleet().get(j).elements.get(i).setHit(true); // store as hit
                                                vibr.vibrate(500); //vibrate
                                                // send message to the opponent in case there was a hit
                                                String string2 = "hit";
                                                byte[] bytes2 = string2.getBytes();
                                                sendReceive.write(bytes2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // short break to avoid message conjunction
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // send the sunken ship's element's indexes to the opponent if the last shot has sunken a ship
                        if (ShipIsDown()) {
                            int sunkenShipSize = homeFleet.getFleet().get(SunkenShip).elements.size();
                            String SunkenShipIndexes = "x";

                            // take the indexes of the ship elements (from the first to the last-1) and convert them to the string to be sent to the opponent
                            for (int i = 0; i < sunkenShipSize - 1; i++) {
                                SunkenShipIndexes = SunkenShipIndexes
                                        + homeFleet.getFleet().get(SunkenShip).elements.get(i).getxIndex()
                                        + "x" + homeFleet.getFleet().get(SunkenShip).elements.get(i).getyIndex() + "x";
                            }
                            // without "x" at the end for the last index
                            SunkenShipIndexes = SunkenShipIndexes
                                    + homeFleet.getFleet().get(SunkenShip).elements.get(sunkenShipSize - 1).getxIndex()
                                    + "x" + homeFleet.getFleet().get(SunkenShip).elements.get(sunkenShipSize - 1).getyIndex();

                            // send message to the other phone with the sunken ship's element's indexes
                            String string3 = "ShipIsDown" + SunkenShipIndexes;
                            byte[] bytes3 = string3.getBytes();
                            sendReceive.write(bytes3);

                            try { // short break to avoid message conjunction
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            NrOfSunkenShips++;

                            //--start Dialog if it was the last ship to be sunk
                            if (NrOfSunkenShips == 10) {
                                build.setText("Game\nover");
                                ad.show(); // show dialog NrOfSunkenShip has just been increased to the maximal number of ships.
                                SumSunkenShip = 0;
                            }
                            //**
                        }
                    }
                }
                break;
        }
    }

    // checking if an enemy shot (with coordinates a,b) has hit an own ship's last helathy element, if yes, send a "ShipDown" message to the enemy
    private boolean ShipIsDown() {
        boolean ShipDown = false;
        int hitElements = 0;
        // checking if an enemy shot (with coordinates a,b) has hit an own ship's last helathy element, if yes, send a "ShipDown" message to the enemy
        if (homeFleet.getFleet() != null) {
            for (int j = 0; j < homeFleet.getFleet().size(); j++) {
                //if ship exists, not yet sunk and has elements
                if (homeFleet.getFleet().get(j) != null && !homeFleet.getFleet().get(j).getSunk() && homeFleet.getFleet().get(j).elements != null) {
                    for (int i = 0; i < homeFleet.getFleet().get(j).elements.size(); i++) {
                        //first ship of the fleet is a dummy, no ne for check
                        if (j == 0) {
                        }
                        //all other ships have to be checked
                        else {
                            // increase hit elements if that element was already hit
                            if (homeFleet.getFleet().get(j).elements.get(i).isHit()) {
                                hitElements++;
                            }
                            // or if the element is hit now
                            else if ((homeFleet.getFleet().get(j).elements.get(i).getxIndex() == a &&
                                    homeFleet.getFleet().get(j).elements.get(i).getyIndex() == b)) {
                                hitElements++;
                            }
                            // if all elements of a ship are hit
                            if (homeFleet.getFleet().get(j).elements.size() == hitElements) {
                                homeFleet.getFleet().get(j).setSunk(true); // store it as sunk
                                SunkenShip = j; // store the just sunken ship's index
                                ShipDown = true; // return true, saying that the ship has been sunk
                            }
                        }
                    }
                    hitElements = 0; //otherwise reset counter to 0
                }
            }
        }
        if (ShipDown) {
            return true;
        } else return false;
    }

}
