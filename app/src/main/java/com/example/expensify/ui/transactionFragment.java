package com.example.expensify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.expensify.R;
import com.example.expensify.adapter.transactionAdapter;
import com.example.expensify.model.expenseModel;
import com.example.expensify.userDashboard;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class transactionFragment extends Fragment {

    public List<expenseModel> transList;
    public android.app.AlertDialog filterDialog;
    public TextView upDel;
    public ConstraintLayout transLayout;
    public Button filterTrans, deleteTrans, filterExpense, filterIncome, expenseCategory, incomeCategory;
    ListView listView;
    FirebaseDatabase database, userBankDB;
    DatabaseReference homeRef, userBankRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        transLayout = view.findViewById(R.id.nullTransLayout);

        filterTrans = view.findViewById(R.id.filter_transaction);
        deleteTrans = view.findViewById(R.id.delete_transaction);

        upDel = view.findViewById(R.id.upDel);

        filterTrans.setOnClickListener(v -> alertFilterTransactions());
        deleteTrans.setOnClickListener(v -> alertDeleteTransactions());

        database = FirebaseDatabase.getInstance();
        homeRef = database.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");

        userBankDB = FirebaseDatabase.getInstance();
        userBankRef = userBankDB.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("UserBank");

        transList = new ArrayList<>();
        listView = view.findViewById(R.id.listview);
        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    expenseModel p = d.getValue(expenseModel.class);
                    if (p == null) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                transLayout.setVisibility(View.VISIBLE);
                                upDel.setText("");
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                transLayout.setVisibility(View.GONE);
                                upDel.setText("Click on a Transaction card to Update or Delete your transaction ✌️");
                                transList.add(p);
                            });
                        }
                    }
                }
                if (transList.size() > 0) {
                    transactionAdapter adapter = new transactionAdapter(getContext(), (ArrayList<expenseModel>) transList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                homeRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void alertDeleteTransactions() {
        android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(getContext());
        myDialog.setPositiveButton("Delete", (dialog, which) -> homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() != null) {
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(getContext(), "No Transactions to Delete", Toast.LENGTH_SHORT).show();
                    } else {
                        homeRef.removeValue();
                        userBankRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    Toast.makeText(getContext(), "No Transactions to Delete", Toast.LENGTH_SHORT).show();
                                } else {
                                    userBankRef.removeValue();
                                }
                                userBankRef.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Intent i = new Intent(getContext(), userDashboard.class);
                        startActivity(i);
                        Toast.makeText(getContext(), "All Transactions Deleted!", Toast.LENGTH_SHORT).show();
                    }
                    homeRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        }));
        myDialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View myView = inflater.inflate(R.layout.custom_delete_transactions, null);
        myDialog.setView(myView);
        final android.app.AlertDialog dialog = myDialog.create();
        dialog.show();
    }

    private void alertFilterTransactions() {
        android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View myView = inflater.inflate(R.layout.custom_filter_transaction, null);
        myDialog.setView(myView);
        filterDialog = myDialog.create();
        filterDialog.show();

        filterExpense = myView.findViewById(R.id.filter_expense);
        filterIncome = myView.findViewById(R.id.filter_income);
        expenseCategory = myView.findViewById(R.id.expense_category);
        incomeCategory = myView.findViewById(R.id.income_category);

        filterExpense.setOnClickListener(v -> filterType("expense"));
        filterIncome.setOnClickListener(v -> filterType("income"));
        expenseCategory.setOnClickListener(v -> showBottomSheetExpenseDialog());
        incomeCategory.setOnClickListener(v -> showBottomSheetIncomeDialog());
    }

    private void showBottomSheetExpenseDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_expense_dialog);

        LinearLayout fEntertainment = bottomSheetDialog.findViewById(R.id.entertainment_layout);
        LinearLayout fSubscription = bottomSheetDialog.findViewById(R.id.subscription_layout);
        LinearLayout fHealth = bottomSheetDialog.findViewById(R.id.health_layout);
        LinearLayout fInvestment = bottomSheetDialog.findViewById(R.id.investment_layout);
        LinearLayout fUtilities = bottomSheetDialog.findViewById(R.id.utilities_layout);
        LinearLayout fTransport = bottomSheetDialog.findViewById(R.id.transport_layout);
        LinearLayout fShopping = bottomSheetDialog.findViewById(R.id.shopping_layout);
        LinearLayout fFood = bottomSheetDialog.findViewById(R.id.food_layout);
        LinearLayout fRent = bottomSheetDialog.findViewById(R.id.rent_layout);

        bottomSheetDialog.show();

        if (fEntertainment != null) {
            fEntertainment.setOnClickListener(v -> {
                filterCategory("Entertainment");
                bottomSheetDialog.dismiss();
            });
        }

        if (fSubscription != null) {
            fSubscription.setOnClickListener(v -> {
                filterCategory("Subscription");
                bottomSheetDialog.dismiss();
            });
        }

        if (fHealth != null) {
            fHealth.setOnClickListener(v -> {
                filterCategory("Health Care");
                bottomSheetDialog.dismiss();
            });
        }

        if (fInvestment != null) {
            fInvestment.setOnClickListener(v -> {
                filterCategory("Investment");
                bottomSheetDialog.dismiss();
            });
        }

        if (fUtilities != null) {
            fUtilities.setOnClickListener(v -> {
                filterCategory("Utilities");
                bottomSheetDialog.dismiss();
            });
        }

        if (fTransport != null) {
            fTransport.setOnClickListener(v -> {
                filterCategory("Transport");
                bottomSheetDialog.dismiss();
            });
        }

        if (fShopping != null) {
            fShopping.setOnClickListener(v -> {
                filterCategory("Shopping");
                bottomSheetDialog.dismiss();
            });
        }

        if (fFood != null) {
            fFood.setOnClickListener(v -> {
                filterCategory("Food");
                bottomSheetDialog.dismiss();
            });
        }

        if (fRent != null) {
            fRent.setOnClickListener(v -> {
                filterCategory("Rent");
                bottomSheetDialog.dismiss();
            });
        }
    }

    private void showBottomSheetIncomeDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_income_dialog);

        LinearLayout fSalary = bottomSheetDialog.findViewById(R.id.salary_layout);
        LinearLayout fAward = bottomSheetDialog.findViewById(R.id.award_layout);
        LinearLayout fStocks = bottomSheetDialog.findViewById(R.id.stocks_layout);
        LinearLayout fOthers = bottomSheetDialog.findViewById(R.id.others_layout);

        bottomSheetDialog.show();

        if (fSalary != null) {
            fSalary.setOnClickListener(v -> {
                filterCategory("Salary");
                bottomSheetDialog.dismiss();
            });
        }

        if (fAward != null) {
            fAward.setOnClickListener(v -> {
                filterCategory("Award");
                bottomSheetDialog.dismiss();
            });
        }

        if (fStocks != null) {
            fStocks.setOnClickListener(v -> {
                filterCategory("Stocks");
            });
        }

        if (fOthers != null) {
            fOthers.setOnClickListener(v -> {
                filterCategory("Others");
                bottomSheetDialog.dismiss();
            });
        }
    }

    public void filterType(String type) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getContext(), "No Transactions to Filter", Toast.LENGTH_SHORT).show();
                } else {
                    if (getContext() != null) {
                        ArrayList<expenseModel> list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            expenseModel p = ds.getValue(expenseModel.class);
                            if (Objects.requireNonNull(p).getType().equals(type)) {
                                list.add(p);
                                filterDialog.dismiss();
                            }
                        }
                        if (list.size() > 0) {
                            transactionAdapter adapter = new transactionAdapter(getContext(), list);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No Transactions to Filter", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void filterCategory(String category) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(getContext(), "No Transactions to Filter", Toast.LENGTH_SHORT).show();
                } else {
                    if (getContext() != null) {
                        ArrayList<expenseModel> list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            expenseModel p = ds.getValue(expenseModel.class);
                            if (Objects.requireNonNull(p).getCategory().equals(category)) {
                                list.add(p);
                                filterDialog.dismiss();
                            }
                        }
                        if (list.size() > 0) {
                            transactionAdapter adapter = new transactionAdapter(getContext(), list);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No " + category + " Transactions to filter", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}