package com.example.expensify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensify.R;
import com.example.expensify.adapter.CustomBaseAdapter;
import com.example.expensify.userDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class selectAvatar extends AppCompatActivity {
    public static int[] avatarImages = {R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10};
    public ListView listView;
    public FirebaseAuth mAuth;
    public FirebaseDatabase database;
    public DatabaseReference reference;
    String[] avatarList = {"Peter", "Daniel", "Jacob", "Sofia", "Olivia", "Emily"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_avatar);
        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.avatar_listview);
        CustomBaseAdapter CustomBaseAdapter = new CustomBaseAdapter(getApplicationContext(), avatarList, avatarImages);
        listView.setAdapter(CustomBaseAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("Users");
            reference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("avatar").setValue(position);
            Toast.makeText(getApplicationContext(), "Avatar changed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), userDashboard.class);
            startActivity(intent);
        });
    }
}
