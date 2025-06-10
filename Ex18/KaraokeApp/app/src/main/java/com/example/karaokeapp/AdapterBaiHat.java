package com.example.karaokeapp;

import android.app.Activity;
import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterBaiHat extends ArrayAdapter<BaiHat> {
    Activity context;
    int resource;
    ArrayList<BaiHat> objects;

    public AdapterBaiHat(Activity context, int resource, ArrayList<BaiHat> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(this.resource, null);
            holder = new ViewHolder();
            holder.txtMaSo = convertView.findViewById(R.id.txtMaSo);
            holder.txtTenBaiHat = convertView.findViewById(R.id.txtTenBaiHat);
            holder.btnLike = convertView.findViewById(R.id.btnLike);
            holder.btnUnlike = convertView.findViewById(R.id.btnUnlike);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BaiHat baiHat = this.objects.get(position);
        holder.txtMaSo.setText(baiHat.getMaBH());
        holder.txtTenBaiHat.setText(baiHat.getTenBH());

        if (baiHat.isYeuThich()) {
            holder.btnLike.setVisibility(View.VISIBLE);
            holder.btnUnlike.setVisibility(View.GONE);
        } else {
            holder.btnLike.setVisibility(View.GONE);
            holder.btnUnlike.setVisibility(View.VISIBLE);
        }

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý bỏ thích
                xuLyBoThich(baiHat);
                holder.btnLike.setVisibility(View.GONE);
                holder.btnUnlike.setVisibility(View.VISIBLE);
            }
        });

        holder.btnUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý thích
                xuLyThich(baiHat);
                holder.btnLike.setVisibility(View.VISIBLE);
                holder.btnUnlike.setVisibility(View.GONE);
            }
        });

        return convertView;
    }

    private void xuLyThich(BaiHat baiHat) {
        baiHat.setYeuThich(true);
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", 1); // 1 là thích
        int kq = MainActivity.database.update("ArirangSongBook", values, "MABH=?", new String[]{baiHat.getMaBH()});
        if (kq > 0) {
            Toast.makeText(context, "Đã thích bài hát: " + baiHat.getTenBH(), Toast.LENGTH_SHORT).show();
            // Nếu MainActivity có danh sách yêu thích đang hiển thị, cần cập nhật
            if (context instanceof MainActivity) {
                ((MainActivity) context).capNhatLaiDanhSachYeuThichKhiThaoTac();
            }
        } else {
            Toast.makeText(context, "Lỗi khi thích bài hát!", Toast.LENGTH_SHORT).show();
            baiHat.setYeuThich(false); // Rollback
        }
        notifyDataSetChanged(); // Cập nhật lại item này
    }

    private void xuLyBoThich(BaiHat baiHat) {
        baiHat.setYeuThich(false);
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", 0); // 0 là không thích
        int kq = MainActivity.database.update("ArirangSongBook", values, "MABH=?", new String[]{baiHat.getMaBH()});
        if (kq > 0) {
            Toast.makeText(context, "Đã bỏ thích bài hát: " + baiHat.getTenBH(), Toast.LENGTH_SHORT).show();
            // Nếu MainActivity có danh sách yêu thích đang hiển thị, cần cập nhật
            if (context instanceof MainActivity) {
                ((MainActivity) context).capNhatLaiDanhSachYeuThichKhiThaoTac();
            }
        } else {
            Toast.makeText(context, "Lỗi khi bỏ thích bài hát!", Toast.LENGTH_SHORT).show();
            baiHat.setYeuThich(true); // Rollback
        }
        notifyDataSetChanged(); // Cập nhật lại item này
    }

    // ViewHolder pattern
    private static class ViewHolder {
        TextView txtMaSo;
        TextView txtTenBaiHat;
        ImageButton btnLike;
        ImageButton btnUnlike;
    }
}
