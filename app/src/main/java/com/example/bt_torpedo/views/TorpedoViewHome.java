package com.example.bt_torpedo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import static com.example.bt_torpedo.game.MyHandler.a; // X index of an enemy shoot
import static com.example.bt_torpedo.game.MyHandler.b; // Y index of an enemy shoot
import static com.example.bt_torpedo.entry.StartActivity.sendReceive;
import static com.example.bt_torpedo.game.TorpedoActivity.homeFleet;
import static com.example.bt_torpedo.game.TorpedoActivity.homeShip;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyShots;


public class TorpedoViewHome extends View {

    public Paint paintE; //rajzoláshoz kell
    private Paint paintH;
    private Paint paintWhite;
    public static float xMaxHome; //kijelző maximális méretei
    public static float yMaxHome;
    float sideX, sideY;


    public TorpedoViewHome(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintE = new Paint();
        paintE.setStrokeWidth(3);
        paintH = new Paint();
        paintWhite = new Paint();
        paintE.setColor(Color.MAGENTA);
        paintH.setColor(Color.GREEN);
        paintWhite.setColor(Color.TRANSPARENT);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) /*get and transfers the X and Y sizes of the Home court to the other phone*/ {
        super.onSizeChanged(w, h, oldw, oldh);
        xMaxHome = w;
        yMaxHome = h;
        sideX = xMaxHome / 12;
        sideY = yMaxHome / 12;
        String string1 = xMaxHome + "x" + yMaxHome + "x" + "A";
        byte[] bytes1 = string1.getBytes();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendReceive.write(bytes1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < homeShip.elements.size(); i++) {            // showing real time as touched
            canvas.drawRect(
                    homeShip.elements.get(i).getxStart(),
                    homeShip.elements.get(i).getySart(),
                    homeShip.elements.get(i).getxEnd(),
                    homeShip.elements.get(i).getyEnd(),
                    paintH);
        }

        if (homeFleet.getFleet() != null) {                         // showing all ships from the fleet list after a ship of a chosen size is finished
            for (int j = 0; j < homeFleet.getFleet().size(); j++) {
                if (homeFleet.getFleet().get(j) != null && homeFleet.getFleet().get(j).elements != null) {
                    for (int i = 0; i < homeFleet.getFleet().get(j).elements.size(); i++) {//first ship of the fleet is a dummy, one pixel, transparent
                        if (j == 0) {
                            canvas.drawRect(
                                    homeFleet.getFleet().get(j).elements.get(i).getxStart(),
                                    homeFleet.getFleet().get(j).elements.get(i).getySart(),
                                    homeFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                    homeFleet.getFleet().get(j).elements.get(i).getyEnd(), paintWhite);
                        } else {                                                    //all other ships are blue and element sized

                            if (homeFleet.getFleet().get(j).elements.get(i).getxIndex() == a && // if the enemy shot an element, its colour changes to painE
                                    homeFleet.getFleet().get(j).elements.get(i).getyIndex() == b) {
                                homeFleet.getFleet().get(j).elements.get(i).setPaint(paintE);
                            }

                            canvas.drawRect(
                                    homeFleet.getFleet().get(j).elements.get(i).getxStart(),
                                    homeFleet.getFleet().get(j).elements.get(i).getySart(),
                                    homeFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                    homeFleet.getFleet().get(j).elements.get(i).getyEnd(),
                                    homeFleet.getFleet().get(j).elements.get(i).getPaint());
                        }
                    }
                }
            }
        }

        if (enemyShots.getFleet() != null) {
            for (int j = 0; j < enemyShots.getFleet().size(); j++) {
                if (enemyShots.getFleet().get(j) != null && enemyShots.getFleet().get(j).elements != null) {
                    for (int i = 0; i < enemyShots.getFleet().get(j).elements.size(); i++) {//first ship of the fleet is a dummy, one pixel, transparent
 {
                            canvas.drawLine(
                                    enemyShots.getFleet().get(j).elements.get(i).getxStart(),
                                    enemyShots.getFleet().get(j).elements.get(i).getySart(),
                                    enemyShots.getFleet().get(j).elements.get(i).getxEnd(),
                                    enemyShots.getFleet().get(j).elements.get(i).getyEnd(),
                                    paintE
                            );
                            canvas.drawLine(
                                    enemyShots.getFleet().get(j).elements.get(i).getxStart(),
                                    enemyShots.getFleet().get(j).elements.get(i).getyEnd(),
                                    enemyShots.getFleet().get(j).elements.get(i).getxEnd(),
                                    enemyShots.getFleet().get(j).elements.get(i).getySart(),
                                    paintE
                            );
                        }
                    }
                }
            }
        }

        try {
            Thread.sleep(5); //szünet az újrarajzolás előtt
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();//invalidate mindig meghivja az onDraw-t, vegetelen ciklus
    }
}




