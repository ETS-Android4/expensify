package com.example.expensify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
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

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
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