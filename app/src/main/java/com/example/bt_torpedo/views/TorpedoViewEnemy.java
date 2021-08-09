package com.example.bt_torpedo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import static com.example.bt_torpedo.entry.StartActivity.sendReceive;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyFleet;
import static com.example.bt_torpedo.game.TorpedoActivity.enemyShip;


public class TorpedoViewEnemy extends View {

    private Paint paintE, paintWhite, paintSunken; //rajzoláshoz kell
    public static float xMaxEnemy; //kijelző maximális méretei
    public static float yMaxEnemy;


    public TorpedoViewEnemy(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintE = new Paint();
        paintWhite = new Paint();
        paintSunken = new Paint();
        paintE.setStrokeWidth(3);
        paintE.setColor(Color.MAGENTA);
        paintWhite.setColor(Color.TRANSPARENT);
        paintSunken.setColor(Color.BLACK);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        xMaxEnemy = w;
        yMaxEnemy = h;
        String string1 = xMaxEnemy + "x" + yMaxEnemy + "x" + "A";
        byte[] bytes1 = string1.getBytes();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendReceive.write(bytes1);
    } // get the max X and Y coordinates (sizes of the Enemy view) and transfer them to the other phone

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < enemyShip.elements.size(); i++) {
            canvas.drawRect(
                    enemyShip.elements.get(i).getxStart(),
                    enemyShip.elements.get(i).getySart(),
                    enemyShip.elements.get(i).getxEnd(),
                    enemyShip.elements.get(i).getyEnd(),
                    paintE);
        } // showing real time as touched

        if (enemyFleet.getFleet() != null) /*showing all ships from the fleet list*/ {
            for (int j = 0; j < enemyFleet.getFleet().size(); j++) {
                if (enemyFleet.getFleet().get(j) != null && enemyFleet.getFleet().get(j).elements != null) {
                    for (int i = 0; i < enemyFleet.getFleet().get(j).elements.size(); i++) {
                        if (j == 0) {                                               //first ship of the fleet is a dummy, one pixel, transparent
                            canvas.drawRect(
                                    enemyFleet.getFleet().get(j).elements.get(i).getxStart(),
                                    enemyFleet.getFleet().get(j).elements.get(i).getySart(),
                                    enemyFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                    enemyFleet.getFleet().get(j).elements.get(i).getyEnd(), paintWhite);
                        } else {                                                    //all other ships are magenta and element sized

                            if (!enemyFleet.getFleet().get(j).elements.get(i).isHit()) {
                                canvas.drawLine(
                                        enemyFleet.getFleet().get(j).elements.get(i).getxStart(),
                                        enemyFleet.getFleet().get(j).elements.get(i).getySart(),
                                        enemyFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                        enemyFleet.getFleet().get(j).elements.get(i).getyEnd(),
                                        paintE
                                );
                                canvas.drawLine(
                                        enemyFleet.getFleet().get(j).elements.get(i).getxStart(),
                                        enemyFleet.getFleet().get(j).elements.get(i).getyEnd(),
                                        enemyFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                        enemyFleet.getFleet().get(j).elements.get(i).getySart(),
                                        paintE);

                            } else {
                                if (!enemyFleet.getFleet().get(j).getSunk()) {
                                    canvas.drawRect(
                                            enemyFleet.getFleet().get(j).elements.get(i).getxStart(),
                                            enemyFleet.getFleet().get(j).elements.get(i).getySart(),
                                            enemyFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                            enemyFleet.getFleet().get(j).elements.get(i).getyEnd(),
                                            paintE);

                                } else {
                                    canvas.drawRect(
                                            enemyFleet.getFleet().get(j).elements.get(i).getxStart(),
                                            enemyFleet.getFleet().get(j).elements.get(i).getySart(),
                                            enemyFleet.getFleet().get(j).elements.get(i).getxEnd(),
                                            enemyFleet.getFleet().get(j).elements.get(i).getyEnd(),
                                            paintSunken);

                                }
                            }
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
