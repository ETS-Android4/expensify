package com.example.expensify.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.expensify.R;
import com.example.expensify.adapter.currencyAdapter;
import com.example.expensify.loginActivity;
import com.example.expensify.userDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;


public class profileFragment extends Fragment {

    public TextView profEmail, helloUser, totalSpent, totalEarned;
    public Integer totalExpense, totalIncome;
    public FirebaseDatabase database;
    public DatabaseReference reference;
    FirebaseAuth mAuth;
    ImageView imageView;
    Button editAcc, updateProfileBtn, logOut, createBudget, changeCurrency, changeAvatar;
    EditText edit_profile_username, edit_profile_password;
    private String budgetAmount, walletAmount;

    @Override
    public void onStart() {
        super.onStart();
        getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logOut = view.findViewById(R.id.logout);
        profEmail = view.findViewById(R.id.profile_email);
        helloUser = view.findViewById(R.id.helloUser);
        totalSpent = view.findViewById(R.id.total_spent);
        totalEarned = view.findViewById(R.id.total_earned);
        mAuth = FirebaseAuth.getInstance();
        editAcc = view.findViewById(R.id.edit_profile);
        createBudget = view.findViewById(R.id.createBudget);
        changeCurrency = view.findViewById(R.id.changeCurrency);
        changeAvatar = view.findViewById(R.id.change_avatar);
        imageView = view.findViewById(R.id.profile_image);

        changeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), selectAvatar.class);
            startActivity(intent);
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("avatar");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer avatar = dataSnapshot.getValue(Integer.class);
                if (avatar == null) {
                    imageView.setImageResource(R.drawable.image5);
                } else if (avatar == 1) {
                    imageView.setImageResource(R.drawable.image6);
                } else if (avatar == 2) {
                    imageView.setImageResource(R.drawable.image7);
                } else if (avatar == 3) {
                    imageView.setImageResource(R.drawable.image8);
                } else if (avatar == 4) {
                    imageView.setImageResource(R.drawable.image9);
                } else if (avatar == 5) {
                    imageView.setImageResource(R.drawable.image10);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        profEmail.setText(Objects.requireNonNull(user).getEmail());

        FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("Transactions")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        totalExpense = 0;
                        totalIncome = 0;
                        if (getContext() != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                Object total = null;
                                if (map != null) {
                                    total = map.get("amount");
                                }
                                Object type = null;
                                if (map != null) {
                                    type = map.get("type");
                                }
                                if (String.valueOf(type).equals("expense")) {
                                    int pTotal = Integer.parseInt(String.valueOf(total));
                                    totalExpense += pTotal;
                                }
                                if (String.valueOf(type).equals("income")) {
                                    int pTotal = Integer.parseInt(String.valueOf(total));
                                    totalIncome += pTotal;
                                }
                            }
                            if (totalExpense == 0 && totalIncome == 0) {
                                totalSpent.setVisibility(View.GONE);
                                totalEarned.setVisibility(View.GONE);
                            } else {
                                totalSpent.setVisibility(View.VISIBLE);
                                totalEarned.setVisibility(View.VISIBLE);
                                totalSpent.setText("Total Spending's : -" + totalExpense + currencyAdapter.setUserCurrency());
                                totalSpent.setTextColor(getResources().getColor(R.color.red));
                                totalEarned.setText("Total Earnings's : +" + totalIncome + currencyAdapter.setUserCurrency());
                                totalEarned.setTextColor(getResources().getColor(R.color.green));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        editAcc.setOnClickListener(v -> alertUpdate());
        logOut.setOnClickListener(v -> alertLogOut());
        createBudget.setOnClickListener(v -> alertCreateBudget());
        changeCurrency.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), currencyAdapter.class);
            startActivity(intent);
        });
        return view;
    }

    private void alertCreateBudget() {
        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.activity_budget, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view2);
        final AlertDialog dialog = builder.create();
        dialog.show();

        EditText budget = view2.findViewById(R.id.budget);
        EditText wallet = view2.findViewById(R.id.wallet_amount);
        Button addBudget = view2.findViewById(R.id.addBudget);

        addBudget.setOnClickListener(v -> {
            budgetAmount = budget.getText().toString().trim();
            walletAmount = wallet.getText().toString().trim();
            if (budgetAmount.isEmpty()) {
                budget.setError("Please enter a budget amount");
            } else if (budgetAmount.contains(".")) {
                budget.setError("Enter an approximate amount");
            } else if (walletAmount.contains(".")) {
                wallet.setError("Enter an approximate amount");
            } else if (Integer.parseInt(budgetAmount) == 0) {
                budget.setError("Please enter a valid budget amount");
            } else if (walletAmount.isEmpty()) {
                wallet.setError("Please enter a wallet amount");
            } else if (Integer.parseInt(walletAmount) == 0) {
                wallet.setError("Please enter a valid wallet amount");
            } else if (!budgetAmount.matches("[0-9]+")) {
                budget.setError("Please enter a valid amount");
            } else if (!walletAmount.matches("[0-9]+")) {
                wallet.setError("Please enter a valid amount");
            } else if (Integer.parseInt(budgetAmount) > Integer.parseInt(walletAmount)) {
                Toast.makeText(getContext(), "Budget amount can't be greater than Wallet amount! ", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(requireActivity(), userDashboard.class);
                startActivity(intent);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");
                myRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("UserBank").child("Wallet").setValue(walletAmount);
                myRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("UserBank").child("Budget").setValue(budgetAmount);
                dialog.dismiss();
                Toast.makeText(getContext(), "Budget and Wallet Added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alertUpdate() {
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view1);
        final AlertDialog dialog = builder.create();
        dialog.show();

        edit_profile_username = view1.findViewById(R.id.edit_profile_username);
        edit_profile_password = view1.findViewById(R.id.edit_profile_password);
        updateProfileBtn = view1.findViewById(R.id.edit_profile_button);

        updateProfileBtn.setOnClickListener(v -> {
            if (getContext() != null) {
                if (edit_profile_username.getText().toString().isEmpty() || edit_profile_password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the required fields", Toast.LENGTH_SHORT).show();
                } else if (edit_profile_password.getText().toString().length() < 6) {
                    Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_profile_username.getText().toString()).matches()) {
                    edit_profile_username.setError("Invalid Email");
                } else if (edit_profile_username.getText().toString().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())) {
                    Toast.makeText(getContext(), "Username can not be same as before", Toast.LENGTH_SHORT).show();
                } else {
                    if (getContext() != null) {
                        mAuth.getCurrentUser().updateEmail(edit_profile_username.getText().toString().trim());
                        mAuth.getCurrentUser().updatePassword(edit_profile_password.getText().toString().trim());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(mAuth.getCurrentUser().getUid()).child("email").setValue(edit_profile_username.getText().toString());
                        databaseReference.child(mAuth.getCurrentUser().getUid()).child("password").setValue(edit_profile_password.getText().toString());
                        profEmail.setText(edit_profile_username.getText().toString());
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void alertLogOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setMessage("You will be returned to the login screen");
        dialog.setTitle("Log Out?");
        dialog.setPositiveButton("Log Out",
                (dialog12, which) -> {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), loginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                });
        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void getName() {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("fullName")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue(String.class);
                        if (name == null) {
                            name = "";
                        }
                        String[] parts = Objects.requireNonNull(name).split(" ");
                        String firstName = parts[0];
                        helloUser.setText("Hello " + firstName + " 👋");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}