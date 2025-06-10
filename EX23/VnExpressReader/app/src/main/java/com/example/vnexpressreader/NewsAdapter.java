package com.example.vnexpressreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    Activity context;
    int layoutResourceId;
    ArrayList<NewsItem> data = null;

    public NewsAdapter(Activity context, int layoutResourceId, ArrayList<NewsItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewsItemHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new NewsItemHolder();
            holder.imgIcon = row.findViewById(R.id.imgView);
            holder.txtTitle = row.findViewById(R.id.txtTitle);
            holder.txtDescription = row.findViewById(R.id.txtDescription);

            row.setTag(holder);
        } else {
            holder = (NewsItemHolder) row.getTag();
        }

        NewsItem item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtDescription.setText(item.getDescription());

        if (item.getImageBitmap() != null) {
            holder.imgIcon.setImageBitmap(item.getImageBitmap());
        } else {
            holder.imgIcon.setImageResource(R.mipmap.ic_launcher);
        }
        return row;
    }

    static class NewsItemHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDescription;
    }
}
