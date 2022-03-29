package com.example.expensify.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensify.R;
import com.example.expensify.userDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class currencyAdapter extends AppCompatActivity {
    private static final String JSON_URL = "https://run.mocky.io/v3/41c02396-0c0f-4ff6-a07b-b1072e51d855";
    public static String currency;
    public static FirebaseDatabase database2;
    public static DatabaseReference myRef2;
    public ListView listView;
    public ArrayList<HashMap<String, String>> currencyList;
    public String name, currencySymbol, currencyCode;
    public TextView currentCurrency;
    FirebaseAuth mAuth;
    FirebaseDatabase database, currDB;
    DatabaseReference myRef, currRef;
    List<String> searchList;
    private ProgressDialog progressDialog;

    public static String setUserCurrency() {
        database2 = FirebaseDatabase.getInstance();
        myRef2 = database2.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("currency");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currency = dataSnapshot.getValue(String.class);
                myRef2.setValue(currency);
                myRef2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (currency == null) return "$";
        else if (currency.length() == 3) {
            currency = " " + currency.substring(0, 3);
        } else if (currency.length() == 4) {
            currency = " " + currency.substring(0, 4);
        }
        return currency;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        setContentView(R.layout.activity_currency_list);
        listView = findViewById(R.id.currencyList);
        currentCurrency = findViewById(R.id.currentCurrency);
        currencyList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        GetData getData = new GetData();
        getData.execute();

        currDB = FirebaseDatabase.getInstance();
        currRef = currDB.getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("currency");
        currRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currency = dataSnapshot.getValue(String.class);
                if (currency == null) {
                    currentCurrency.setText("Your current Selection: $");
                } else {
                    currentCurrency.setText("Your current Selection: " + currency);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getApplicationContext(), "Currency Changed to " + currencyList.get(position).get("name"), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), userDashboard.class);
            startActivity(i);
            currency = currencyList.get(position).get("symbol");
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Users");
            myRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("currency").setValue(currency);
        });
    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(currencyAdapter.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder current = new StringBuilder();
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    int data = isr.read();
                    while (data != -1) {
                        current.append((char) data);
                        data = isr.read();
                    }
                    return current.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("Currency");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    name = jsonObject1.getString("name");
                    currencySymbol = jsonObject1.getString("symbol");
                    currencyCode = jsonObject1.getString("code");
                    HashMap<String, String> currency = new HashMap<>();
                    currency.put("name", name);
                    currency.put("symbol", currencySymbol);
                    currency.put("code", currencyCode);
                    currencyList.add(currency);
                }

                searchList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject e = jsonArray.getJSONObject(i);
                    name = e.getString("name");
                    searchList.add(name);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, searchList);
                AutoCompleteTextView textView = findViewById(R.id.atv);
                textView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                textView.setOnItemClickListener((parent, view, position, id) -> {
                    String selected = (String) parent.getItemAtPosition(position);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject e = null;
                        try {
                            e = jsonArray.getJSONObject(i);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        try {
                            if (e != null && e.getString("name").equals(selected)) {
                                currencySymbol = e.getString("symbol");
                                currencyCode = e.getString("code");
                                break;
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                    myRef2.setValue(currencySymbol);
                    Toast.makeText(getApplicationContext(), "Currency Changed to " + selected, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), userDashboard.class);
                    startActivity(intent);
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(), currencyList,
                    R.layout.currency_listview,
                    new String[]{"name", "symbol", "code"},
                    new int[]{R.id.curr_name, R.id.curr_symbol, R.id.curr_code});
            listView.setAdapter(adapter);
        }
    }
}