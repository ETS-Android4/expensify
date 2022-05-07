package com.example.expensify.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.expensify.R;
import com.example.expensify.adapter.currencyAdapter;
import com.example.expensify.adapter.expenseAdapter;
import com.example.expensify.model.expenseModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class homeFragment extends Fragment {
    public ConstraintLayout homeLayout;
    public ImageView budgetNullAvatar, walletNullAvatar, budgetExdAvatar;
    public Button expenseBtn, incomeBtn, categoryBtnExpense, categoryBtnIncome;
    public TextView tv1Pt, tv2Pt, tv3Pt, tv4Pt, tv5Pt, tv6Pt, tv7Pt, tv8Pt, tv9Pt, tv10Pt, tv11Pt, tv12Pt, tv13Pt, tv14Pt;
    public ProgressDialog progressDialog;
    public String mAmount, mNotes, category;
    public Integer totalExpense;
    public LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8, ll9, ll10, ll11, ll12, ll13, ll14;
    ListView listView;
    TextView homeBudget, walletBalance, recTrans;
    FirebaseDatabase database, database2, database3, database4;
    DatabaseReference homeRef, homeRef2, homeRef3, homeRef4;
    private List<expenseModel> transList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        transList = new ArrayList<>();
        getTransactions();
        currencyAdapter.setUserCurrency();
        getWalletBalance();
        getBudgetBalance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currencyAdapter.setUserCurrency();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        expenseBtn = view.findViewById(R.id.expenseBtn);
        incomeBtn = view.findViewById(R.id.incomeBtn);
        recTrans = view.findViewById(R.id.recent_transactions);
        homeLayout = view.findViewById(R.id.noTransLayout);
        homeBudget = view.findViewById(R.id.homeBudget);
        walletBalance = view.findViewById(R.id.walletBalance);
        listView = view.findViewById(R.id.list);

        ll1 = view.findViewById(R.id.ll1);
        ll2 = view.findViewById(R.id.ll2);
        ll3 = view.findViewById(R.id.ll3);
        ll4 = view.findViewById(R.id.ll4);
        ll5 = view.findViewById(R.id.ll5);
        ll6 = view.findViewById(R.id.ll6);
        ll7 = view.findViewById(R.id.ll7);
        ll8 = view.findViewById(R.id.ll8);
        ll9 = view.findViewById(R.id.ll9);
        ll10 = view.findViewById(R.id.ll10);
        ll11 = view.findViewById(R.id.ll11);
        ll12 = view.findViewById(R.id.ll12);
        ll13 = view.findViewById(R.id.ll13);
        ll14 = view.findViewById(R.id.ll14);

        tv1Pt = view.findViewById(R.id.entertainment_total);
        tv2Pt = view.findViewById(R.id.subscription_total);
        tv3Pt = view.findViewById(R.id.healthcare_total);
        tv4Pt = view.findViewById(R.id.investment_total);
        tv5Pt = view.findViewById(R.id.utilities_total);
        tv6Pt = view.findViewById(R.id.transport_total);
        tv7Pt = view.findViewById(R.id.shopping_total);
        tv8Pt = view.findViewById(R.id.food_total);
        tv9Pt = view.findViewById(R.id.rent_total);
        tv10Pt = view.findViewById(R.id.salary_total);
        tv11Pt = view.findViewById(R.id.award_total);
        tv12Pt = view.findViewById(R.id.stocks_total);
        tv13Pt = view.findViewById(R.id.others_total);
        tv14Pt = view.findViewById(R.id.others_expense_total);

        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        homeRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() != null) {
                    int entertainment = 0, subscription = 0, healthcare = 0, investment = 0, utilities = 0, transport = 0, shopping = 0, food = 0, rent = 0, other = 0;
                    int salary = 0, award = 0, stocks = 0, others = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        expenseModel model = ds.getValue(expenseModel.class);
                        switch (Objects.requireNonNull(model).getCategory()) {
                            case "Entertainment":
                                entertainment += model.getAmount();
                                break;
                            case "Subscription":
                                subscription += model.getAmount();
                                break;
                            case "Health Care":
                                healthcare += model.getAmount();
                                break;
                            case "Investment":
                                investment += model.getAmount();
                                break;
                            case "Utilities":
                                utilities += model.getAmount();
                                break;
                            case "Transport":
                                transport += model.getAmount();
                                break;
                            case "Shopping":
                                shopping += model.getAmount();
                                break;
                            case "Food":
                                food += model.getAmount();
                                break;
                            case "Rent":
                                rent += model.getAmount();
                                break;
                            case "Other":
                                other += model.getAmount();
                                break;
                            case "Salary":
                                salary += model.getAmount();
                                break;
                            case "Award":
                                award += model.getAmount();
                                break;
                            case "Stocks":
                                stocks += model.getAmount();
                                break;
                            case "Others":
                                others += model.getAmount();
                                break;
                        }
                        if (entertainment == 0) {
                            ll1.setVisibility(View.GONE);
                        } else {
                            ll1.setVisibility(View.VISIBLE);
                            tv1Pt.setText("-" + entertainment + currencyAdapter.setUserCurrency());
                            tv1Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (subscription == 0) {
                            ll2.setVisibility(View.GONE);
                        } else {
                            ll2.setVisibility(View.VISIBLE);
                            tv2Pt.setText("-" + subscription + currencyAdapter.setUserCurrency());
                            tv2Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (healthcare == 0) {
                            ll3.setVisibility(View.GONE);
                        } else {
                            ll3.setVisibility(View.VISIBLE);
                            tv3Pt.setText("-" + healthcare + currencyAdapter.setUserCurrency());
                            tv3Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (investment == 0) {
                            ll4.setVisibility(View.GONE);
                        } else {
                            ll4.setVisibility(View.VISIBLE);
                            tv4Pt.setText("-" + investment + currencyAdapter.setUserCurrency());
                            tv4Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (utilities == 0) {
                            ll5.setVisibility(View.GONE);
                        } else {
                            ll5.setVisibility(View.VISIBLE);
                            tv5Pt.setText("-" + utilities + currencyAdapter.setUserCurrency());
                            tv5Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (transport == 0) {
                            ll6.setVisibility(View.GONE);
                        } else {
                            ll6.setVisibility(View.VISIBLE);
                            tv6Pt.setText("-" + transport + currencyAdapter.setUserCurrency());
                            tv6Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (shopping == 0) {
                            ll7.setVisibility(View.GONE);
                        } else {
                            ll7.setVisibility(View.VISIBLE);
                            tv7Pt.setText("-" + shopping + currencyAdapter.setUserCurrency());
                            tv7Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (food == 0) {
                            ll8.setVisibility(View.GONE);
                        } else {
                            ll8.setVisibility(View.VISIBLE);
                            tv8Pt.setText("-" + food + currencyAdapter.setUserCurrency());
                            tv8Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (rent == 0) {
                            ll9.setVisibility(View.GONE);
                        } else {
                            ll9.setVisibility(View.VISIBLE);
                            tv9Pt.setText("-" + rent + currencyAdapter.setUserCurrency());
                            tv9Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (other == 0) {
                            ll14.setVisibility(View.GONE);
                        } else {
                            ll14.setVisibility(View.VISIBLE);
                            tv14Pt.setText("-" + other + currencyAdapter.setUserCurrency());
                            tv14Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (salary == 0) {
                            ll10.setVisibility(View.GONE);
                        } else {
                            ll10.setVisibility(View.VISIBLE);
                            tv10Pt.setText("+" + salary + currencyAdapter.setUserCurrency());
                            tv10Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (award == 0) {
                            ll11.setVisibility(View.GONE);
                        } else {
                            ll11.setVisibility(View.VISIBLE);
                            tv11Pt.setText("+" + award + currencyAdapter.setUserCurrency());
                            tv11Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (stocks == 0) {
                            ll12.setVisibility(View.GONE);
                        } else {
                            ll12.setVisibility(View.VISIBLE);
                            tv12Pt.setText("+" + stocks + currencyAdapter.setUserCurrency());
                            tv12Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }

                        if (others == 0) {
                            ll13.setVisibility(View.GONE);
                        } else {
                            ll13.setVisibility(View.VISIBLE);
                            tv13Pt.setText("+" + others + currencyAdapter.setUserCurrency());
                            tv13Pt.setTextColor(getResources().getColor(R.color.primary_dark));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        expenseBtn.setOnClickListener(v -> homeRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String budget = dataSnapshot.getValue(String.class);
                if (budget == null) {
                    checkBudget();
                } else {
                    addExpense();
                }
                homeRef3.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));

        incomeBtn.setOnClickListener(v -> homeRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String wallet = dataSnapshot.getValue(String.class);
                if (wallet == null) {
                    checkWallet();
                } else if (Integer.parseInt(wallet) < 0) {
                    walletBalance.setText(wallet);
                } else {
                    addIncome();
                }
                homeRef2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));

        return view;
    }

    public void addIncome() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        progressDialog = new ProgressDialog(getContext());

        View myView = inflater.inflate(R.layout.income_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        categoryBtnIncome = myView.findViewById(R.id.category_selector_income);
        categoryBtnIncome.setOnClickListener(v -> showBottomSheetForIncome());

        final EditText amount = myView.findViewById(R.id.amount);
        final EditText notes = myView.findViewById(R.id.note);
        final Button saveBtn = myView.findViewById(R.id.save);
        final Button cancelBtn = myView.findViewById(R.id.cancel);

        saveBtn.setOnClickListener(v -> {
            mAmount = amount.getText().toString();
            mNotes = notes.getText().toString();
            category = categoryBtnIncome.getText().toString();

            if (mAmount.isEmpty()) {
                amount.setError("Enter the amount!");
                return;
            } else if (mNotes.isEmpty()) {
                notes.setError("Enter a note!");
                return;
            } else if (mNotes.matches("[0-9]+")) {
                Toast.makeText(requireActivity(), "Enter a valid note!", Toast.LENGTH_SHORT).show();
            } else if (mAmount.matches("[a-zA-Z]+")) {
                Toast.makeText(requireActivity(), "Enter a valid amount!", Toast.LENGTH_SHORT).show();
            } else if (category.equals("Choose a Category")) {
                Toast.makeText(getContext(), "Select a valid category!", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Adding Income...");
                progressDialog.show();

                String id = ref.push().getKey();

                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                String time = "" + format.format(calendar.getTime());
                if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) {
                    time = "" + (calendar.get(Calendar.HOUR_OF_DAY) - 12) + ":" + calendar.get(Calendar.MINUTE) + " PM";
                } else if (calendar.get(Calendar.MINUTE) < 10) {
                    time = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":0" + calendar.get(Calendar.MINUTE);
                } else {
                    time = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " AM";
                }
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();

                String date = dateFormat.format(cal.getTime());
                if (mAmount.contains(".")) {
                    mAmount = mAmount.substring(0, mAmount.indexOf("."));
                }
                expenseModel data = new expenseModel(category, mNotes, id, date, Integer.parseInt(mAmount), "income", time);
                ref.child(Objects.requireNonNull(id)).setValue(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        homeRef2.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String wallet = dataSnapshot.getValue(String.class);
                                int walletAmount = 0;
                                if (wallet != null) {
                                    walletAmount = Integer.parseInt(wallet);
                                }
                                int income = Integer.parseInt(mAmount);
                                int total = walletAmount + income;
                                homeRef2.setValue(String.valueOf(total));
                                walletBalance.setText("Wallet: " + total + currencyAdapter.setUserCurrency());
                                homeRef2.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(getContext(), "Income added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Task Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            transList = new ArrayList<>();
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    public void addExpense() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        progressDialog = new ProgressDialog(getContext());
        View myView = inflater.inflate(R.layout.expense_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        categoryBtnExpense = myView.findViewById(R.id.category_selector_expense);
        categoryBtnExpense.setOnClickListener(v -> showBottomSheetForExpense());

        final EditText amount = myView.findViewById(R.id.amount);
        final EditText notes = myView.findViewById(R.id.note);
        final Button saveBtn = myView.findViewById(R.id.save);
        final Button cancelBtn = myView.findViewById(R.id.cancel);

        saveBtn.setOnClickListener(v -> {
            mAmount = amount.getText().toString();
            mNotes = notes.getText().toString();
            category = categoryBtnExpense.getText().toString();
            if (mAmount.isEmpty()) {
                amount.setError("Enter the amount!");
                return;
            } else if (mNotes.isEmpty()) {
                notes.setError("Enter a note!");
                return;
            } else if (mNotes.matches("[0-9]+")) {
                Toast.makeText(requireActivity(), "Enter a valid note!", Toast.LENGTH_SHORT).show();
            } else if (mAmount.matches("[a-zA-Z]+")) {
                Toast.makeText(requireActivity(), "Enter a valid amount!", Toast.LENGTH_SHORT).show();
            } else if (category.equals("Choose a Category")) {
                Toast.makeText(getContext(), "Select a valid category!", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Adding Expense...");
                progressDialog.show();
                String id = ref.push().getKey();

                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                String time = timeFormat.format(calendar.getTime());

                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());
                if (mAmount.contains(".")) {
                    mAmount = mAmount.substring(0, mAmount.indexOf("."));
                }
                expenseModel data = new expenseModel(category, mNotes, id, date, Integer.parseInt(mAmount), "expense", time);
                ref.child(Objects.requireNonNull(id)).setValue(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        homeRef2.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String wallet = dataSnapshot.getValue(String.class);

                                int walletAmount = 0;
                                if (wallet != null) {
                                    walletAmount = Integer.parseInt(wallet);
                                }
                                int expense = Integer.parseInt(mAmount);
                                int total = walletAmount - expense;
                                homeRef2.setValue(String.valueOf(total));
                                if (total <= 0) {
                                    homeRef2.setValue(String.valueOf(total));
                                    walletBalance.setText("Wallet: " + total + currencyAdapter.setUserCurrency());
                                    Toast.makeText(getContext(), "Add more money to your wallet!", Toast.LENGTH_SHORT).show();
                                    homeRef2.removeEventListener(this);
                                } else {
                                    homeRef2.setValue(String.valueOf(total));
                                    walletBalance.setText("Wallet: " + total + currencyAdapter.setUserCurrency());
                                    homeRef2.removeEventListener(this);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        homeRef3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String budget = snapshot.getValue(String.class);
                                if (getContext() != null) {
                                    if (budget == null) {
                                        budget = "0";
                                    } else if (Integer.parseInt(Objects.requireNonNull(budget)) < totalExpense) {
                                        homeBudget.setText("Budget Exceeded!");
                                        homeBudget.setTextColor(requireContext().getResources().getColor(R.color.red));
                                    } else if (Integer.parseInt(budget) * 0.8 <= totalExpense && Integer.parseInt(budget) * 0.9 >= totalExpense) {
                                        homeBudget.setTextColor(requireContext().getResources().getColor(R.color.warning));
                                        Toast.makeText(requireContext(), "80% Budget Limit Reached!", Toast.LENGTH_LONG).show();
                                    } else if (Integer.parseInt(budget) * 0.5 <= totalExpense) {
                                        homeBudget.setTextColor(requireContext().getResources().getColor(R.color.warning));
                                        Toast.makeText(requireContext(), "50% Budget Limit Reached!", Toast.LENGTH_LONG).show();
                                    } else {
                                        homeBudget.setTextColor(requireContext().getResources().getColor(R.color.green));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(getContext(), "Expense Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Task Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            transList = new ArrayList<>();
            homeRef.addValueEventListener(new ValueEventListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int totalAmount = 0;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) d.getValue();
                        Object total = Objects.requireNonNull(map).get("amount");
                        Object type = map.get("type");
                        if (String.valueOf(type).equals("expense")) {
                            int pTotal = Integer.parseInt(String.valueOf(total));
                            totalAmount += pTotal;
                        }
                        int finalTotalAmount = totalAmount;
                        homeRef3.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String budget = snapshot.getValue(String.class);
                                if (budget == null) {
                                    budget = "0";
                                } else if (finalTotalAmount > Integer.parseInt(Objects.requireNonNull(budget))) {
                                    if (getContext() != null) {
                                        showBudgetExceeded();
                                        expenseBtn.setEnabled(false);
                                    }
                                }
                                homeRef3.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            transList.clear();
            dialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void showBottomSheetForExpense() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_expense_dialog);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        LinearLayout fEntertainment = bottomSheetDialog.findViewById(R.id.entertainment_layout);
        LinearLayout fSubscription = bottomSheetDialog.findViewById(R.id.subscription_layout);
        LinearLayout fHealth = bottomSheetDialog.findViewById(R.id.health_layout);
        LinearLayout fInvestment = bottomSheetDialog.findViewById(R.id.investment_layout);
        LinearLayout fUtilities = bottomSheetDialog.findViewById(R.id.utilities_layout);
        LinearLayout fTransport = bottomSheetDialog.findViewById(R.id.transport_layout);
        LinearLayout fShopping = bottomSheetDialog.findViewById(R.id.shopping_layout);
        LinearLayout fFood = bottomSheetDialog.findViewById(R.id.food_layout);
        LinearLayout fRent = bottomSheetDialog.findViewById(R.id.rent_layout);
        LinearLayout fOthers = bottomSheetDialog.findViewById(R.id.other_layout);

        bottomSheetDialog.show();

        if (fEntertainment != null) {
            fEntertainment.setOnClickListener(v -> {
                categoryBtnExpense.setText("Entertainment");
                bottomSheetDialog.dismiss();
            });
        }
        if (fSubscription != null) {
            fSubscription.setOnClickListener(v -> {
                categoryBtnExpense.setText("Subscription");
                bottomSheetDialog.dismiss();
            });
        }
        if (fHealth != null) {
            fHealth.setOnClickListener(v -> {
                categoryBtnExpense.setText("Health Care");
                bottomSheetDialog.dismiss();
            });
        }
        if (fInvestment != null) {
            fInvestment.setOnClickListener(v -> {
                categoryBtnExpense.setText("Investment");
                bottomSheetDialog.dismiss();
            });
        }
        if (fUtilities != null) {
            fUtilities.setOnClickListener(v -> {
                categoryBtnExpense.setText("Utilities");
                bottomSheetDialog.dismiss();
            });
        }
        if (fTransport != null) {
            fTransport.setOnClickListener(v -> {
                categoryBtnExpense.setText("Transport");
                bottomSheetDialog.dismiss();
            });
        }
        if (fShopping != null) {
            fShopping.setOnClickListener(v -> {
                categoryBtnExpense.setText("Shopping");
                bottomSheetDialog.dismiss();
            });
        }
        if (fFood != null) {
            fFood.setOnClickListener(v -> {
                categoryBtnExpense.setText("Food");
                bottomSheetDialog.dismiss();
            });
        }
        if (fRent != null) {
            fRent.setOnClickListener(v -> {
                categoryBtnExpense.setText("Rent");
                bottomSheetDialog.dismiss();
            });
        }
        if (fOthers != null) {
            fOthers.setOnClickListener(v -> {
                categoryBtnExpense.setText("Other");
                bottomSheetDialog.dismiss();
            });
        }
    }

    @SuppressLint("SetTextI18n")
    public void showBottomSheetForIncome() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_income_dialog);

        LinearLayout fSalary = bottomSheetDialog.findViewById(R.id.salary_layout);
        LinearLayout fAward = bottomSheetDialog.findViewById(R.id.award_layout);
        LinearLayout fStocks = bottomSheetDialog.findViewById(R.id.stocks_layout);
        LinearLayout fOthers = bottomSheetDialog.findViewById(R.id.others_layout);

        bottomSheetDialog.show();

        if (fSalary != null) {
            fSalary.setOnClickListener(v -> {
                categoryBtnIncome.setText("Salary");
                bottomSheetDialog.dismiss();
            });
        }
        if (fAward != null) {
            fAward.setOnClickListener(v -> {
                categoryBtnIncome.setText("Award");
                bottomSheetDialog.dismiss();
            });
        }
        if (fStocks != null) {
            fStocks.setOnClickListener(v -> {
                categoryBtnIncome.setText("Stocks");
                bottomSheetDialog.dismiss();
            });
        }
        if (fOthers != null) {
            fOthers.setOnClickListener(v -> {
                categoryBtnIncome.setText("Others");
                bottomSheetDialog.dismiss();
            });
        }
    }

    public void checkBudget() {
        homeRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String budget = dataSnapshot.getValue(String.class);
                if (budget == null) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> showBudgetAlert());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkWallet() {
        homeRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String wallet = dataSnapshot.getValue(String.class);
                if (wallet == null) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> showWalletAlert());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getTransactions() {
        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        homeRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    expenseModel p = d.getValue(expenseModel.class);
                    if (p == null) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                homeLayout.setVisibility(View.VISIBLE);
                                recTrans.setText("");
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                homeLayout.setVisibility(View.GONE);
                                recTrans.setText("Recent Transactions 💵");
                                transList.add(p);
                            });
                        }
                    }
                }
                if (transList.size() > 0) {
                    expenseAdapter adapter = new expenseAdapter(getContext(), (ArrayList<expenseModel>) transList);
                    listView.setAdapter(adapter);
                    Collections.reverse(transList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getWalletBalance() {
        database2 = FirebaseDatabase.getInstance();
        homeRef2 = database2.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("UserBank").child("Wallet");
        homeRef2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currencyAdapter.setUserCurrency();
                String wallet = dataSnapshot.getValue(String.class);
                if (wallet == null) {
                    walletBalance.setText("Wallet: 0" + currencyAdapter.setUserCurrency());
                } else {
                    walletBalance.setText("Wallet: " + wallet + currencyAdapter.setUserCurrency());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getBudgetBalance() {
        database3 = FirebaseDatabase.getInstance();
        homeRef3 = database3.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("UserBank").child("Budget");
        homeRef3.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currencyAdapter.setUserCurrency();
                String budget = dataSnapshot.getValue(String.class);
                if (budget == null) {
                    homeBudget.setText("Budget: 0" + currencyAdapter.setUserCurrency());
                } else {
                    if (getContext() != null) {
                        homeBudget.setText("Budget: " + budget + currencyAdapter.setUserCurrency());
                        homeRef.addValueEventListener(new ValueEventListener() {
                            @SuppressWarnings("unchecked")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                totalExpense = 0;
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
                                    }
                                }
                                homeRef3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String budget = snapshot.getValue(String.class);
                                        if (getContext() != null) {
                                            if (budget == null) {
                                                budget = "0";
                                            } else if (Integer.parseInt(Objects.requireNonNull(budget)) < totalExpense) {
                                                homeBudget.setText("Budget Exceeded!");
                                                homeBudget.setTextColor(requireContext().getResources().getColor(R.color.red));
                                            } else if (Integer.parseInt(budget) * 0.8 <= totalExpense && Integer.parseInt(budget) * 0.9 >= totalExpense) {
                                                homeBudget.setTextColor(requireContext().getResources().getColor(R.color.warning));
                                            } else if (Integer.parseInt(budget) * 0.5 <= totalExpense) {
                                                homeBudget.setTextColor(requireContext().getResources().getColor(R.color.warning));
                                            } else {
                                                homeBudget.setTextColor(requireContext().getResources().getColor(R.color.green));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showWalletAlert() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.custom_wallet_warning);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
        walletNullAvatar = bottomSheetDialog.findViewById(R.id.walletNullAvatar);
        setImageResource(walletNullAvatar, R.drawable.image23, R.drawable.image24, R.drawable.image25, R.drawable.image26, R.drawable.image27, R.drawable.image28);
    }

    public void showBudgetAlert() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.custom_budget_warning);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
        budgetNullAvatar = bottomSheetDialog.findViewById(R.id.budgetNullAvatar);
        setImageResource(budgetNullAvatar, R.drawable.image29, R.drawable.image30, R.drawable.image31, R.drawable.image32, R.drawable.image33, R.drawable.image34);
    }

    public void showBudgetExceeded() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.custom_alert_budget_exceeded);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
        budgetExdAvatar = bottomSheetDialog.findViewById(R.id.budgetExdImg);
        setImageResource(budgetExdAvatar, R.drawable.image12, R.drawable.image15, R.drawable.image11, R.drawable.image16, R.drawable.image13, R.drawable.image14);
    }

    public void setImageResource(ImageView imageView, int imgRsc1, int imgRsc2, int imgRsc3, int imgRsc4, int imgRsc5, int imgRsc6) {
        database4 = FirebaseDatabase.getInstance();
        homeRef4 = database4.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("avatar");
        homeRef4.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer avatar = dataSnapshot.getValue(Integer.class);
                if (avatar == null) {
                    imageView.setImageResource(imgRsc1);
                } else if (avatar == 1) {
                    imageView.setImageResource(imgRsc2);
                } else if (avatar == 2) {
                    imageView.setImageResource(imgRsc3);
                } else if (avatar == 3) {
                    imageView.setImageResource(imgRsc4);
                } else if (avatar == 4) {
                    imageView.setImageResource(imgRsc5);
                } else if (avatar == 5) {
                    imageView.setImageResource(imgRsc6);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}