package com.example.root;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class invite extends AppCompatActivity {

    TextView myCode;
    ImageView shareImage;
    String downloadLink;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        myCode=findViewById(R.id.referCode);

        String Referral=tournament.userList.get(0).getReferralCode();

        myCode.setText(Referral);
        shareImage=findViewById(R.id.forShareOnly);
        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);
        TextView title = findViewById(R.id.toolbar_title);

        wallet.setVisibility(View.GONE);
        title.setText("Invite Friend");

        //call adLoad method
        BannerAds();

        back.setOnClickListener(v -> back());
         //toolbar coding end here

        myCode.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copy",myCode.getText().toString());
            clipboard.setPrimaryClip(clip);
            StyleableToast.makeText(invite.this,"text copied",R.style.styleGreenToast).show();
        });
    }

    private  void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    //share app only omn whatsapp
    @SuppressLint("IntentReset")
    public void whatsApp(View view){
        try{
            myRef.child("AppData").child("DownloadLink").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    downloadLink= Objects.requireNonNull(snapshot.child("url").getValue()).toString();
                    BitmapDrawable drawable=(BitmapDrawable) shareImage.getDrawable();
                    Bitmap bitmap=drawable.getBitmap();
                    Uri bmpUri;
                    bmpUri=saveImage(bitmap);

                    String testToShare="I have gifted you 60Rs to join contest on ROOT App.Think you can win contest? And Win Real cash \n" +
                            "1.Download the ROOT App  from here: "+downloadLink+"\n"+
                            "2.Use my invite code "+tournament.userList.get(0).getReferralCode()+"\n"+
                            "3.Get 60rs User cash Bonus\n"+
                            "Let show  your gaming skills and win real cash";

                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Refer A Friend");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM,bmpUri);
                    intent.putExtra(Intent.EXTRA_TEXT,testToShare);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(intent,""));
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    StyleableToast.makeText(invite.this,"Some thing went wrong",R.style.styleRedToast).show();
                }
            });
        }
        catch (Exception e){
            StyleableToast.makeText(invite.this,e.getMessage(),R.style.styleRedToast).show();
        }

    }

    //share app for all share platform
    public void moreOption(View view){
        myRef.child("AppData").child("DownloadLink").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadLink= Objects.requireNonNull(snapshot.child("url").getValue()).toString();
                BitmapDrawable drawable=(BitmapDrawable) shareImage.getDrawable();
                Bitmap bitmap=drawable.getBitmap();

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                Uri bmpUri;
                String testToShare="I have gifted you 60Rs to join contest on ROOT App.Think you can win contest? And Win Real cash \n" +
                        "1.Download the ROOT App  from here: "+downloadLink+"\n"+
                        "2.Use my invite code "+tournament.userList.get(0).getReferralCode()+"\n"+
                        "3.Get 60rs User cash Bonus\n"+
                        "Let show  your gaming skills and win real cash";
                bmpUri=saveImage(bitmap);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM,bmpUri);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Refer And Earn");
                intent.putExtra(Intent.EXTRA_TEXT,testToShare);
                invite.this.startActivity(Intent.createChooser(intent,"Share App"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                StyleableToast.makeText(invite.this,"something went wrong"+error.getMessage(),R.style.styleRedToast).show();

            }
        });

    }
    public void work(View view){
        Intent intent10=new Intent(invite.this,website.class);
        intent10.putExtra("name","How invite work");
        myRef.child("AppData").child("invite").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent10.putExtra("url", Objects.requireNonNull(snapshot.child("url").getValue()).toString());
                startActivity(intent10);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                StyleableToast.makeText(invite.this,"Something went wrong",R.style.styleRedToast).show();
            }
        });
    }
    public Uri saveImage(Bitmap image){
        File imageFolder=new File(invite.this.getCacheDir(),"images");
        Uri uri=null;
        try{
            imageFolder.mkdir();
            File file=new File(imageFolder,"share_image.jpg");

            FileOutputStream stream=new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(Objects.requireNonNull(invite.this.getApplicationContext()),
                    "com.example.root"+".provider",file);

        }catch (IOException e){
            Toast.makeText(invite.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}