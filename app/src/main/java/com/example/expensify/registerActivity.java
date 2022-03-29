package com.example.expensify;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensify.model.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class registerActivity extends AppCompatActivity {

    EditText name, email, password, rePass;
    CountryCodePicker country;
    Button signup, user_exist;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rePass = findViewById(R.id.confirmPass);
        signup = findViewById(R.id.submit);
        user_exist = findViewById(R.id.login_exist);
        name = findViewById(R.id.name);
        country = findViewById(R.id.ccp);
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> createUser());

        user_exist.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
        });
    }

    private void createUser() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");
        String Username = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repass = rePass.getText().toString().trim();
        String Name = name.getText().toString().trim();
        String Country = country.getSelectedCountryName();
        ProgressDialog progressDialog = new ProgressDialog(this);

        if (Username.isEmpty() || pass.isEmpty() || repass.isEmpty() || Name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter the required fields", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(repass)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Username).matches()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(registerActivity.this);
            builder.setTitle("Invalid Email!");
            builder.setMessage("Please enter a valid email address! eg: username@expensify.com");
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(Username, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String uid = Objects.requireNonNull(currentUser).getUid();
                    userModel userModel = new userModel(uid, Name, Username, pass, Country);
                    reference.child(uid).setValue(userModel);
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                } else if (!isConnectedToInternet()) {
                    Toast.makeText(getApplicationContext(), "Unable to Register at the moment, check your Internet connection and try again", Toast.LENGTH_LONG).show();
                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            });
        }
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) registerActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}