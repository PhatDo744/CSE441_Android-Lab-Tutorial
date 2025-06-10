package com.example.dabrateviewer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<Tygia> {
    Activity context;
    int resource;
    List<Tygia> objects;

    public MyArrayAdapter(Activity context, int resource, List<Tygia> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        Tygia tygia = this.objects.get(position);

        ImageView imgHinh = (ImageView) item.findViewById(R.id.imgHinh);
        TextView txtType = (TextView) item.findViewById(R.id.txtType);
        TextView txtMuaTM = (TextView) item.findViewById(R.id.txtMuaTM);
        TextView txtBanTM = (TextView) item.findViewById(R.id.txtBanTM); // GTM-fixed typo: txt.BanTM -> txtBanTM
        TextView txtMuaCK = (TextView) item.findViewById(R.id.txtMuaCK);
        TextView txtBanCK = (TextView) item.findViewById(R.id.txtBanCK);

        if (tygia.getBitmap() != null) {
            imgHinh.setImageBitmap(tygia.getBitmap());
        } else {
            // Có thể đặt một hình ảnh mặc định nếu bitmap là null
            imgHinh.setImageResource(R.drawable.ic_launcher_foreground); // Ví dụ
        }
        txtType.setText(tygia.getType());
        txtMuaTM.setText(tygia.getMuatienmat());
        txtBanTM.setText(tygia.getBantienmat()); // Sửa tên phương thức
        txtMuaCK.setText(tygia.getMuack());
        txtBanCK.setText(tygia.getBanck());

        return item;
    }
}
