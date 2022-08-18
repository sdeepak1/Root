package com.example.root;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;


import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class myprofile extends AppCompatActivity {

    //declare all variable
    EditText name,email,mobile,dob;
    CircleImageView image;
    RadioGroup gender;
    RadioButton male,female;
    String getGender;
    Uri img;
    String downloadUrl;
    loading_dialog loading_dialog;
    int stage=0;


    //firebase
    FirebaseFirestore firestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    Dialog dialog;
    Button updateButton,editProfile;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);


        MobileAds.initialize(this, initializationStatus -> {
        });
        init();
        BannerAds();
        //for dialog box
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.succes_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.unvisible));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        //firebase Database
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Users");
        documentReference=collectionReference.document(tournament.userList.get(0).getUserId());


        //toolbar start here
        Button back = findViewById(R.id.back);
        Button wallet = findViewById(R.id.wallet);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("My Profile");

        wallet.setVisibility(View.GONE);




        back.setOnClickListener(v -> back());
        //toolbar coding end here

        setData(); //Set User Data
    }

    //connect all variable  with data
    public void init(){

        name=findViewById(R.id.Profile_Name);
        email=findViewById(R.id.Profile_Email);
        mobile=findViewById(R.id.Profile_Mobile);
        dob=findViewById(R.id.Profile_DOB);
        image=findViewById(R.id.Profile_Image);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        gender=findViewById(R.id.get_gender);
        loading_dialog=new loading_dialog(myprofile.this);
        updateButton=findViewById(R.id.updateProfile);
        editProfile=findViewById(R.id.editProfile);


    }
    public void setData(){
        try {
            name.setText(tournament.userList.get(0).getUserName());
            email.setText(tournament.userList.get(0).getUserEmail());
            mobile.setText(tournament.userList.get(0).getUserMobile());
            dob.setText(tournament.userList.get(0).getUserDOB());
            Glide.with(myprofile.this).load(tournament.userList.get(0).getUserPicUrl()).into(image);

            if (tournament.userList.get(0).getUserGender().equals("male")) {
                male.setChecked(true);
                getGender = "male";
            } else {
                female.setChecked(true);
                getGender = "female";
            }
        }catch (Exception e){
            StyleableToast.makeText(myprofile.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }

    public void edit(View view){

        name.setEnabled(true);
        mobile.setEnabled(true);
        dob.setEnabled(true);
        male.setEnabled(true);
        female.setEnabled(true);
        updateButton.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.GONE);

    }
    public void update(View view){
        if(name.getText().toString().isEmpty()){
            name.setError("Please Enter Name");
            return;
        }
        if(email.getText().toString().isEmpty()){
            email.setError("Please Enter Email");
            return;
        }
        if(mobile.getText().toString().isEmpty()){
            mobile.setError("Please enter Number");
            return;
        }
        if(dob.getText().toString().isEmpty()){
            dob.setError("Please Enter Your Date Of Birth");
            return;
        }
        gender.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i==R.id.male){
                getGender="male";
            }else if(i==R.id.female){
                getGender="female";
            }
        });
        loading_dialog.startLoading();
        if(stage==0){
            upload();
        }else{
            UploadData();
        }


    }
    public void addImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                img = data != null ? data.getData() : null;
                image.setImageURI(img);
                stage=1;

            }
        }
    }

    public void UploadData() {
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference imageRefrence = storageReference.child("usersProfile").child(tournament.userList.get(0).getUserId());

            UploadTask uploadTask = imageRefrence.putFile(img);

            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                // Continue with the task to get the download URL
                return imageRefrence.getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        downloadUrl = Objects.requireNonNull(task1.getResult()).toString();
                        upload();
                    } else {
                        loading_dialog.endLoading();
                        StyleableToast.makeText(myprofile.this, "Failed To Upload Image", R.style.styleRedToast).show();
                    }
                });
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    task.getResult();
                } else {
                    // Handle failures
                    // ...
                    loading_dialog.endLoading();
                    StyleableToast.makeText(myprofile.this, "Failed To Upload Image", R.style.styleRedToast).show();

                }
            });
        }catch (Exception e){
            StyleableToast.makeText(myprofile.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }


    }

    @SuppressLint("SetTextI18n")
    public void upload(){
        try {
            documentReference.update("UserName", name.getText().toString());
            documentReference.update("UserEmail", tournament.userList.get(0).getUserEmail());
            if (stage == 0) {
                documentReference.update("UserImage", tournament.userList.get(0).getUserPicUrl());

            } else {
                documentReference.update("UserImage", downloadUrl);

            }

            documentReference.update("UserGender", getGender);
            documentReference.update("UserDOB", dob.getText().toString());
            documentReference.update("UserMobile", mobile.getText().toString());
            loading_dialog.endLoading();


            CircleImageView image = dialog.findViewById(R.id.setImage);
            Button Okay = dialog.findViewById(R.id.Done);
            TextView stage = dialog.findViewById(R.id.succes);
            TextView msg = dialog.findViewById(R.id.Message);


            image.setImageResource(R.drawable.succes);
            Okay.setText("Okay");
            Okay.setBackgroundResource(R.drawable.withdraw_bg);
            stage.setText("Success");
            stage.setTextColor(ContextCompat.getColor(myprofile.this, R.color.green));
            msg.setText("Profile Update Successfully");
            dialog.show();
            Okay.setOnClickListener(view -> {
                dialog.dismiss();
                back();

            });
        }catch (Exception e){
            StyleableToast.makeText(myprofile.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();
        }
    }


    private  void back(){
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }


    public void BannerAds(){
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


    }
}