package com.example.bt_torpedo.game;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.bt_torpedo.R;
import com.example.bt_torpedo.buildingblocks.Element;

import static com.example.bt_torpedo.game.TorpedoActivity.semaphore;
import static com.example.bt_torpedo.game.Game.SEMAPHORE_OWN;
import static com.example.bt_torpedo.game.TorpedoActivity.tvE;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyShot;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyShots;
import static com.example.bt_torpedo.game.TorpedoActivity.paintE;
import static com.example.bt_torpedo.game.TorpedoActivity.build;
import static com.example.bt_torpedo.game.MyHandler.a; // X index of an enemy shoot
import static com.example.bt_torpedo.game.MyHandler.b; // Y index of an enemy shoot
import static com.example.bt_torpedo.game.TorpedoActivity.start;

public class GameHandler extends Handler {

    private Element e;

    public GameHandler(@NonNull Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {

            case SEMAPHORE_OWN:

                if (start) { // block will be executed if the home fleet is already complete
                    semaphore = true; //activates playground
                    tvE.setBackgroundResource(R.drawable.torpedo_active);
                    build.setText("Shoot");
                    e = new Element(a, b, paintE);                 // create an element to trace enemy shots
                    if (enemyShot.InSide(e)) {
                        enemyShot.addElement(e);                   //  places element if inside court, no other checks performed
                        enemyShots.addShip(enemyShot);
                    }
                }
                break;
        }
    }
}
