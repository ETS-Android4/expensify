package com.example.expensify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.expensify.adapter.currencyAdapter;
import com.example.expensify.model.expenseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class checkStatus extends AppCompatActivity {

    public FirebaseDatabase avatarDB, database, database2;
    public DatabaseReference avatarRef, homeRef, homeRef2;
    public ImageView avatarImg;
    public TextView tv1, tv2, tv3, tv4, tv5, tv6, report_status;
    public Integer totalExpense, totalIncome;
    public LottieAnimationView lottieAnimationView;
    ConstraintLayout noTransInReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_status);

        avatarImg = findViewById(R.id.user_avatar);

        tv1 = findViewById(R.id.expense_user);
        tv2 = findViewById(R.id.country_of_residence);
        tv3 = findViewById(R.id.spending);
        tv4 = findViewById(R.id.income);
        tv5 = findViewById(R.id.most_spent_on);
        tv6 = findViewById(R.id.savings);
        report_status = findViewById(R.id.report_status);

        noTransInReport = findViewById(R.id.no_trans_in_report);
        lottieAnimationView = findViewById(R.id.status);

        avatarDB = FirebaseDatabase.getInstance();
        avatarRef = avatarDB.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("avatar");
        avatarRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer avatar = dataSnapshot.getValue(Integer.class);
                if (avatar == null) {
                    avatarImg.setImageResource(R.drawable.image29);
                } else if (avatar == 1) {
                    avatarImg.setImageResource(R.drawable.image30);
                } else if (avatar == 2) {
                    avatarImg.setImageResource(R.drawable.image31);
                } else if (avatar == 3) {
                    avatarImg.setImageResource(R.drawable.image32);
                } else if (avatar == 4) {
                    avatarImg.setImageResource(R.drawable.image33);
                } else if (avatar == 5) {
                    avatarImg.setImageResource(R.drawable.image34);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("fullName")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        tv1.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("country")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        tv2.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        homeRef.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("unchecked")
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalExpense = 0;
                totalIncome = 0;
                for (DataSnapshot d : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) d.getValue();
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
                    } else if (String.valueOf(type).equals("income")) {
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalIncome += pTotal;
                    }
                    int savings = totalIncome - totalExpense;
                    tv3.setText("-" + totalExpense + currencyAdapter.setUserCurrency());
                    tv3.setTextColor(getResources().getColor(R.color.red));
                    tv4.setText("+" + totalIncome + currencyAdapter.setUserCurrency());
                    tv4.setTextColor(getResources().getColor(R.color.green));
                    if (savings < 0) {
                        tv6.setText(savings + currencyAdapter.setUserCurrency());
                        tv6.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        tv6.setText("+" + savings + currencyAdapter.setUserCurrency());
                        tv6.setTextColor(getResources().getColor(R.color.green));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database2 = FirebaseDatabase.getInstance();
        homeRef2 = database2.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        homeRef2.addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("unchecked")
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int max = 0;
                String category = "";
                for (DataSnapshot d : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) d.getValue();
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
                        if (pTotal > max) {
                            max = pTotal;
                            category = String.valueOf(Objects.requireNonNull(map).get("category"));
                        }
                    }
                }
                tv5.setText(category + ": " + max + currencyAdapter.setUserCurrency());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            expenseModel p = d.getValue(expenseModel.class);
                            if (p == null) {
                                noTransInReport.setVisibility(View.VISIBLE);
                            } else {
                                noTransInReport.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("UserBank").child("Budget")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String budget = snapshot.getValue(String.class);
                        if (budget != null) {
                            if (Integer.parseInt(budget) < totalExpense) {
                                report_status.setText("You have failed to meet your budget goals and have exceeded your budget limit by " + (totalExpense - Integer.parseInt(budget)) + currencyAdapter.setUserCurrency() + " ! Try to reduce your expenses");
                                report_status.setTextColor(getResources().getColor(R.color.red));
                                lottieAnimationView.setAnimation(R.raw.failure);
                                lottieAnimationView.playAnimation();
                            } else if (Integer.parseInt(budget) * 0.8 <= totalExpense && Integer.parseInt(budget) * 0.9 >= totalExpense) {
                                report_status.setText("Your expenses have crossed 80% of your budget limit! Try to reduce your expenses");
                                report_status.setTextColor(getResources().getColor(R.color.warning));
                                lottieAnimationView.setAnimation(R.raw.warning);
                                lottieAnimationView.playAnimation();
                            } else if (Integer.parseInt(budget) * 0.5 <= totalExpense) {
                                report_status.setText("Your expenses have crossed 50% of your budget limit! Try to reduce your expenses");
                                report_status.setTextColor(getResources().getColor(R.color.warning));
                                lottieAnimationView.setAnimation(R.raw.warning);
                                lottieAnimationView.playAnimation();
                            } else {
                                report_status.setText("Good Job so far! You are within your budget limit. Keep it up!");
                                report_status.setTextColor(getResources().getColor(R.color.green));
                                lottieAnimationView.setAnimation(R.raw.success);
                                lottieAnimationView.playAnimation();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}