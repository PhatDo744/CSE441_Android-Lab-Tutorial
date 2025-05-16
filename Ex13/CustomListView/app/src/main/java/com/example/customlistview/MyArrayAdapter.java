package com.example.customlistview;

import android.app.Activity;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<ContactsContract.CommonDataKinds.Phone> {
    Activity context;
    int idlayout;
    ArrayList<ContactsContract.CommonDataKinds.Phone> mylist;
    public MyArrayAdapter(Activity context, int idlayout, ArrayList<ContactsContract.CommonDataKinds.Phone> mylist) {
        super(context, idlayout, mylist);
        this.context = context;
        this.idlayout = idlayout;
        this.mylist = mylist;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflater = context.getLayoutInflater();
        convertView = myInflater.inflate(idlayout, null);

        ContactsContract.CommonDataKinds.Phone myphone = mylist.get(position);

        // Gán id
        ImageView imgphone = convertView.findViewById(R.id.imgphone);
        // Thiết lập dữ liệu
        imgphone.setImageResource(myphone.getImagephone());

        // ----------- TextView ------------
        TextView txtnamephone = convertView.findViewById(R.id.txtnamephone);
        txtnamephone.setText(myphone.getNamephone());

        return convertView;
    }
}
