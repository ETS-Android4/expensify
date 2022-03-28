package com.example.expensify;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    EditText username, password;
    Button signup_btn, login_btn;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    FirebaseDatabase nameDb;
    DatabaseReference nameRef;
    private ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        signup_btn = findViewById(R.id.signup);
        login_btn = findViewById(R.id.signin);
        username = findViewById(R.id.user);
        password = findViewById(R.id.pass);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.login_img);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom);
        imageView.setAnimation(bottomAnim);

        signup_btn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), registerActivity.class);
            startActivity(i);
        });

        login_btn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String Username = username.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (Username.isEmpty()) {
            username.setError("Username can not be empty!");
            username.requestFocus();
        } else if (Password.isEmpty()) {
            password.setError("Password can not be empty!");
            password.requestFocus();
        } else {
            progressDialog.setMessage("Logging in Please Wait...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(Username, Password).addOnCompleteListener(this, task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), userDashboard.class));
                    nameDb = FirebaseDatabase.getInstance();
                    nameRef = nameDb.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("fullName");
                    nameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            String[] parts = Objects.requireNonNull(name).split(" ");
                            String firstName = parts[0];
                            Toast.makeText(getApplicationContext(), "Welcome " + firstName + " 😊!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE);
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", true);
                    if (isLoggedIn) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), userDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (!isConnectedToInternet()) {
                    Toast.makeText(getApplicationContext(), "Check your Internet connection and Try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) loginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}