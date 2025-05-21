package com.example.tabselector2.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tabselector2.Models.Item;
import com.example.tabselector2.R;

import java.util.ArrayList;
import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<Item> {
    Activity context = null;
    ArrayList<Item> myArrayList = null;
    int layoutId;
    
    public MyArrayAdapter(Activity context, int layoutId, @NonNull List<Item> objects) {
        super(context, layoutId, objects);
        this.context = context;
        this.layoutId = layoutId;
        this.myArrayList = (ArrayList<Item>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);
        }
        
        Item item = myArrayList.get(position);

        ImageButton btnlike = convertView.findViewById(R.id.btnlike);
        updateHeartIcon(btnlike, item);
        
        btnlike.setOnClickListener(v -> {
            // Toggle like status
            if (item.getThich() == 1) {
                item.setThich(0);
            } else {
                item.setThich(1);
            }
            updateHeartIcon(btnlike, item);
            
            // Notify any other adapters that might be displaying this item
            notifyDataSetChanged();
        });

        TextView tieude = convertView.findViewById(R.id.txttieude);
        tieude.setText(item.getTieude());
        
        TextView maso = convertView.findViewById(R.id.txtmaso);
        maso.setText(item.getMaso());
        
        return convertView;
    }
    
    // Helper method to update heart icon based on like state
    private void updateHeartIcon(ImageButton btnlike, Item item) {
        if (item.getThich() == 1) {
            btnlike.setImageResource(R.drawable.heart_147246);
        } else {
            btnlike.setImageResource(R.drawable.black_152877);
        }
    }
}
