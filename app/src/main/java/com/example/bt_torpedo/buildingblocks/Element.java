package com.example.bt_torpedo.buildingblocks;


import android.graphics.Paint;

import static com.example.bt_torpedo.views.TorpedoViewHome.xMaxHome;
import static com.example.bt_torpedo.views.TorpedoViewHome.yMaxHome;

public class Element {
    private float x;
    private float y;
    private float xStart;
    private float yStart;
    private float xEnd;
    private float yEnd;
    private int xIndex;
    private int yIndex;
    private Paint paint;
    private boolean hit;

    public Element(float x, float y) {
        this.x = x;
        this.y = y;
        sides(x, y);
        hit = false;
    }

    public Element(float x, float y, Paint paint) {
        this.x = x;
        this.y = y;
        this.paint = paint;
        sides(x, y);
        hit = false;
    }

    public Element(int x, int y, Paint paint) {
        this.xIndex = x;
        this.yIndex = y;
        this.paint = paint;
        sidesFromIndex(x, y);
        hit = false;
    }


    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getxStart() {
        return xStart;
    }

    public float getySart() {
        return yStart;
    }

    public float getxEnd() {
        return xEnd;
    }

    public float getyEnd() {
        return yEnd;
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void sides(float x, float y) {
        int i = 0;
        yIndex = 0;
        xIndex = 0;
        boolean found = false;
        while (i < xMaxHome / 12 && !found) {
            if (i * xMaxHome / 12 < x && x < (i + 1) * xMaxHome / 12) {
                xStart = i * xMaxHome / 12;
                xEnd = xStart + xMaxHome / 12;
                found = true;
                xIndex = i;
            }
            i++;
        }
        i = 0;
        found = false;
        while (i < yMaxHome / 12 && !found) {
            if (i * yMaxHome / 12 < y && y < (i + 1) * yMaxHome / 12) {
                yStart = i * yMaxHome / 12;
                yEnd = yStart + yMaxHome / 12;
                yIndex = i;
                found = true;
            }
            i++;
        }
    } //calculates indexes and start&end coordinates of the element

    public void sidesFromIndex(int x, int y) {
        xStart = x * (xMaxHome / 12);
        yStart = y * (yMaxHome / 12);
        xEnd = xStart + xMaxHome / 12;
        yEnd = yStart + yMaxHome / 12;
    } //calculates  start&end coordinates of the element with given index
}
