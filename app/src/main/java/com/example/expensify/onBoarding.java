package com.example.expensify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.expensify.adapter.sliderAdapter;

public class onBoarding extends AppCompatActivity {

    public Button letsGetStarted, next, skip;
    ViewPager viewPager;
    LinearLayout dotsLayout;
    com.example.expensify.adapter.sliderAdapter sliderAdapter;
    TextView[] dots;
    Animation animation;
    int currentPos;
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;

            skip = findViewById(R.id.skip_btn);
            next = findViewById(R.id.next_btn);

            if (position == 0) {
                skip.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                skip.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                skip.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else {
                skip.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);
                animation = AnimationUtils.loadAnimation(onBoarding.this, R.anim.bottom);
                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);

        letsGetStarted.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), registerActivity.class);
            startActivity(i);
        });

        sliderAdapter = new sliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view) {
        startActivity(new Intent(onBoarding.this, loginActivity.class));
        finish();
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {

        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.primary_dark));
        }

    }

}