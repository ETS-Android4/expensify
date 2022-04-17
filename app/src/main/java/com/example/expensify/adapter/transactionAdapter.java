package com.example.expensify.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.expensify.R;
import com.example.expensify.model.expenseModel;
import com.example.expensify.userDashboard;
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
import java.util.Objects;

public class transactionAdapter extends BaseAdapter {
    public int newWallet;
    public String wallet;
    Context context;
    ArrayList<expenseModel> arrayList;
    TextView homeCategory, homeAmount, homeNote, homeDate, trans_time;
    private String postid;
    private String note;
    private int amount;
    private String category;
    private String type;
    private ProgressDialog progressDialog;

    public transactionAdapter(Context context, ArrayList<expenseModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.retrieve_layout, parent, false);
        }

        ImageView imageView;
        CardView cardView;
        final expenseModel data = arrayList.get(position);

        homeCategory = convertView.findViewById(R.id.transaction_category);
        homeAmount = convertView.findViewById(R.id.transaction_amount);
        homeNote = convertView.findViewById(R.id.transaction_note);
        homeDate = convertView.findViewById(R.id.transaction_date);
        cardView = convertView.findViewById(R.id.cardView);

        imageView = convertView.findViewById(R.id.imageView);
        trans_time = convertView.findViewById(R.id.trans_time);

        if (data.getType().equals("expense")) {
            homeCategory.setText(arrayList.get(position).getCategory());
            homeAmount.setText("-" + arrayList.get(position).getAmount() + currencyAdapter.setUserCurrency());
            homeAmount.setTextColor(context.getResources().getColor(R.color.red));
            homeNote.setText("Note: " + arrayList.get(position).getNote());
            homeDate.setText("Date: " + arrayList.get(position).getDate());
            trans_time.setText("" + arrayList.get(position).getTime());
            trans_time.setTextColor(Color.parseColor("#6200EE"));
        } else {
            homeCategory.setText(arrayList.get(position).getCategory());
            homeAmount.setText("+" + arrayList.get(position).getAmount() + currencyAdapter.setUserCurrency());
            homeAmount.setTextColor(context.getResources().getColor(R.color.green));
            homeNote.setText("Note: " + arrayList.get(position).getNote());
            homeDate.setText("Date: " + arrayList.get(position).getDate());
            trans_time.setText("" + arrayList.get(position).getTime());
            trans_time.setTextColor(Color.parseColor("#6200EE"));
        }

        switch (data.getType()) {
            case "income":
                switch (data.getCategory()) {
                    case "Salary":
                        imageView.setImageResource(R.drawable.salary);
                        break;
                    case "Stocks":
                        imageView.setImageResource(R.drawable.stocks);
                        break;
                    case "Others":
                        imageView.setImageResource(R.drawable.others);
                        break;
                    case "Award":
                        imageView.setImageResource(R.drawable.award);
                        break;
                }
                break;
            case "expense":
                switch (data.getCategory()) {
                    case "Food":
                        imageView.setImageResource(R.drawable.food);
                        break;
                    case "Transport":
                        imageView.setImageResource(R.drawable.transport);
                        break;
                    case "Shopping":
                        imageView.setImageResource(R.drawable.shopping);
                        break;
                    case "Utilities":
                        imageView.setImageResource(R.drawable.utilities);
                        break;
                    case "Entertainment":
                        imageView.setImageResource(R.drawable.entertainment);
                        break;
                    case "Investment":
                        imageView.setImageResource(R.drawable.investment);
                        break;
                    case "Subscription":
                        imageView.setImageResource(R.drawable.subscription);
                        break;
                    case "Health Care":
                        imageView.setImageResource(R.drawable.healthcare);
                        break;
                    case "Rent":
                        imageView.setImageResource(R.drawable.rent);
                }
                break;
            default:
                break;
        }

        cardView.setOnClickListener(v -> {
            postid = data.getId();
            note = data.getNote();
            amount = data.getAmount();
            category = data.getCategory();
            type = data.getType();
            updateData();
        });
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.update_delete_layout, null);
        progressDialog = new ProgressDialog(context);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItem = myView.findViewById(R.id.item);
        final EditText mAmount = myView.findViewById(R.id.amount);
        final EditText mNote = myView.findViewById(R.id.note);

        mItem.setText(category);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        mNote.setText(note);
        mNote.setSelection(note.length());

        Button updateBtn = myView.findViewById(R.id.update);
        Button deleteBtn = myView.findViewById(R.id.delete);

        updateBtn.setOnClickListener(view -> {
            TextView homeAmount, homeNote;

            homeAmount = ((Activity) context).findViewById(R.id.transaction_amount);
            homeNote = ((Activity) context).findViewById(R.id.transaction_note);
            amount = Integer.parseInt(mAmount.getText().toString());
            note = mNote.getText().toString();

            homeAmount.setText("Amount: " + amount + currencyAdapter.setUserCurrency());
            homeNote.setText("Note: " + note);

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = Calendar.getInstance();
            String date = dateFormat.format(cal.getTime());

            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String time = "" + format.format(calendar.getTime());
            if (calendar.get(Calendar.HOUR_OF_DAY) > 12) {
                time = "" + (calendar.get(Calendar.HOUR_OF_DAY) - 12) + ":" + calendar.get(Calendar.MINUTE) + " PM";
            } else if (calendar.get(Calendar.MINUTE) < 10) {
                time = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":0" + calendar.get(Calendar.MINUTE);
            } else {
                time = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " AM";
            }

            expenseModel data = new expenseModel(category, note, postid, date, amount, type, time);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Transactions");
            progressDialog.setMessage("Updating...");
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (context != null) {
                        progressDialog.dismiss();
                        int originalAmount = dataSnapshot.child(postid).child("amount").getValue(Integer.class);
                        int updatedAmount = amount;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").
                                child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .child("UserBank").child("Wallet");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                wallet = dataSnapshot.getValue(String.class);
                                if (type.equals("expense")) {
                                    if (originalAmount > updatedAmount) {
                                        newWallet = Integer.parseInt(Objects.requireNonNull(wallet)) + (originalAmount - updatedAmount);
                                    } else {
                                        newWallet = Integer.parseInt(Objects.requireNonNull(wallet)) - (updatedAmount - originalAmount);
                                    }
                                } else {
                                    if (originalAmount > updatedAmount) {
                                        newWallet = Integer.parseInt(Objects.requireNonNull(wallet)) - (originalAmount - updatedAmount);
                                    } else {
                                        newWallet = Integer.parseInt(Objects.requireNonNull(wallet)) + (updatedAmount - originalAmount);
                                    }
                                }
                                reference.child(postid).setValue(data);
                                ref.setValue(String.valueOf(newWallet));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        ref.removeEventListener(this);
                    }
                    reference.removeEventListener(this);
                    Intent intent = new Intent(context, userDashboard.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            dialog.dismiss();
        });

        deleteBtn.setOnClickListener(view -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .child("Transactions");
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
            reference.child(postid).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (this.context != null) {
                        progressDialog.dismiss();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("UserBank").child("Wallet");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String wallet = dataSnapshot.getValue(String.class);
                                int newWallet = 0;
                                if (type.equals("expense")) {
                                    newWallet = Integer.parseInt(Objects.requireNonNull(wallet)) + amount;
                                    ref.setValue(String.valueOf(newWallet));
                                }
                                if (type.equals("income")) {
                                    newWallet = Integer.parseInt(Objects.requireNonNull(wallet)) - amount;
                                    ref.setValue(String.valueOf(newWallet));
                                }
                                Intent intent = new Intent(context, userDashboard.class);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });
        dialog.show();
    }
}