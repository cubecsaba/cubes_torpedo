package com.example.bt_torpedo.options;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bt_torpedo.R;

public class ListItemAdapter extends ArrayAdapter {
    private int resource;
// assigns adapter and list item
    public ListItemAdapter(@NonNull Context context, int resource, @NonNull String[] objects ) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = (String) getItem(position);
        if (convertView == null) {              //list item felfújása
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }
        TextView tvName = convertView.findViewById(R.id.tvName);
        tvName.setText(name);
        return convertView;
    }
}
