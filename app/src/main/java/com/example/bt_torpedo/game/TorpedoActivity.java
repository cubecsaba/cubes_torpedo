package com.example.bt_torpedo.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.bt_torpedo.R;
import com.example.bt_torpedo.buildingblocks.Element;
import com.example.bt_torpedo.buildingblocks.EnemyShip;
import com.example.bt_torpedo.buildingblocks.HomeFleet;
import com.example.bt_torpedo.buildingblocks.HomeShip;
import com.example.bt_torpedo.game.Game;
import com.example.bt_torpedo.options.SettingsActivity;
import com.example.bt_torpedo.views.TorpedoViewEnemy;
import com.example.bt_torpedo.views.TorpedoViewHome;

import static com.example.bt_torpedo.entry.StartActivity.sendReceive;
import static com.example.bt_torpedo.views.TorpedoViewHome.xMaxHome;
import static com.example.bt_torpedo.views.TorpedoViewHome.yMaxHome;
import static com.example.bt_torpedo.entry.BT_Activity.sharedPrefs;

public class TorpedoActivity extends AppCompatActivity {

    public static TorpedoViewEnemy tvE;
    TorpedoViewHome tvH;
    float xE, yE;
    public static float xH, yH;
    public static Element eh, ee, es;
    public static HomeShip homeShip;
    public static EnemyShip enemyShip;
    public static EnemyShip enemyShot;
    public static HomeFleet homeFleet, enemyFleet, enemyShots;
    public static int ShipSize;
    private Button b1, b2, b3, b4;
    public static Paint paintH, paintE;
    public static TextView build;
    public static boolean start;
    public static int xCo, yCo;
    public static com.example.bt_torpedo.game.Game Game;
    public static boolean semaphore;
    private EnemyShip enShp;
    public static Vibrator vibr;
    public static MediaPlayer torpedo = null;
    public static MediaPlayer explosion = null;
    public static MediaPlayer boathorn = null;
    public static AlertDialog ad;
    private Intent intent;
    public static boolean silent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setContentView(R.layout.activity_torpedo);
        tvE = findViewById(R.id.viewTEnemy);
        tvH = findViewById(R.id.viewTHome);
        b1 = findViewById(R.id.one);
        b1.setEnabled(true);                          // enable new buttons for new ships
        b2 = findViewById(R.id.two);
        b2.setEnabled(true);
        b3 = findViewById(R.id.three);
        b3.setEnabled(true);
        b4 = findViewById(R.id.four);
        b4.setEnabled(true);
        build = findViewById(R.id.build);
        torpedo = MediaPlayer.create(this, R.raw.torpedo);
        explosion = MediaPlayer.create(this, R.raw.explosion);
        boathorn = MediaPlayer.create(this, R.raw.boathorn);
        intent = getIntent();
        silent = false;

        //------ create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This was a nice game!").setMessage("Another one?");
        builder.setNegativeButton("No, thanks.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //return with result Finish
                intent.putExtra("IsNewGame?", "Finish");
                setResult(RESULT_OK, intent);
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("Yes please!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //return with result Continue
                intent.putExtra("IsNewGame?", "Continue");
                setResult(RESULT_OK, intent);
                dialog.dismiss();
                finish();
            }
        });
        ad = builder.create();
        //**********

        //initialize
        start = false;
        ShipSize = -1;
        semaphore = false;

        homeShip = new HomeShip(); //first element at 0:0 transparent to have a Fleet and its first ship to avoid Null reference
        homeFleet = new HomeFleet();
        eh = new Element(0, 0);
        homeShip.addElement(eh);

        enemyShip = new EnemyShip(); //first element at 0:0 transparent to have a Fleet and its first ship to avoid Null reference
        enemyFleet = new HomeFleet();
        ee = new Element(0, 0);
        enemyShip.addElement(ee);

        enemyShot = new EnemyShip(); //first element at 0:0 transparent to have a Fleet and its first ship to avoid Nullpointer
        enemyShots = new HomeFleet();
        es = new Element(0, 0);

        homeFleet.getFleet().add(homeShip);
        enemyFleet.getFleet().add(enemyShip);
        enemyShots.getFleet().add(enemyShot);

        paintH = new Paint();
        paintH.setColor(Color.BLUE);
        paintE = new Paint();
        paintE.setColor(Color.MAGENTA);
        Game = new Game(); //creates a Game object



        // events on EnemyField
        tvE.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getActionMasked();
                int pointerIndex = event.getActionIndex();
                // activates the "enemy" court only after the enemy has placed his ships as well, and the game has been started
                if (start && semaphore) {
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:  // touch
                            xE = event.getX(pointerIndex);
                            yE = event.getY(pointerIndex);
                            ee = new Element(xE, yE, paintE);                 // creates an element of a possible ship to trace own shots
                            enShp = new EnemyShip();
                            //  if inside court and not yet selected
                            if (enShp.allowElement(ee)) {
                                if (!sharedPrefs.getBoolean("silent", false)) {
                                    torpedo.start();
                                    // short break to avoid voice conjunction
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                enShp.addElement(ee);
                                enemyFleet.addShip(enShp);                  // add element to ship and fleet (the enemyShip consists of single elements)
                                // calculates and transfers indexes to the other phone. There (via myHandler) these will be available as "a" and "b" integers
                                calcCoordinates(xE, yE);
                                String string1 = xCo + "x" + yCo;
                                byte[] bytes1 = string1.getBytes();
                                sendReceive.write(bytes1);
                                // set court inactive
                                semaphore = false;
                                tvE.setBackgroundResource(R.drawable.torpedo);  //no yellow frame -> inactive
                                build.setText("Wait for\nenemy\nshot");
                            }
                            return true;
                    }
                }
                return (true);
            }
        });

        //events on HomeField
        tvH.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                int pointerIndex = event.getActionIndex();
                switch (action) {
                    case MotionEvent.ACTION_DOWN://touching the screen
                        xH = event.getX(pointerIndex);                  //touch x and y coordinates
                        yH = event.getY(pointerIndex);
                        eh = new Element(xH, yH, paintH);                 //create an element of a possible ship
                        if (homeShip.allowElement(eh)) {
                            homeShip.addElement(eh);                       // fill ship with elements that are allowed
                        }
                        // add ship to the fleet if chosen size achieved
                        if (homeShip.elements.size() == ShipSize) {
                            homeFleet.addShip(homeShip);
                            switch (ShipSize) {                             //track size of created ships
                                case 1:
                                    homeFleet.setOneShip(homeFleet.getOneShip() + 1);
                                    break;
                                case 2:
                                    homeFleet.setTwoShip(homeFleet.getTwoShip() + 1);
                                    break;
                                case 3:
                                    homeFleet.setThreeShip(homeFleet.getThreeShip() + 1);
                                    break;
                                case 4:
                                    homeFleet.setFourShip(homeFleet.getFourShip() + 1);
                                    break;
                            }
                            if (homeFleet.getFleet().size() == 11) {
                                setButtonsInvisible();                     //fleet is complete, no more ships allowed
                                build.setText("wait for\nenemy\nfleet");
                                start = true;                               //start the game,
                                game();                                    // send "start" to the other phone
                                return false;                            // return false if fleet is full
                            }
                            resetButtons();                            // reset buttons and shipSize
                        }
                        return true;
                }
                return true;
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(false);
                b1.setBackgroundResource(R.drawable.one_pressed);
                homeShip = new HomeShip();
                ShipSize = 1;
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(false);
                b2.setBackgroundResource(R.drawable.two_pressed);
                homeShip = new HomeShip();
                ShipSize = 2;
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(false);
                b3.setBackgroundResource(R.drawable.three_pressed);
                homeShip = new HomeShip();
                ShipSize = 3;
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(false);
                b4.setBackgroundResource(R.drawable.four_pressed);
                homeShip = new HomeShip();
                ShipSize = 4;
            }
        });
    }

    // creates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // handles menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // starts SettingsActivity if "sound" has been selected from the menu
        if(id==R.id.sound){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    // calculates the indexes
    private void calcCoordinates(float xE, float yE) {
        xCo = (int) (xE / (xMaxHome / 12));
        yCo = (int) (yE / (yMaxHome / 12));
    }

    //sends the message "start" to the other phone, the game can be started
    public static void game() {
        String string1 = "start";
        byte[] bytes1 = string1.getBytes();
        sendReceive.write(bytes1);
    }

    // reset ShipSize and buttons
    public void resetButtons() {
        ShipSize = -1;
        if (homeFleet.getOneShip() < 4) {
            b1.setEnabled(true);
            // enable new buttons for new ships
            b1.setBackgroundResource(R.drawable.one);
        } else b1.setVisibility(View.INVISIBLE);
        if (homeFleet.getTwoShip() < 3) {
            b2.setEnabled(true);
            b2.setBackgroundResource(R.drawable.two);
        } else b2.setVisibility(View.INVISIBLE);
        if (homeFleet.getThreeShip() < 2) {
            b3.setEnabled(true);
            b3.setBackgroundResource(R.drawable.three);
        } else b3.setVisibility(View.INVISIBLE);
        if (homeFleet.getFourShip() < 1) {
            b4.setEnabled(true);
            b4.setBackgroundResource(R.drawable.four);
        } else b4.setVisibility(View.INVISIBLE);
    }

    // set Buttons invisible
    public void setButtonsInvisible() {
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        b4.setVisibility(View.INVISIBLE);
    }


}