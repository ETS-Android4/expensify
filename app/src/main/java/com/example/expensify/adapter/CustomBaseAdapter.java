package com.example.expensify.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expensify.R;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String[] listAvatar;
    int[] listImages;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String[] countryList, int[] countryImages) {
        this.context = ctx;
        this.listAvatar = countryList;
        this.listImages = countryImages;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return listAvatar.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_avatar_listview, null);
        TextView textView = convertView.findViewById(R.id.avatar_name);
        ImageView avatarImg = convertView.findViewById(R.id.avatar);
        textView.setText(listAvatar[position]);
        avatarImg.setImageResource(listImages[position]);
        return convertView;
    }
}