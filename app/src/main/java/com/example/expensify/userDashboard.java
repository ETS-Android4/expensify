package com.example.expensify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.expensify.ui.homeFragment;
import com.example.expensify.ui.profileFragment;
import com.example.expensify.ui.transactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class userDashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new homeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new homeFragment();
                    break;
                case R.id.transaction:
                    fragment = new transactionFragment();
                    break;
                case R.id.profile:
                    fragment = new profileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, Objects.requireNonNull(fragment)).commit();
            return true;
        });
    }
}