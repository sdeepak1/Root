package com.example.root;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;



public class getamount extends AppCompatActivity  {
    TextView currentBalance;
    TextView balanceStatus;
    EditText addedAmount,withdrawAmount;
    ConstraintLayout pay,withdraw;
    int total=0;

    String sendUserBonus="0";



    //firebase Variable
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    //for success or failed dialog box
    Dialog dialog1;
    loading_dialog loading_dialog=new loading_dialog(getamount.this);



    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getamount);


        //called banner ads method
        BannerAds();
        //toolbar coding start here
        Button back = findViewById(R.id.back_wallet);
        Button wallet=findViewById(R.id.shareApp);
        wallet.setVisibility(View.GONE);
        loading_dialog=new loading_dialog(getamount.this);

        back.setOnClickListener(v -> back());

        //toolbar coding end here

        //for dialog box



        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
        documentReference=collectionReference.document(tournament.userList.get(0).getUserId());
        if(!tournament.userList.get(0).getFromUserId().equals("null")){
            collectionReference.document(tournament.userList.get(0).getFromUserId()).addSnapshotListener((value, error) -> sendUserBonus= Objects.requireNonNull(value).getString("UserCashBonus"));
        }

        pay=findViewById(R.id.addMoney);
        withdraw=findViewById(R.id.withdraw_dialog);


        currentBalance=findViewById(R.id.currentBalance);
        balanceStatus=findViewById(R.id.balanceStatus);
        int mode=getIntent().getIntExtra("mode",0);
        total=getIntent().getIntExtra("totalAmount",0);
        currentBalance.setText(Integer.toString(total));
        if(mode==0){
            addedAmount=findViewById(R.id.getAddedAmount);
            balanceStatus.setText("Current Balance");
            pay.setVisibility(View.VISIBLE);
        }else{
            withdrawAmount=findViewById(R.id.getWithdrawAmount);
            balanceStatus.setText("Your Winning");
            withdraw.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        back();
    }
    private  void back(){
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void payMoney(View view){
        try {


            if (addedAmount.getText().toString().isEmpty()) {
                addedAmount.setError("Please Enter Added Amount");
                return;
            }
            int a;
            try {
                a = Integer.parseInt(addedAmount.getText().toString());
            } catch (Exception e) {
                StyleableToast.makeText(getamount.this, "Please enter only number", R.style.styleRedToast).show();
                return;
            }
            if (a < 10) {
                addedAmount.setError("Minimum Amount is 10");
                return;
            }
            if (a > 1000) {
                addedAmount.setError("Maximum Amount is 1000");
                return;
            }

            loading_dialog.startLoading();
            //set amount in user wallet
            int added = Integer.parseInt(tournament.userList.get(0).getUserAddedAmount());
            int total = added + Integer.parseInt(addedAmount.getText().toString());
            documentReference.update("UserAddedAmount", Integer.toString(total));
            History(Integer.parseInt(addedAmount.getText().toString()), tournament.userList.get(0).getUserId(), 0);
            //send user bonus
            if (!sendUserBonus.equals("0")) {
                int add = Integer.parseInt(addedAmount.getText().toString());
                float decimal = (float) 10 / 100;
                float value = decimal * add;
                int sendBonus = (int) value;

                int sendCashBonusTotal = Integer.parseInt(sendUserBonus) + sendBonus;
                String finalBonus = Integer.toString(sendCashBonusTotal);
                FirebaseFirestore.getInstance().collection("Users").document(tournament.userList.get(0).getFromUserId()).update("UserCashBonus", finalBonus);
                History(Integer.parseInt(Integer.toString(sendBonus)), tournament.userList.get(0).getFromUserId(), 1);
            }
            loading_dialog.endLoading();
            show();

        }catch (Exception e){
            StyleableToast.makeText(getamount.this,"Something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }

    }



    public void withdraw(View view){
        try {


            if (withdrawAmount.getText().toString().isEmpty()) {
                withdrawAmount.setError("Please Enter Your Amount");
                return;
            }
            int a = Integer.parseInt(withdrawAmount.getText().toString());
            if (a < 100) {
                withdrawAmount.setError("Minimum Withdraw Amount is 100");
                return;
            }
            if (a > 10000) {
                withdrawAmount.setError("Maximum withdraw Amount is 10000");
            }
            if (a <= total) {
                Intent intent = new Intent(getamount.this, withdraw.class);
                intent.putExtra("Amount", a);
                startActivity(intent);
                finish();
            } else {
                withdrawAmount.setError("Please Enter Valid Amount");
            }
        }catch (Exception e){
            StyleableToast.makeText(getamount.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }
    }

    //add data into user database for transaction history
    private void History(int money,String userId,int mode){

        try {


            firestore = FirebaseFirestore.getInstance();
            @SuppressLint("SimpleDateFormat")

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String date = simpleDateFormat.format(new Date());

            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss ");
            String date1 = simpleDateFormat1.format(new Date());
            String msg;
            if (mode == 0) {
                msg = "Amount added";
            } else {
                msg = "Refer A Friend Bonus";
            }


            String id = UUID.randomUUID().toString();
            Map<String, Object> map = new HashMap<>();
            map.put("Money", "+" + money + "/Rs");
            map.put("Msg", msg);
            map.put("Date", date);
            map.put("Time", date1);


            firestore.collection("Users").document(userId)
                    .collection("TRHistory").document(id).set(map).addOnCompleteListener(task -> {

            });
        }catch (Exception e){
            StyleableToast.makeText(getamount.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void show(){
        try {


            dialog1 = new Dialog(getamount.this);
            dialog1.setContentView(R.layout.succes_dialog);
            dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.unvisible));
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.setCancelable(false);
            dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            Button Okay = dialog1.findViewById(R.id.Done);
            TextView msg = dialog1.findViewById(R.id.Message);
            msg.setText("You Have Successfully Added Amount into wallet");

            dialog1.show();
            Okay.setOnClickListener(view -> {
                Intent intent = new Intent(getamount.this, wallet_screen.class);
                dialog1.dismiss();
                startActivity(intent);
                finish();
            });

        }catch (Exception e){
            StyleableToast.makeText(getamount.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }

    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);




    }



}