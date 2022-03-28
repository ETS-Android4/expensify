package com.example.expensify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 2000;
    Animation topAnim, bottomAnim;
    TextView title;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom);
        mAuth = FirebaseAuth.getInstance();

        title = findViewById(R.id.tv1);
        title.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            sharedPreferences = getSharedPreferences("onboarding_status", MODE_PRIVATE);
            boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
            if (isFirstRun) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstRun", false);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), onBoarding.class);
                startActivity(intent);
                finish();
            } else if (isConnectedToInternet()) {
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getApplicationContext(), userDashboard.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                    startActivity(intent);
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
            }
        }, SPLASH_SCREEN);
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}