package com.apify.apifyrealtimecurrencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> CURRENCY_CODE,COUNTRY_NAME,CURRENCY_NAME,CURRENCY_SYMBOL,URL;
    AutoCompleteTextView code1,code2,symbol1,symbol2,name1,name2,currency1,currency2;
    EditText editText1,editText2;
    String st1="";
    String st2="";
    AppCompatButton pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout=findViewById(R.id.show2);
        LinearLayout layout2=findViewById(R.id.show1);
        layout.setVisibility(View.VISIBLE);
        code1=findViewById(R.id.code1);
        symbol1=findViewById(R.id.symbol1);
        name1=findViewById(R.id.name1);
        currency1=findViewById(R.id.currency1);
        code2=findViewById(R.id.code2);
        symbol2=findViewById(R.id.symbol2);
        name2=findViewById(R.id.name2);
        currency2=findViewById(R.id.currency2);
        editText1=findViewById(R.id.input1);
        editText2=findViewById(R.id.input2);
        pressed=findViewById(R.id.pressed);
        CURRENCY_CODE=new ArrayList<>();
        COUNTRY_NAME=new ArrayList<>();
        CURRENCY_NAME=new ArrayList<>();
        CURRENCY_SYMBOL=new ArrayList<>();
        URL=new ArrayList<>();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance().getReference().getDatabase();
        DatabaseReference bds= firebaseDatabase.getReference("CONVERSION DATA");
        bds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //fething real time datas from my firebase as I used 2016 currency datas which can lead to false results some times
                CURRENCY_CODE=(ArrayList<String>) snapshot.child("CURRENCY CODE").getValue();
                COUNTRY_NAME=(ArrayList<String>) snapshot.child("COUNTRY NAME").getValue();
                CURRENCY_NAME=(ArrayList<String>) snapshot.child("CURRENCY NAME").getValue();
                CURRENCY_SYMBOL=(ArrayList<String>) snapshot.child("SYMBOLS").getValue();
                URL =(ArrayList<String>) snapshot.child("API LINK").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "CANT CONNECT TO SERVER\nPLEASE MAKE SURE YOU ARE CONNECTED TO INTERNET OR CONTACT US FOR FURTHER HELP", Toast.LENGTH_SHORT).show();
            }
        });
        //using delayer as I noticed a issue that this code without postdelayed worked before the data were even fetched from which created false results
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CURRENCY_CODE.size()!=0){
                    layout2.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    code1.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,CURRENCY_CODE));
                    symbol1.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,CURRENCY_SYMBOL));
                    name1.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,COUNTRY_NAME));
                    currency1.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,CURRENCY_NAME));
                    code2.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,CURRENCY_CODE));
                    symbol2.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,CURRENCY_SYMBOL));
                    name2.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,COUNTRY_NAME));
                    currency2.setAdapter(new ArrayAdapter<>(MainActivity.this, com.androidnetworking.R.layout.support_simple_spinner_dropdown_item,CURRENCY_NAME));
                    code1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < CURRENCY_CODE.size(); i++) {
                                if (CURRENCY_CODE.get(i).equals(st)){
                                    symbol1.setText(CURRENCY_SYMBOL.get(i));
                                    name1.setText(COUNTRY_NAME.get(i));
                                    currency1.setText(CURRENCY_NAME.get(i));
                                }
                            }
                        }
                    });
                    name1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < COUNTRY_NAME.size(); i++) {
                                if (COUNTRY_NAME.get(i).equals(st)){
                                    symbol1.setText(CURRENCY_SYMBOL.get(i));
                                    code1.setText(CURRENCY_CODE.get(i));
                                    currency1.setText(CURRENCY_NAME.get(i));
                                }
                            }
                        }
                    });
                    currency1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < CURRENCY_NAME.size(); i++) {
                                if (CURRENCY_NAME.get(i).equals(st)){
                                    symbol1.setText(CURRENCY_SYMBOL.get(i));
                                    name1.setText(COUNTRY_NAME.get(i));
                                    code1.setText(CURRENCY_CODE.get(i));
                                }
                            }
                        }
                    });
                    symbol1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < CURRENCY_SYMBOL.size(); i++) {
                                if (CURRENCY_SYMBOL.get(i).equals(st)){
                                    code1.setText(CURRENCY_CODE.get(i));
                                    name1.setText(COUNTRY_NAME.get(i));
                                    currency1.setText(CURRENCY_NAME.get(i));
                                }
                            }
                        }
                    });




                    code2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < CURRENCY_CODE.size(); i++) {
                                if (CURRENCY_CODE.get(i).equals(st)){
                                    symbol2.setText(CURRENCY_SYMBOL.get(i));
                                    name2.setText(COUNTRY_NAME.get(i));
                                    currency2.setText(CURRENCY_NAME.get(i));
                                }
                            }
                        }
                    });
                    name2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < COUNTRY_NAME.size(); i++) {
                                if (COUNTRY_NAME.get(i).equals(st)){
                                    symbol2.setText(CURRENCY_SYMBOL.get(i));
                                    code2.setText(CURRENCY_CODE.get(i));
                                    currency2.setText(CURRENCY_NAME.get(i));
                                }
                            }
                        }
                    });
                    currency2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < CURRENCY_NAME.size(); i++) {
                                if (CURRENCY_NAME.get(i).equals(st)){
                                    symbol2.setText(CURRENCY_SYMBOL.get(i));
                                    name2.setText(COUNTRY_NAME.get(i));
                                    code2.setText(CURRENCY_CODE.get(i));
                                }
                            }
                        }
                    });
                    symbol2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String  st= (String) parent.getAdapter().getItem(position);
                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < CURRENCY_SYMBOL.size(); i++) {
                                if (CURRENCY_SYMBOL.get(i).equals(st)){
                                    code2.setText(CURRENCY_CODE.get(i));
                                    name2.setText(COUNTRY_NAME.get(i));
                                    currency2.setText(CURRENCY_NAME.get(i));
                                }
                            }
                        }
                    });
                    pressed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!code1.getText().toString().equals("") && !code2.getText().toString().equals("")){
                                AndroidNetworking.initialize(MainActivity.this);
                                AndroidNetworking.get(URL.get(0).toString().replace("+as-","https://").replace("+2b-","/"))
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    JSONObject rates=(JSONObject) response.getJSONObject("rates");
                                                    Double first_currency=rates.getDouble(code1.getText().toString());
                                                    Double second_currency=rates.getDouble(code2.getText().toString());
                                                    if(first_currency!=null && second_currency!=null){
                                                        Log.d("ans",first_currency +" "+ second_currency);
                                                        if (st1.length()==0&&st2.length()==0){
                                                            sets(first_currency,second_currency,-1);
                                                        }else if (st1.equals(editText1.getText().toString()) && st2.equals(editText2.getText().toString())){
                                                            sets(first_currency,second_currency,-1);
                                                        } else if (st1.equals(editText1.getText().toString())){
                                                            sets(first_currency,second_currency,2);
                                                        } else if (st2.equals(editText2.getText().toString())){
                                                            sets(first_currency,second_currency,1);
                                                        }else{
                                                            sets(first_currency,second_currency,-1);
                                                        }
                                                    }else{
                                                        Toast.makeText(MainActivity.this, "Please select a valid country fields\nTry selecting from the options available", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    Toast.makeText(MainActivity.this, "Please try again with valid country code", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Toast.makeText(MainActivity.this, "ERROR IN LOADING\nPlease make sure you are connected to internet", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else{
                                Toast.makeText(MainActivity.this, "PLEASE ENTER BOTH CURRENCIES", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    //show error messages correctly here
                    Toast.makeText(MainActivity.this, "ERROR IN LOADING\nPlease make sure you are connected to internet", Toast.LENGTH_SHORT).show();
                }
            }
        },5400);
    }
    public void sets(Double a,Double b,int code){
        if (code==-1){
            if(!editText1.getText().equals("") && !editText2.getText().equals("") && (editText1.getText().toString()).length()!=0){
                Double c=Double.parseDouble(editText1.getText().toString());
                Double d= new Double((b/a) * c);
                editText1.setText(c.toString());
                editText2.setText(d.toString());
            }else if (!editText2.getText().equals("") && editText2.getText().toString().length()!=0){
                Double c=Double.parseDouble(editText2.getText().toString());
                Double d= new Double((a/b) * c);
                editText1.setText(d.toString());
                editText2.setText(c.toString());
            }else{
                editText1.setText(a.toString());
                editText2.setText(b.toString());
            }
        }else if (code==1 && editText1.getText().toString().length()!=0){
            Double c=Double.parseDouble(editText1.getText().toString());
            Double d= new Double((b/a) * c);
            editText1.setText(c.toString());
            editText2.setText(d.toString());
        } else if (code==2&& editText2.getText().toString().length()!=0){
            Double c=Double.parseDouble(editText2.getText().toString());
            Double d= new Double((a/b) * c);
            editText1.setText(d.toString());
            editText2.setText(c.toString());
        }else {
            Toast.makeText(this, "PLEASE SPECIFY AMOUNT", Toast.LENGTH_SHORT).show();
        }
        st1=editText1.getText().toString();
        st2=editText2.getText().toString();
    }
}