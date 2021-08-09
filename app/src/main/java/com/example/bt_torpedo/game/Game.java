package com.example.bt_torpedo.game;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import static com.example.bt_torpedo.game.TorpedoActivity.semaphore;
import static com.example.bt_torpedo.game.MyHandler.a; // X index of an enemy shoot
import static com.example.bt_torpedo.game.MyHandler.b; // Y index of an enemy shoot


public class Game extends Thread {

    private int aOld, bOld;
    static final int SEMAPHORE_OWN = 1;

    public Game() {
        aOld = -1;
        bOld = -1;
        a = 100;
        b = 100;
    }

    @Override
    public void run() {

        Handler gHandler = new GameHandler(Looper.getMainLooper()); //create a GameHandler to communicate with the main? thread

        while (true) {
            if (!semaphore && ((aOld != a) || (bOld != b))) { //if enemy is active and the enemy has shot (change in indexes)
                Message message = Message.obtain();
                message.what = SEMAPHORE_OWN; // sends a message to the main thread to activate playground
                gHandler.sendMessage(message);
            }
            aOld = a; // updates old values
            bOld = b;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
