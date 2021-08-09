package com.example.bt_torpedo.views;

import android.content.Context;
import android.widget.ArrayAdapter;

import static com.example.bt_torpedo.entry.NET_Activity.partnered;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter {

    public MyArrayAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        //return super.isEnabled(position);
        if (partnered)
            return false;
        else return true;

    }
}
