package com.example.root;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;



public class withdraw extends AppCompatActivity {
    TabLayout withdraw_Tab;
    CardView bank,upi,wallet;
    EditText getAccountName,getBankName,getIFSC,getAC,getMobile;//declare here bank edit text
    EditText getUpi;//declare here get upi edittext
    EditText getWalletAPP,getPhone;//here declare wallet edittext
    int  amount;
    TextView Amount;


    //firebase databse
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    loading_dialog loading_dialog;
    int load=0;
    Dialog dialog;


    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        //for dialog box
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.succes_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.unvisible));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        //initialization of card view layout
        withdraw_Tab=findViewById(R.id.paymentMethod);
        bank=findViewById(R.id.bank);
        upi=findViewById(R.id.upi);
        wallet=findViewById(R.id.get_wallet_layout);
        Amount=findViewById(R.id.amount);

        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
        documentReference=collectionReference.document(tournament.userList.get(0).getUserId());

        //initialization of Edit text of card view
        getAccountName=findViewById(R.id.getAccountName);
        getBankName=findViewById(R.id.getBankName);
        getIFSC=findViewById(R.id.getIFSC);
        getAC=findViewById(R.id.getAC);
        getMobile=findViewById(R.id.getMObile);
        getUpi=findViewById(R.id.getUPI);
        getWalletAPP=findViewById(R.id.getWalletAPP);
        getPhone=findViewById(R.id.getphone);


        amount=getIntent().getIntExtra("Amount",0);
        Amount.setText(amount);

        loading_dialog=new loading_dialog(withdraw.this);

        //toolbar start here
        Button back = findViewById(R.id.back_wallet);

        TextView title=findViewById(R.id.waller_toolbar_title);
        title.setText("Withdraw");


        back.setOnClickListener(v -> back());
        //Toolbar coding end here

        //top game select navigation start here
        withdraw_Tab.addTab( withdraw_Tab.newTab().setText("Wallet").setId(1).setIcon(R.drawable.ic_baseline_account_balance_wallet_24));
        withdraw_Tab.addTab( withdraw_Tab.newTab().setText("UPI").setId(2).setIcon(R.drawable.person));
        withdraw_Tab.addTab( withdraw_Tab.newTab().setText("Bank").setId(3).setIcon(R.drawable.bank));
        withdraw_Tab.setTabGravity(TabLayout.GRAVITY_FILL);




        BannerAds();
        wallet();

        //top navigation start here
        withdraw_Tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabs) {
                switch (tabs.getId()) {
                    case 1:
                        wallet();
                        break;
                    case 2:
                        upi();
                        break;
                    case 3:
                        bank();
                        break;


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
    }

    private void wallet(){
        try {
            bank.setVisibility(View.GONE);
            upi.setVisibility(View.GONE);
            wallet.setVisibility(View.VISIBLE);
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }
    }

    private void upi(){
        try {
            bank.setVisibility(View.GONE);
            upi.setVisibility(View.VISIBLE);
            wallet.setVisibility(View.GONE);
        }catch (Exception e){            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();


        }
    }

    private void bank(){
        try {
            bank.setVisibility(View.VISIBLE);
            upi.setVisibility(View.GONE);
            wallet.setVisibility(View.GONE);
        }catch (Exception e){            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();


        }
    }

    public void setBank(View view){
        try {
            if (getAccountName.getText().toString().isEmpty()) {
                getAccountName.setError("Please Enter Holder Name");
                return;
            }
            if (getBankName.getText().toString().isEmpty()) {
                getBankName.setError("Please Enter Bank Name");
                return;
            }
            if (getIFSC.getText().toString().isEmpty()) {
                getIFSC.setError("Please Enter IFSC Code");
                return;
            }
            if (getAC.getText().toString().isEmpty()) {
                getAC.setError("Please Enter Account Number");
                return;
            }
            if (getMobile.getText().toString().isEmpty()) {
                getMobile.setError("Please Enter Mobile Number");
                return;
            }

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss 'at' dd.MM.yyyy");
            String date = simpleDateFormat.format(new Date());


            Map<String, Object> map = new HashMap<>();
            map.put("UserName", tournament.userList.get(0).getUserName());
            map.put("Url", tournament.userList.get(0).getUserPicUrl());
            map.put("UserAmount", Integer.toString(amount));
            map.put("Date", date);
            map.put("Upi", "Null");
            map.put("WalletApp", "Null");
            map.put("WalletPhone", "Null");
            map.put("BankName", getBankName.getText().toString());
            map.put("AccountHolderName", getAccountName.getText().toString());
            map.put("IFSC", getIFSC.getText().toString());
            map.put("AccountNumber", getAC.getText().toString());
            map.put("BankMobile", getMobile.getText().toString());

            Withdraw(map);
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }


    }
    public void setUpi(View view){
        try {
            if (getUpi.getText().toString().isEmpty()) {
                getUpi.setError("Please Enter UPI Id");
                return;
            }


            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss 'at' dd.MM.yyyy");
            String date = simpleDateFormat.format(new Date());


            Map<String, Object> map = new HashMap<>();
            map.put("UserName", tournament.userList.get(0).getUserName());
            map.put("Url", tournament.userList.get(0).getUserPicUrl());
            map.put("UserAmount", Integer.toString(amount));
            map.put("Date", date);
            map.put("Upi", getUpi.getText().toString());
            map.put("WalletApp", "Null");
            map.put("WalletPhone", "Null");
            map.put("BankName", "Null");
            map.put("AccountHolderName", "Null");
            map.put("IFSC", "Null");
            map.put("AccountNumber", "Null");
            map.put("BankMobile", "Null");

            Withdraw(map);
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }




    }
    public void setWallet(View view){
        try {
            if (getWalletAPP.getText().toString().isEmpty()) {
                getWalletAPP.setError("Please Enter Wallet App");
                return;
            }
            if (getPhone.getText().toString().isEmpty()) {
                getPhone.setError("Please Enter Mobile Number");
                return;
            }


            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss 'at' dd.MM.yyyy");
            String date = simpleDateFormat.format(new Date());


            Map<String, Object> map = new HashMap<>();
            map.put("UserName", tournament.userList.get(0).getUserName());
            map.put("Url", tournament.userList.get(0).getUserPicUrl());
            map.put("UserAmount", Integer.toString(amount));
            map.put("Date", date);
            map.put("Upi", "Null");
            map.put("WalletApp", getWalletAPP.getText().toString());
            map.put("WalletPhone", getPhone.getText().toString());
            map.put("BankName", "Null");
            map.put("AccountHolderName", "Null");
            map.put("IFSC", "Null");
            map.put("AccountNumber", "Null");
            map.put("BankMobile", "Null");

            Withdraw(map);
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }

    private void Withdraw(Map<String,Object> maps){
        try {
            loading_dialog.startLoading();
            load = 1;
            String id = UUID.randomUUID().toString();
            myRef.child("withdraw").child(tournament.userList.get(0).getUserId()).child(id).setValue(maps).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String money = tournament.userList.get(0).getUserWinningAmount();
                    int balance = Integer.parseInt(money);

                    int totalAmount = balance - amount;
                    documentReference.update("UserWinningAmount", Integer.toString(totalAmount));
                    withdraw_Tab.setClickable(false);
                    bank.setVisibility(View.GONE);
                    upi.setVisibility(View.GONE);
                    wallet.setVisibility(View.GONE);
                    History(amount);
                    loading_dialog.endLoading();
                    load = 0;
                    show_dialog();


                } else {
                    loading_dialog.endLoading();
                    load = 0;
                    StyleableToast.makeText(withdraw.this, "Something went wrong please try again after some time", R.style.styleRedToast).show();

                }
            });
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }

    //Dialog Box Code Here
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public void show_dialog(){
        try {
            Button Okay = dialog.findViewById(R.id.Done);
            TextView msg = dialog.findViewById(R.id.Message);
            msg.setText("Withdraw Amount Successfully You get your amount In 48 Working Hour Into your choose payment method");


            dialog.show();

            Okay.setOnClickListener(v -> {
                Intent intent = new Intent(withdraw.this, wallet_screen.class);
                startActivity(intent);
                dialog.dismiss();
                finish();

            });
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }


    }








    //add data into user database for transaction history
    private void History(int money){
        try {
            firestore = FirebaseFirestore.getInstance();
            @SuppressLint("SimpleDateFormat")

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String date = simpleDateFormat.format(new Date());

            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss ");
            String date1 = simpleDateFormat1.format(new Date());


            String id = UUID.randomUUID().toString();
            Map<String, Object> map = new HashMap<>();
            map.put("Money", "-" + money + "/Rs");
            map.put("Msg", "Withdraw Amount");
            map.put("Date", date);
            map.put("Time", date1);


            firestore.collection("Users").document(tournament.userList.get(0).getUserId())
                    .collection("TRHistory").document(id).set(map).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    History(money);
                }
            });
        }catch (Exception e){
            StyleableToast.makeText(withdraw.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
    private  void back(){
        if(load==1){
            StyleableToast.makeText(withdraw.this,"Cannot Be Back Please Wait to finish this Process",R.style.styleRedToast).show();
            return;
        }
        Intent intent=new Intent(withdraw.this,wallet_screen.class);
        startActivity(intent);
        finish();
    }

    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}