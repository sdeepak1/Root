package com.example.root;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import com.muddzdev.styleabletoastlibrary.StyleableToast;



public class login extends AppCompatActivity {
    //declare all data types
    EditText getEmail,getPassword,user_email;
    private FirebaseAuth firebaseAuth;
    TextView clickRegister,forget;
    ScrollView scrollView;
    loading_dialog loading_dialog;
    ConstraintLayout constraintLayout;//forget password dialog box
    int stage=0;//for check dialog stage visible or not



    FirebaseUser firebaseUser;
    FirebaseAuth auth;




    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();





        //call definition function
        init();

        forget.setOnClickListener(view -> {
            scrollView.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            stage=1;

        });

    }

    //definition of all datatype
    public void init(){
        getEmail=findViewById(R.id.loginGetEmail);
        getPassword=findViewById(R.id.loginGetPassword);
        firebaseAuth=FirebaseAuth.getInstance();
        clickRegister=findViewById(R.id.clickRagister);
        forget=findViewById(R.id.forget);
        scrollView=findViewById(R.id.scroll);
        constraintLayout=findViewById(R.id.forget_dialog);
        user_email=findViewById(R.id.login_user_email);
        loading_dialog=new loading_dialog(login.this);
    }


    public void newUSer(View view){
        Intent intent=new Intent(login.this,signup.class);
        startActivity(intent);
        finish();
    }
    public void ragister(View View){
        try {
            if (getEmail.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail.getText().toString()).matches()) {
                getEmail.setError("enter a valid email address");
            } else {
                getEmail.setError(null);
            }

            if (getPassword.getText().toString().isEmpty()) {
                getPassword.setError("Please Enter Password");
                return;
            }

            String email, password;
            email = getEmail.getText().toString();
            password = getPassword.getText().toString();
            loading_dialog.startLoading();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    StyleableToast.makeText(login.this, "login Successfully", R.style.styleGreenToast).show();
                    loading_dialog.endLoading();
                    Intent intent = new Intent(login.this, tournament.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(e -> {
                loading_dialog.endLoading();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    StyleableToast.makeText(login.this, "Password Is Wrong ", R.style.styleGreenToast).show();
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    StyleableToast.makeText(login.this, "Invalid Email", R.style.styleGreenToast).show();

                } else {
                    StyleableToast.makeText(login.this, e.getMessage(), R.style.styleRedToast).show();

                }
            });
        }catch (Exception e){
            StyleableToast.makeText(login.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }



    }


    public void close_forget_dialog(View view){
        scrollView.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.GONE);
        stage=0;
    }

    public void Send_Link(View view){
        try {
            if (user_email.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail.getText().toString()).matches()) {
                user_email.setError("Please Enter valid Email Address");
                return;
            }

            loading_dialog.startLoading();
            firebaseAuth.sendPasswordResetEmail(user_email.getText().toString()).addOnCompleteListener(task -> {
                StyleableToast.makeText(login.this, "Successfully Check Your Gmail", R.style.styleGreenToast).show();
                close_forget_dialog(view);
                loading_dialog.endLoading();

            }).addOnFailureListener(e -> {
                close_forget_dialog(view);
                loading_dialog.endLoading();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    StyleableToast.makeText(login.this, "This Email Not Exits in our database", R.style.styleRedToast).show();

                } else {
                    StyleableToast.makeText(login.this, "Something went wrong", R.style.styleRedToast).show();

                }
            });
        }catch (Exception e){
            StyleableToast.makeText(login.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();


        }

    }

    @Override
    public void onBackPressed() {
        //if any dialog visible so close dialog first
        if(stage==1){
            scrollView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            stage=0;
        }
        finish();
    }


}