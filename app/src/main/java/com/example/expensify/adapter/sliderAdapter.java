package com.example.expensify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.expensify.R;

public class sliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    int[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
    };
    int[] headings = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three,
            R.string.heading_four,
    };
    int[] description = {
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three,
            R.string.desc_four,
    };

    public sliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);

        ImageView imageView = view.findViewById(R.id.img);
        TextView heading = view.findViewById(R.id.heading);
        TextView desc = view.findViewById(R.id.description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(description[position]);

        container.addView(view);
        return view;
    }
}