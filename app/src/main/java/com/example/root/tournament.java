package com.example.root;

import android.annotation.SuppressLint;
import android.app.Dialog;


import android.content.Intent;



import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class tournament extends AppCompatActivity {

    //declare all variable
    SwipeRefreshLayout refreshLayout;
    int refreshStage=0;
    TabLayout tour_tabLayout,tour_bottom_tab_layout;//for tab layout

    RecyclerView tournament_view,joinView;
    ShimmerFrameLayout shimmerFrameLayout;
    int tab_id;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    TextView shoemessage;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView mycontest;
    Button viewAll;
    ImageView imageView;

    loading_dialog loading_dialog;

    //for firebase
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    String userId,game_name;
   FirebaseFirestore firestore;
   CollectionReference collectionReference;
   DocumentReference documentReference;

    //for headers datatype
    CircleImageView userProfile;
    TextView userName;
    TextView userEmail;
    View hView;

    //userlist
    public static List<userDataModel> userList=new ArrayList<>();



//slider image variable
    SliderView sliderView1;
    int[] rag_image={R.drawable.tour_ff,
            R.drawable.tour_bgmi_image,
            R.drawable.tour_ludo_image,
    R.drawable.tour_cod};







    public static List<tournament_model> list=new ArrayList<>();
    public static List<tournament_model>joinList=new ArrayList<>();

    tournament_adapter adapter;
    Dialog walletDialog;
    //wallet dialog textView id
    TextView added;
    TextView winning;
    TextView cashBonus;


    @SuppressLint({"SetTextI18n", "NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);



        //initilize tournament activity data type
        tour_tabLayout=findViewById(R.id.tournament_tab);
        tour_bottom_tab_layout=findViewById(R.id.tournament_Bottom_tab);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mycontest=findViewById(R.id.textView15);
        viewAll=findViewById(R.id.textView33);
        imageView=findViewById(R.id.imageView);
        hView=navigationView.getHeaderView(0);
        refreshLayout=findViewById(R.id.refresh);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loading_dialog=new loading_dialog(tournament.this);



        shimmerFrameLayout=findViewById(R.id.shimmer);
        shoemessage=findViewById(R.id.showmessage);
        list.clear();
        tournament_view=findViewById(R.id.tournament_view);
        joinView=findViewById(R.id.joinView);

        sliderView1=findViewById(R.id.tour_image_slider);
        tour_slide_adapter tour_slide_adapter=new tour_slide_adapter(rag_image);
        sliderView1.setSliderAdapter(tour_slide_adapter);
        sliderView1.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView1.startAutoCycle();


        //top game select navigation start here
        tour_tabLayout.addTab(tour_tabLayout.newTab().setText("FreeFire").setId(1).setIcon(R.drawable.ic_freefire_icon));
        tour_tabLayout.addTab(tour_tabLayout.newTab().setText("BGMI").setId(2).setIcon(R.drawable.ic_bgmi_icon));
        tour_tabLayout.addTab(tour_tabLayout.newTab().setText("COD").setId(3).setIcon(R.drawable.cod));
        tour_tabLayout.addTab(tour_tabLayout.newTab().setText("LudoKing").setId(4).setIcon(R.drawable.ic_dice));
        tour_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //top game select navigation end here
        tour_bottom_tab_layout.addTab(tour_bottom_tab_layout.newTab().setText("HOME").setId(1).setIcon(R.drawable.ic_baseline_home_24));
        tour_bottom_tab_layout.addTab(tour_bottom_tab_layout.newTab().setText("LIVE").setId(2).setIcon(R.drawable.live));
        tour_bottom_tab_layout.addTab(tour_bottom_tab_layout.newTab().setText("WINNER").setId(3).setIcon(R.drawable.winner));
        tour_bottom_tab_layout.addTab(tour_bottom_tab_layout.newTab().setText("Notification").setId(4).setIcon(R.drawable.ic_baseline_notifications_24));
        tour_bottom_tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        //for wallet dialog box
        walletDialog = new Dialog(this);
        walletDialog.setContentView(R.layout.walletdialog);
        walletDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.unvisible));
        walletDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        walletDialog.setCancelable(false);
        walletDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initilize variable of firebase
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        userId= Objects.requireNonNull(firebaseUser).getUid();
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
        documentReference=collectionReference.document(userId);

        //for initilze header datatype
        userProfile=hView.findViewById(R.id.userPic);
        userName=hView.findViewById(R.id.userName);
        userEmail=hView.findViewById(R.id.userEmail);

        //for connect wallet dialog with textView
         added=walletDialog.findViewById(R.id.DialogAdded);
         winning=walletDialog.findViewById(R.id.DialogWinning);
        cashBonus=walletDialog.findViewById(R.id.DialogCash_Bonus);


        userList.clear();
        try {
            documentReference.addSnapshotListener((value, error) -> {
                userList.add(new userDataModel(Objects.requireNonNull(value).getString("UserName"),
                        value.getString("UserId"),
                        value.getString("UserEmail"),
                        value.getString("UserMobile"),
                        value.getString("UserImage"),
                        value.getString("UserDOB"),
                        value.getString("UserGender"),
                        value.getString("UserAddedAmount"),
                        value.getString("UserWinningAmount"),
                        value.getString("UserCashBonus"),
                        value.getString("FromUserId"),
                        value.getString("ReferralId")));


                userEmail.setText(userList.get(0).getUserEmail());
                userName.setText(userList.get(0).getUserName());
                Glide.with(getApplicationContext()).load(userList.get(0).getUserPicUrl()).into(userProfile);

                //set value in user wallet dialog box
                added.setText(userList.get(0).getUserAddedAmount());
                winning.setText(userList.get(0).getUserWinningAmount());
                cashBonus.setText(userList.get(0).getUserCashBonus());


            });
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }


        FreeFire();

        //toolbar start here
        Button wallet = findViewById(R.id.wallet);
        Button shareApp=findViewById(R.id.shareApp);
        TextView title = findViewById(R.id.toolbar_title);


        wallet.setOnClickListener(v -> showWalletDialog());

        shareApp.setOnClickListener(view -> {
            Intent intent=new Intent(tournament.this,invite.class);
            startActivity(intent);
        });

        title.setText("ROOT");
        //toolbar coding end here








        //top navigation start here
        tour_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabs) {
                tab_id=tabs.getId();
                switch(tabs.getId()){
                    case 1:
                        FreeFire();
                        break;
                    case 2:
                        bgmi();
                        break;
                    case 3:
                        cod();
                        break;
                    case 4:
                        ludoking();
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
        //top navigation end here


        //bottom tab coding start here

        tour_bottom_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getId()){
                    case 2:
                        Intent intent=new Intent(tournament.this,live_complete_screen.class);
                        intent.putExtra("id",tab_id);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent1=new Intent(tournament.this,winner_screen.class);
                        intent1.putExtra("id",tab_id);
                        startActivity(intent1);
                        break;
                    case 4:
                        Intent intent2=new Intent(tournament.this,notification.class);
                        intent2.putExtra("id",tab_id);
                        startActivity(intent2);
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





        //bottom tab coding end here


        //navigation click listener

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.myProfile:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent4=new Intent(tournament.this,myprofile.class);
                    startActivity(intent4);
                    break;

                case R.id.menuWallet:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent=new Intent(tournament.this,wallet_screen.class);
                    startActivity(intent);
                    break;

                case R.id.privacy:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    loading_dialog.startLoading();
                    Intent intent1=new Intent(tournament.this,website.class);
                    intent1.putExtra("name","Privacy Policy");
                    myRef.child("AppData").child("privacy").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            intent1.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                            loading_dialog.endLoading();
                            startActivity(intent1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading_dialog.endLoading();
                            StyleableToast.makeText(tournament.this,"Something went wrong",R.style.styleRedToast).show();
                        }
                    });

                    break;
                case R.id.refund:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    loading_dialog.startLoading();
                    Intent intent10=new Intent(tournament.this,website.class);
                    intent10.putExtra("name","Refund Policy");
                    myRef.child("AppData").child("refund").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            intent10.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                            loading_dialog.endLoading();
                            startActivity(intent10);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading_dialog.endLoading();
                            StyleableToast.makeText(tournament.this,"Something went wrong",R.style.styleRedToast).show();
                        }
                    });

                    break;

                case R.id.community:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    loading_dialog.startLoading();
                    Intent intent2=new Intent(tournament.this,website.class);
                    intent2.putExtra("name","Community Guidelines");

                    myRef.child("AppData").child("Community").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            intent2.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                            loading_dialog.endLoading();
                            startActivity(intent2);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading_dialog.endLoading();
                            StyleableToast.makeText(tournament.this,"Something went wrong",R.style.styleRedToast).show();
                        }
                    });
                    break;

                case R.id.term:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    loading_dialog.startLoading();
                    Intent intent3=new Intent(tournament.this,website.class);
                    intent3.putExtra("name","Terms And Conditions");
                    myRef.child("AppData").child("Term").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            intent3.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                            loading_dialog.endLoading();
                            startActivity(intent3);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loading_dialog.endLoading();
                            StyleableToast.makeText(tournament.this,"Something went wrong",R.style.styleRedToast).show();
                        }
                    });
                    break;

                case R.id.contact:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    String sub="Contact Us";
                    sendEmail(sub,1);
                    break;

                case R.id.report:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    String subs="Report/Feedback";
                    sendEmail(subs,0);
                    break;

                case R.id.share:

                case R.id.menuInvite:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent11=new Intent(tournament.this,invite.class);
                    startActivity(intent11);
                    break;

                case R.id.logout:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    AlertDialog.Builder dialog=new AlertDialog.Builder(tournament.this);
                    dialog.setCancelable(false)
                            .setMessage("Account Logout");
                    dialog.setPositiveButton("Yes", (dialog12, which) -> {
                        auth.signOut();
                        finish();

                    })
                            .setNegativeButton("No", (dialog1, which) -> dialog1.cancel());

                    dialog.create();
                    dialog.show();
                    break;

            }

            return true;
        });

        //for refresh layout
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red));
            refreshStage=1;
            switch(tab_id){
                case 1:
                    FreeFire();
                    break;
                case 2:
                    bgmi();
                    break;
                case 3:
                    cod();
                    break;
                case 4:
                    ludoking();
                    break;

            }

        });

        //for all joined contact activity
        viewAll.setOnClickListener(view -> {
            Intent intent=new Intent(tournament.this,joinedcontest.class);
            startActivity(intent);
        });

    }

    private void FreeFire(){
        try {
            game_name = "freefire";
            tab_id = 1;
            goneMethod();
            fetchData();
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }

    }

    private void bgmi(){
        try {
            game_name = "bgmi";
            goneMethod();
            fetchData();
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }

    }

    private void cod(){
        try {
            game_name = "cod";
            goneMethod();
            fetchData();
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }

    }



    private void ludoking(){
        try {
            game_name = "ludoking";
            goneMethod();
            fetchData();
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }



    }

    private  void back(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setCancelable(false)
                .setMessage("Are You Sure You Want To Exit");
        dialog.setPositiveButton("Yes", (dialog12, which) -> finish())
                .setNegativeButton("No", (dialog1, which) -> dialog1.cancel());

        dialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START) ){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        back();
    }



    //fetch data from firebase
    private void fetchData(){
        try {
            final Boolean[] check = {false};
            //Read tournament details data from firebase
            myRef.child("tournament").child(game_name).child("upcoming").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        ArrayList<String> getAllUser = new ArrayList<>();
                        List<showAmountModel> models = new ArrayList<>();

                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Tournament_join_Person").getChildren()) {
                            getAllUser.add(dataSnapshot2.getKey());
                            if (Objects.equals(dataSnapshot2.getKey(), userId)) {
                                check[0] = true;
                            }

                        }

                        for (DataSnapshot snapshot1 : dataSnapshot1.child("winners").getChildren()) {
                            models.add(new showAmountModel(snapshot1.getKey(), Objects.requireNonNull(snapshot1.child("amount").getValue()).toString()));
                        }

                        //if user already join contest so fetch this details
                        if (check[0]) {
                            joinList.add(new tournament_model(Objects.requireNonNull(dataSnapshot1.child("Tournament_Name").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_ID").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Game_Name").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Date").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Time").getValue()).toString(),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Winner_Amount").getValue()).toString()),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Entry_Fee").getValue()).toString()),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Type").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Mode").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Map").getValue()).toString(),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Limit_Person").getValue()).toString()),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Mode_Value").getValue()).toString()),
                                    getAllUser, 0, Objects.requireNonNull(dataSnapshot1.child("TournamentRoomId").getValue()).toString()
                                    , Objects.requireNonNull(dataSnapshot1.child("TournamentRoomPassword").getValue()).toString(), models,0

                            ));
                            check[0] = false;
                            //if user not join contest so fetch this details
                        } else {


                            list.add(new tournament_model(Objects.requireNonNull(dataSnapshot1.child("Tournament_Name").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_ID").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Game_Name").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Date").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Time").getValue()).toString(),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Winner_Amount").getValue()).toString()),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Entry_Fee").getValue()).toString()),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Type").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Mode").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot1.child("Tournament_Match_Map").getValue()).toString(),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Limit_Person").getValue()).toString()),
                                    Integer.parseInt(Objects.requireNonNull(dataSnapshot1.child("Tournament_Mode_Value").getValue()).toString()),
                                    getAllUser, 1, "null", "null", models,0

                            ));
                        }
                    }
                    if (!joinList.isEmpty()) {
                        imageView.setVisibility(View.VISIBLE);
                        mycontest.setVisibility(View.VISIBLE);
                        viewAll.setVisibility(View.VISIBLE);
                        adapter = new tournament_adapter(joinList,1);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(tournament.this);
                        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        joinView.setLayoutManager(layoutManager);
                        joinView.setAdapter(adapter);
                        joinView.setVisibility(View.VISIBLE);
                    }


                    adapter = new tournament_adapter(list,2);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(tournament.this);
                    tournament_view.setLayoutManager(layoutManager);
                    tournament_view.setAdapter(adapter);
                    visible();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    StyleableToast.makeText(tournament.this, "SomeThing Went Wrong When Fetch the data from firebase", R.style.styleRedToast).show();

                }
            });
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }


    }

    private void visible(){
//here manage visibility of layout
        try {
            if (list.isEmpty()) {
                shoemessage.setVisibility(View.VISIBLE);
            }
            if (joinList.isEmpty()) {
                joinView.setVisibility(View.GONE);
            }
            if (refreshStage == 1) {
                refreshLayout.setRefreshing(false);
            }
            tournament_view.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.GONE);
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }



    }
    //fuction for send email
    @SuppressLint("IntentReset")
    public void sendEmail(String subject, int check){

        try {
            String text = "Write your message here";
            if (check == 1) {
                text = "Write your problem/message Here And if you can so please Send Any Image/video of related problem";
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] RootMail = {"root.official6@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, RootMail);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send Mail"));
        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }

    }

    //show wallet dialog box
    @SuppressLint("SetTextI18n")
    public void showWalletDialog(){
        try {
            TextView walletTotal = walletDialog.findViewById(R.id.WalletTotal);
            Button addCash = walletDialog.findViewById(R.id.addCash);

            Button dialogClose = walletDialog.findViewById(R.id.DialogClose);

            int total = Integer.parseInt(added.getText().toString()) + Integer.parseInt(winning.getText().toString())
                    + Integer.parseInt(cashBonus.getText().toString());
            walletTotal.setText(Integer.toString(total));
            walletDialog.show();
            addCash.setOnClickListener(view -> {
                Intent intent = new Intent(tournament.this, getamount.class);
                intent.putExtra("totalAmount", total);
                intent.putExtra("mode", 0);
                startActivity(intent);
            });

            dialogClose.setOnClickListener(view -> walletDialog.dismiss());

        }catch (Exception e){
            StyleableToast.makeText(tournament.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }

    public void goneMethod(){

        shoemessage.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        mycontest.setVisibility(View.GONE);
        joinView.setVisibility(View.GONE);
        tournament_view.setVisibility(View.GONE);
        viewAll.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        shimmerFrameLayout.startShimmer();
        list.clear();
        joinList.clear();
    }



}

















