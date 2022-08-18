package com.example.root;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


public class match_details extends AppCompatActivity {

    //declare all data type
    Button join, live_complete_button,back,room_id;
    String entry;

    ViewPager2 viewPager2;

    CardView md_image;
  Button wallet;
  public static   int position;
    loading_dialog loading_dialog;



    //get room id or password
    String roomId,roomPassword;

    //firebase Variable
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();

    TextView title;


    //all dialog variable here
    Dialog allDialog;
   public static int checkJoin;

    //for get how much amount used in join
    int cb=0,aa=0,wa=0;

    //for check user are valid or not for  join contest
    Boolean check=false;
    //if user money not enough for us so get how much monney need of us
    int neededMoney=0;

    TabLayout matchTab;

    InterstitialAd ad;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);



      ads();
        //for tab layout
        matchTab=findViewById(R.id.matchTab);


        //all dialog box attribute declare here
        allDialog=new Dialog(match_details.this);



        loading_dialog=new loading_dialog(match_details.this);

        position=getIntent().getIntExtra("position",0);

        //initilize firebase variable
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
        documentReference=collectionReference.document(tournament.userList.get(0).getUserId());




        initilaztion();
        //set image of game in mdImage
        String name=getIntent().getStringExtra("name");
            Drawable drawable;
            switch (name){
                case "freefire":
                    drawable=getDrawable(R.drawable.md_ff);
                    md_image.setForeground(drawable);
                    break;
                case "bgmi":
                    drawable=getDrawable(R.drawable.md_bgmi);
                    md_image.setForeground(drawable);
                    break;
                case "cod":
                    drawable=getDrawable(R.drawable.md_cod);
                    md_image.setForeground(drawable);
                    break;
                case "ludoking":
                    drawable=getDrawable(R.drawable.md_ludo);
                    md_image.setForeground(drawable);
                    break;
            }





        //toolbar start here
         back = findViewById(R.id.back);
        wallet = findViewById(R.id.wallet);




        back.setOnClickListener(v -> back());

        wallet.setOnClickListener(v -> showWalletDialog());


        //toolbar coding end here

        //tab layout coding start here
        matchTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //tab layout coding  end here

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                matchTab.selectTab(matchTab.getTabAt(position));
            }
        });







    }


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void initilaztion() {



        //tab layout initialization
        matchTab.addTab(matchTab.newTab().setText("Match Details").setIcon(R.drawable.live));
        matchTab.addTab(matchTab.newTab().setText("Winners").setIcon(R.drawable.winner));
        viewPager2=findViewById(R.id.viewPager2);

        matchTab.setTabGravity(TabLayout.GRAVITY_FILL);

        md_image = findViewById(R.id.md_image);

        live_complete_button=findViewById(R.id.liveCompleate);
        join = findViewById(R.id.join);

        room_id=findViewById(R.id.room_id);


        checkJoin=getIntent().getIntExtra("joinCheck",0);


        FragmentManager fm=getSupportFragmentManager();
      fragmentAdapter  fragmentAdapter=new fragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);


        dataSet();


    }

    @SuppressLint("SetTextI18n")
    public void dataSet(){
        try {
            //when user already join contest
            if (checkJoin == 0) {

                entry = Integer.toString(tournament.joinList.get(position).getTournament_Entry_Fee());
                title = findViewById(R.id.toolbar_title);
                title.setText(tournament.joinList.get(position).getTournament_Game_Name());
                roomId = tournament.joinList.get(position).getRoomId();
                roomPassword = tournament.joinList.get(position).getRoomPassword();

                //set text in join button to is joined text
                live_complete_button.setVisibility(View.VISIBLE);
                live_complete_button.setText("Already join ");
                room_id.setVisibility(View.VISIBLE);
                join.setVisibility(View.GONE);
                if (tournament.joinList.get(position).getRoomId().equals("null")) {
                    room_id.setAlpha(0.5f);
                } else {
                    room_id.setAlpha(1);
                }
//when user not join contest
            } else {
                entry = Integer.toString(tournament.list.get(position).getTournament_Entry_Fee());

                title = findViewById(R.id.toolbar_title);
                title.setText(tournament.list.get(position).getTournament_Game_Name());
                room_id.setVisibility(View.GONE);

                if (tournament.list.get(position).getTournament_Mode_Value() == 1) {
                    int a = tournament.list.get(position).getTournament_Limit_Person();
                    int b = tournament.list.get(position).getGetAllJoinUserSize().size();
                    if (b < a) {
                        join.setVisibility(View.VISIBLE);
                    } else {
                        live_complete_button.setVisibility(View.VISIBLE);
                        live_complete_button.setText("Contest is Full");
                        join.setVisibility(View.GONE);
                    }

                } else if (tournament.list.get(position).getTournament_Mode_Value() == 2) {
                    live_complete_button.setVisibility(View.VISIBLE);
                    live_complete_button.setText("Live");
                    join.setVisibility(View.GONE);
                } else if (tournament.list.get(position).getTournament_Mode_Value() == 3) {
                    live_complete_button.setVisibility(View.VISIBLE);
                    live_complete_button.setText("Completed");
                    join.setVisibility(View.GONE);
                } else {
                    StyleableToast.makeText(match_details.this, "Something Went Wrong when check mode value", R.style.styleRedToast);
                }

            }
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }

    public void matchRule(View view) {
        loading_dialog.startLoading();
        Intent intent1=new Intent(match_details.this,website.class);
        intent1.putExtra("name","Match Rules");
        myRef.child("AppData").child("Rules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent1.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                loading_dialog.endLoading();
                startActivity(intent1);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading_dialog.endLoading();
                StyleableToast.makeText(match_details.this,"Something went wrong",R.style.styleRedToast).show();

            }
        });

    }

    public void matchJoin(View view) {
        calculate();
        if(check){
            showJoinDialog();
        }else{
            enoughMoneyDialog();
        }
    }

    //joining money calculate here
    public void calculate(){
        //tournament entry fee
        int entry = tournament.list.get(position).getTournament_Entry_Fee();
        int add=tournament.list.get(position).getTournament_Entry_Fee();


        try {
            //25% use cash bonus
            float decimal = (float) 25 / 100;
            float value = decimal * add;
            int entryCashBonus = (int) value;


            //user amount
            int addedMoney = (Integer.parseInt(tournament.userList.get(0).getUserAddedAmount()));
            int winningMoney = (Integer.parseInt(tournament.userList.get(0).getUserWinningAmount()));
            int cashBones = (Integer.parseInt(tournament.userList.get(0).getUserCashBonus()));

            //now start all calculation

            if (cashBones >= entryCashBonus) {
                entry = entry - entryCashBonus;
                cb = entryCashBonus;
            } else {
                cb = cashBones;
                entry = entry - cashBones;


            }

            if (addedMoney >= entry) {
                aa = entry;

                //user Added money is enough for join us so join dialog box start
                check = true;


            } else {
                entry = entry - addedMoney;
                aa = addedMoney;

                if (winningMoney >= entry) {
                    wa = entry;
                    //user Cash bonus/added amount / Winning money is enough for join us so join dialog box start
                    check = true;
                } else {
                    entry = entry - winningMoney;
                    wa = winningMoney;
                    check = false;
                    //not enough money
                    neededMoney = entry;
                }
            }
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }

        }

    public void liveMatch(View view) {
       StyleableToast.makeText(match_details.this,live_complete_button.getText().toString()+" Cannot be join",R.style.styleRedToast).show();
    }

    private  void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
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
            map.put("Msg", "Joined Contest");
            map.put("Date", date);
            map.put("Time", date1);


            firestore.collection("Users").document(tournament.userList.get(0).getUserId())
                    .collection("TRHistory").document(id).set(map).addOnCompleteListener(task -> {
            });
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }

    //get room id and show the room id dialog box
    @SuppressLint("UseCompatLoadingForDrawables")
    public void getRoom(View view) {
        try {
            if (roomId.equals("null") && roomPassword.equals("null")) {
                StyleableToast.makeText(match_details.this, "Room Id And Password Available 20 Minute Before The Contest Time", R.style.styleRedToast).show();
                return;
            }
            Button done;
            TextView id, password;
            allDialog.setContentView(R.layout.room_idpassword_dialog);
            dialogSet();


            done = allDialog.findViewById(R.id.dones);
            id = allDialog.findViewById(R.id.getId);
            password = allDialog.findViewById(R.id.getRoomPassword);
            id.setText(roomId);
            if (roomPassword.equals("null")) {
                roomPassword = "";
            }
            password.setText(roomPassword);
            allDialog.show();

            done.setOnClickListener(view1 -> allDialog.dismiss());
            password.setOnClickListener(view12 -> {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copy", password.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    StyleableToast.makeText(match_details.this, "text copied", R.style.styleGreenToast).show();
                } catch (Exception e) {
                    StyleableToast.makeText(match_details.this, e.getMessage(), R.style.styleRedToast).show();
                }


            });
            id.setOnClickListener(view13 -> {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copy", id.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    StyleableToast.makeText(match_details.this, "text copied", R.style.styleGreenToast).show();
                } catch (Exception e) {
                    StyleableToast.makeText(match_details.this, e.getMessage(), R.style.styleRedToast).show();
                }
            });
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }




    //Dialog Box Code Here
    @SuppressLint({"ResourceAsColor", "SetTextI18n", "UseCompatLoadingForDrawables"})
    public void show_dialog(){
        //for success dialog box
        try {
            allDialog.setContentView(R.layout.succes_dialog);
            dialogSet();

            Button Okay = allDialog.findViewById(R.id.Done);
            TextView msg = allDialog.findViewById(R.id.Message);
            msg.setText("You Have Successfully join This Contest");
            allDialog.show();
            Okay.setOnClickListener(v -> {
                allDialog.dismiss();
                Intent intent = new Intent(match_details.this, tournament.class);
                startActivity(intent);
                finish();
            });
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }


    }

    //show joining dialog box and join also
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void showJoinDialog(){
        try {
            //show join dialog box
            allDialog.setContentView(R.layout.joindialog);
            dialogSet();


            Button closeDialog = allDialog.findViewById(R.id.CloseMdDialog);
            EditText getUserGameId = allDialog.findViewById(R.id.get_user_game_id);

            Button finalJoin = allDialog.findViewById(R.id.final_join);

            TextView totalEntry=allDialog.findViewById(R.id.join_Total_Entry_Fee);

            //for balance textview
            TextView AddedBalance = allDialog.findViewById(R.id.MDAABalance);
            TextView WinningBalance = allDialog.findViewById(R.id.MDWBalance);
            TextView cashBonus = allDialog.findViewById(R.id.MDCBBalance);

            AddedBalance.setText(tournament.userList.get(0).getUserAddedAmount());
            WinningBalance.setText(tournament.userList.get(0).getUserWinningAmount());
            cashBonus.setText(tournament.userList.get(0).getUserCashBonus());

            //for used amount textview
            TextView useAdded = allDialog.findViewById(R.id.usesAmountAdded);
            TextView useWinning = allDialog.findViewById(R.id.mdWinnerUsable);
            TextView useCashBonus = allDialog.findViewById(R.id.mdCashBonusUsable);

            useAdded.setText(Integer.toString(aa));
            useWinning.setText(Integer.toString(wa));
            useCashBonus.setText(Integer.toString(cb));

            totalEntry.setText(Integer.toString(tournament.list.get(position).getTournament_Entry_Fee()));

            allDialog.show();

            closeDialog.setOnClickListener(view -> allDialog.dismiss());

            finalJoin.setOnClickListener(view -> {
                if (getUserGameId.getText().toString().isEmpty()) {
                    getUserGameId.setError("Please Enter Game Id");
                    return;
                }
                loading_dialog.startLoading();

                Map<String, Object> map = new HashMap<>();
                map.put("user_Name", tournament.userList.get(0).getUserName());
                map.put("user_Url", tournament.userList.get(0).getUserPicUrl());
                map.put("user_Game_Id", getUserGameId.getText().toString());
                map.put("user_Phone", tournament.userList.get(0).getUserMobile());
                map.put("user_Id", tournament.userList.get(0).getUserId());
                map.put("User_Email", tournament.userList.get(0).getUserEmail());


                myRef.child("tournament").child(tournament.list.get(position).getTournament_Game_Name()).child("upcoming")
                        .child(tournament.list.get(position).getTournament_Id()).child("Tournament_join_Person").child(tournament.userList.get(0).getUserId()).setValue(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                firestore = FirebaseFirestore.getInstance();
                                collectionReference = firestore.collection("Users");
                                documentReference = collectionReference.document(tournament.userList.get(0).getUserId());

                                documentReference.update("UserAddedAmount", Integer.toString(Integer.parseInt(tournament.userList.get(0).getUserAddedAmount()) - aa));
                                documentReference.update("UserWinningAmount", Integer.toString(Integer.parseInt(tournament.userList.get(0).getUserWinningAmount()) - wa));
                                documentReference.update("UserCashBonus", Integer.toString(Integer.parseInt(tournament.userList.get(0).getUserCashBonus()) - cb));

                                History(Integer.parseInt(entry));
                                allDialog.dismiss();
                                showAd();



                            } else {
                                allDialog.dismiss();
                                StyleableToast.makeText(match_details.this, "Something went wrong please try again later", R.style.styleRedToast).show();
                            }
                            loading_dialog.endLoading();
                        });

            });
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }

    //show wallet dialog box
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void showWalletDialog(){
        try {
            //for wallet dialog box
            allDialog.setContentView(R.layout.walletdialog);
            dialogSet();


            TextView walletTotal = allDialog.findViewById(R.id.WalletTotal);
            Button addCash = allDialog.findViewById(R.id.addCash);
            TextView added = allDialog.findViewById(R.id.DialogAdded);
            TextView winning = allDialog.findViewById(R.id.DialogWinning);
            TextView cashBonus = allDialog.findViewById(R.id.DialogCash_Bonus);
            Button dialogClose = allDialog.findViewById(R.id.DialogClose);
            //set data into wallet dialog box textView
            added.setText(tournament.userList.get(0).getUserAddedAmount());
            winning.setText(tournament.userList.get(0).getUserWinningAmount());
            cashBonus.setText(tournament.userList.get(0).getUserCashBonus());
            int total = 0;
            try {
                total = Integer.parseInt(added.getText().toString()) +
                        Integer.parseInt(winning.getText().toString()) +
                        Integer.parseInt(cashBonus.getText().toString());
                walletTotal.setText(Integer.toString(total));
            } catch (Exception e) {
                StyleableToast.makeText(match_details.this, "Something went wrong" + e.getMessage(), R.style.styleRedToast).show();
            }

            allDialog.show();
            int finalTotal = total;
            addCash.setOnClickListener(view -> {
                Intent intent = new Intent(match_details.this, getamount.class);
                intent.putExtra("totalAmount", finalTotal);
                intent.putExtra("mode", 0);
                startActivity(intent);
                finish();
            });

            dialogClose.setOnClickListener(view -> allDialog.dismiss());
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }
    }

    //if money is low than show this dialog
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void enoughMoneyDialog() {
        try {
            //for enough money dialog box
            allDialog.setContentView(R.layout.notenoughmoneydialogbox);
            dialogSet();

            //variable declare here
            TextView entryFees = allDialog.findViewById(R.id.dialogEntryFee);
            Button closeDialog = allDialog.findViewById(R.id.CloseEnoughMoneyDialog);
            //for user balance textView
            TextView EnoughAddedBalance = allDialog.findViewById(R.id.EnoughAddedBalance);
            TextView EnoughWinningBalance = allDialog.findViewById(R.id.EnoughWinningBalance);
            TextView EnoughCashBonusBalance = allDialog.findViewById(R.id.EnoughCashBonusBalance);
            //for how money used textView
            TextView useEnoughAddedAmount = allDialog.findViewById(R.id.usesEnoughAddedAmount);
            TextView useEnoughWinningAmount = allDialog.findViewById(R.id.usesEnoughWinningAmount);
            TextView useEnoughCashBonusAmount = allDialog.findViewById(R.id.usesEnoughCashBonusAmount);
            //for set how much money needed
            TextView showMessage = allDialog.findViewById(R.id.setText);
            //set data in variable here
            entryFees.setText(Integer.toString(tournament.list.get(position).getTournament_Entry_Fee()));
            EnoughAddedBalance.setText(tournament.userList.get(0).getUserAddedAmount());
            EnoughWinningBalance.setText(tournament.userList.get(0).getUserWinningAmount());
            EnoughCashBonusBalance.setText(tournament.userList.get(0).getUserCashBonus());
            useEnoughAddedAmount.setText(Integer.toString(aa));
            useEnoughWinningAmount.setText(Integer.toString(wa));
            useEnoughCashBonusAmount.setText(Integer.toString(cb));

            showMessage.setText("Enough Money! Please Add " + neededMoney + "Rs Into Your Wallet to Join Contest");
            allDialog.show();

            closeDialog.setOnClickListener(view -> allDialog.dismiss());
        }catch (Exception e){
            StyleableToast.makeText(match_details.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void dialogSet(){
        //for compress dialog code
        allDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.unvisible));
        allDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        allDialog.setCancelable(false);
        allDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }
    public void ads(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        ad = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        ad = null;
                    }
        });

    }

    public void showAd(){
        if (ad != null) {
            ad.show(match_details.this);

        }
        show_dialog();
    }


}




