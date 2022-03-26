package com.example.expensify.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expensify.R;
import com.example.expensify.model.expenseModel;

import java.util.ArrayList;

public class expenseAdapter extends BaseAdapter {

    Context context;
    ArrayList<expenseModel> arrayList;

    public expenseAdapter(Context context, ArrayList<expenseModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expense_adapter, parent, false);
        }

        TextView homeCategory, homeNote, homeDate, type;

        homeCategory = convertView.findViewById(R.id.home_category);
        homeNote = convertView.findViewById(R.id.home_note);
        homeDate = convertView.findViewById(R.id.home_date);
        type = convertView.findViewById(R.id.expense_amount);

        homeCategory.setText(arrayList.get(position).getCategory());
        homeNote.setText("Note: " + arrayList.get(position).getNote());
        homeDate.setText("Date: " + arrayList.get(position).getDate());

        if (arrayList.get(position).getType().equals("income")) {
            type.setText("+" + arrayList.get(position).getAmount() + currencyAdapter.setUserCurrency());
            type.setTextColor(Color.parseColor("#08a373"));
        } else {
            type.setText("-" + arrayList.get(position).getAmount() + currencyAdapter.setUserCurrency());
            type.setTextColor(Color.parseColor("#FD3C4A"));
        }
        return convertView;
    }
}