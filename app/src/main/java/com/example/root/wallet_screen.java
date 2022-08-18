package com.example.root;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Objects;




public class wallet_screen extends AppCompatActivity  {


    loading_dialog loading_dialog;
    //firebase Variable
    FirebaseFirestore firestore;
    private DocumentReference documentReference;
    TextView total_balance,addedAmount,winning,cashBonus;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    int total=0;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);




        firestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("Users");
        documentReference= collectionReference.document(tournament.userList.get(0).getUserId());
        total_balance=findViewById(R.id.total_balance);
        addedAmount=findViewById(R.id.addedAmount);
        winning=findViewById(R.id.winningAmount);
        cashBonus=findViewById(R.id.cashBonus);


        //initilize loading dialog box
        loading_dialog=new loading_dialog(wallet_screen.this);

        //toolbar start here
        Button back = findViewById(R.id.back_wallet);
        Button shareApp = findViewById(R.id.shareApp);

        shareApp.setVisibility(View.GONE);
        back.setOnClickListener(v -> back());




        //toolbar coding end here




        BannerAds();
        fetch();


    }


    @Override
    public void onBackPressed() {
        back();
    }
    private  void back(){
        finish();
    }

    public void addMoney(View view){
        Intent intent=new Intent(wallet_screen.this,getamount.class);
        intent.putExtra("totalAmount",total);
        intent.putExtra("mode",0);
        startActivity(intent);
    }


    public  void withdrawMoney(View view){
        Intent intent=new Intent(wallet_screen.this,getamount.class);
        intent.putExtra("totalAmount",Integer.parseInt(tournament.userList.get(0).getUserWinningAmount()));
        intent.putExtra("mode",1);
        startActivity(intent);
        finish();
    }


    public  void recentTranscation(View view){
        Intent intent=new Intent(wallet_screen.this,recentTranscation.class);
        startActivity(intent);
        finish();
    }
    public  void invite(View view){
        Intent intent=new Intent(wallet_screen.this,invite.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void fetch(){
        try {
            loading_dialog.startLoading();

            //initilize firebase variable

            documentReference.addSnapshotListener((value, error) -> {
                tournament.userList.get(0).setUserAddedAmount(Objects.requireNonNull(value).getString("UserAddedAmount"));
                tournament.userList.get(0).setUserWinningAmount(Objects.requireNonNull(value).getString("UserWinningAmount"));
                tournament.userList.get(0).setUserCashBonus(Objects.requireNonNull(value).getString("UserCashBonus"));

                String added = tournament.userList.get(0).getUserAddedAmount();
                String winningAmount = tournament.userList.get(0).getUserWinningAmount();
                String Bonus = tournament.userList.get(0).getUserCashBonus();

                //set data in textView
                addedAmount.setText(added);
                winning.setText(winningAmount);
                cashBonus.setText(Bonus);
                //total of all balance and set in total balance text View
                 total = Integer.parseInt(addedAmount.getText().toString()) +
                        Integer.parseInt(winning.getText().toString()) +
                        Integer.parseInt(cashBonus.getText().toString());

                total_balance.setText(Integer.toString(total));
                loading_dialog.endLoading();
            });
        }catch (Exception e){
            StyleableToast.makeText(wallet_screen.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }



    }

    public void cashBonus(View view){
        loading_dialog.startLoading();
        Intent intent10=new Intent(wallet_screen.this,website.class);
        intent10.putExtra("name","Cash Bonus");
        myRef.child("AppData").child("cashbonus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent10.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                loading_dialog.endLoading();
                startActivity(intent10);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading_dialog.endLoading();
                StyleableToast.makeText(wallet_screen.this,"Something went wrong",R.style.styleRedToast).show();
            }
        });    }


    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }





}