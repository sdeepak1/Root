package com.example.root;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {
    //declare all variable
    EditText getName,getEmail,getMobile,getPassword,getDOB;
    TextInputLayout LayoutDob;
    CircleImageView getProfile;
    RadioGroup gender,radioGroup;
    RadioButton male,female,radioButton;
    TextView clickHere;
    Uri img;
    String downloadUrl,getGender;
    loading_dialog loading_dialog;
    DatePickerDialog.OnDateSetListener onDateSetListener;


    FirebaseAuth auth;
    final Calendar calendar=Calendar.getInstance();
    Dialog dialog;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();


    //for get referral user id
    String fromUserId="null";
    String cashBonus;
    int check=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        auth=FirebaseAuth.getInstance();
        loading_dialog=new loading_dialog(signup.this);

        initilaztion();
        getDOB.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog=new DatePickerDialog(signup.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,day,month,year);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        onDateSetListener= (view, year1, month1, day1) -> {
            month1 = month1 +1;
            String date= day1 +"/"+ month1 +"/"+ year1;
            getDOB.setText(date);
        };
        clickHere.setOnClickListener(view -> {
            Intent intent=new Intent(signup.this,login.class);
            startActivity(intent);
            finish();
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId==R.id.radioButton){
                check=2;
            }
        });


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initilaztion(){
        LayoutDob=findViewById(R.id.textInputDOB);
        getName=findViewById(R.id.getName);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        getEmail=findViewById(R.id.getEmail);
        getMobile=findViewById(R.id.getMobile);
        getPassword=findViewById(R.id.getPassword);
        getDOB=findViewById(R.id.getDOB);
        getProfile=findViewById(R.id.getProfile);
        gender=findViewById(R.id.gender);
        clickHere=findViewById(R.id.moveToLogin);
        male.setChecked(true);
        getGender="male";

        radioGroup=findViewById(R.id.radioGroup);
        radioButton=findViewById(R.id.radioButton);

        //for dialog box
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.codedialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.unvisible));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        showReferralDialog();

    }

   public void showReferralDialog(){
        try {
            Button Next = dialog.findViewById(R.id.Next);
            Button Skip = dialog.findViewById(R.id.Skip);
            EditText getReferralCode = dialog.findViewById(R.id.getReferralCode);
            dialog.show();

            Next.setOnClickListener(view -> {
                if (getReferralCode.getText().toString().isEmpty()) {
                    getReferralCode.setError("Please Enter Referral Code");
                    return;
                }

                myRef.orderByChild("Referralcode").equalTo(getReferralCode.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            fromUserId = Objects.requireNonNull(snapshot.child("userid").getValue()).toString();
                            cashBonus = "60";
                            dialog.dismiss();
                        } else {
                            Toast.makeText(signup.this, "Invalid Referral code", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(signup.this, "Something Went wrong", Toast.LENGTH_SHORT).show();

                    }
                });
            });

            Skip.setOnClickListener(view -> {
                fromUserId = "null";
                cashBonus = "50";
                dialog.dismiss();
            });
        }catch (Exception e){
            StyleableToast.makeText(signup.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }

    }
    public void Ragister(View view){
        try {
            //validation of name
            if (getName.getText().toString().isEmpty() || getName.getText().toString().length() > 25) {
                getName.setError("Please Enter Valid name");
                return;
            }

            //validation for email address
            if (getEmail.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail.getText().toString()).matches()) {
                getEmail.setError("enter a valid email address");
            } else {
                getEmail.setError(null);
            }


            //validation for number
            if (getMobile.getText().toString().isEmpty() || getMobile.getText().toString().length() != 10) {
                try {
                    int n = Integer.parseInt(getMobile.getText().toString());
                    if (n <= 1253) {
                        //check validation
                        StyleableToast.makeText(signup.this, "validation successfully", R.style.styleRedToast);
                    }
                } catch (Exception e) {
                    getMobile.setError("Please Enter Your valid Mobile Number");
                    return;
                }
                getMobile.setError("Please Enter Your valid Mobile Number");
                return;

            }


            //validation password
            if (getPassword.getText().toString().isEmpty() || !isValidPassword(getPassword.getText().toString()) || getPassword.getText().toString().length() < 8) {
                getPassword.setError("please enter valid password");
                return;

            }


            if (getDOB.getText().toString().isEmpty() || getDOB.getText().toString().length() != 8) {
                getDOB.setError("Please Enter valid date of birth");
                return;
            }

            if (img == null) {
                StyleableToast.makeText(signup.this, "Please Select Image", R.style.styleRedToast).show();
                return;
            }


            gender.setOnCheckedChangeListener((radioGroup, i) -> {
                if (i == R.id.male) {
                    getGender = "male";
                } else if (i == R.id.female) {
                    getGender = "female";
                }
            });

            if (check == 1) {
                radioButton.setError("Please check term and condition");
                return;
            }


            String email, password;
            email = getEmail.getText().toString();
            password = getPassword.getText().toString();

            loading_dialog.startLoading();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UploadData();
                }
            }).addOnFailureListener(e -> {
                loading_dialog.endLoading();
                if (e instanceof FirebaseAuthUserCollisionException) {
                    StyleableToast.makeText(signup.this, "Email Already Exits Try Different Email", R.style.styleRedToast).show();
                } else {
                    StyleableToast.makeText(signup.this, e.getMessage(), R.style.styleRedToast).show();

                }

            });
        }catch (Exception e){
            StyleableToast.makeText(signup.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

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
                getProfile.setImageURI(img);
            }
        }
    }

    public void UploadData() {
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference imageRefrence = storageReference.child("usersProfile").child(Objects.requireNonNull(auth.getUid()));

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
                        deleteUser();
                        loading_dialog.endLoading();
                        StyleableToast.makeText(signup.this, "Failed To Upload Image", R.style.styleRedToast).show();
                    }
                });
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    task.getResult();
                } else {
                    // Handle failures
                    // ...
                    deleteUser();
                    loading_dialog.endLoading();
                    StyleableToast.makeText(signup.this, "Failed To Upload Image", R.style.styleRedToast).show();

                }
            });
        }catch (Exception e){
            StyleableToast.makeText(signup.this,"something went wrong"+e.getMessage(),R.style.styleRedToast).show();

        }


    }

    public void upload(){
        try {
            String referral = createUniqueId();

            Map<String, Object> map = new HashMap<>();
            map.put("UserId", auth.getUid());
            map.put("UserName", getName.getText().toString());
            map.put("UserEmail", getEmail.getText().toString());
            map.put("UserImage", downloadUrl);
            map.put("UserGender", getGender);
            map.put("UserDOB", getDOB.getText().toString());
            map.put("UserAddedAmount", "0");
            map.put("UserWinningAmount", "0");
            map.put("UserCashBonus", cashBonus);
            map.put("FromUserId", fromUserId);
            map.put("ReferralId", referral);//call generate referral id number;
            map.put("UserMobile", getMobile.getText().toString());


            FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(auth.getUid())).set(map).addOnCompleteListener(task -> {
                //for referral id
                Map<String, Object> maps = new HashMap<>();
                maps.put("userid", auth.getUid());

                myRef.child("Referralcode").child(referral).setValue(maps).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        loading_dialog.endLoading();
                        StyleableToast.makeText(signup.this, "Successfully To Create Account", R.style.styleGreenToast).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(signup.this, login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        deleteUser();
                        Toast.makeText(signup.this, "Something went wrong Please try again", Toast.LENGTH_SHORT).show();
                        upload();
                    }
                });


            }).addOnFailureListener(e -> {
                loading_dialog.endLoading();
                StyleableToast.makeText(signup.this, "Failed To Store User Data", R.style.styleRedToast).show();

            });

        }catch (Exception e)

    {
        StyleableToast.makeText(signup.this, "something went wrong" + e.getMessage(), R.style.styleRedToast).show();
    }




    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(signup.this,login.class);
        startActivity(intent);
        finish();
    }

    public String createUniqueId(){
        StringBuilder stringBuilder = new StringBuilder(10);
        try {
            String ReferralId = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "1234567890";

            for (int i = 0; i < 10; i++) {
                int index = (int) (ReferralId.length() * Math.random());
                stringBuilder.append(ReferralId.charAt(index));
            }

            myRef.orderByChild("Referralcode").equalTo(stringBuilder.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        createUniqueId();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    createUniqueId();
                }
            });

        }catch (Exception e){
            StyleableToast.makeText(signup.this, "something went wrong" + e.getMessage(), R.style.styleRedToast).show();

        }
        return stringBuilder.toString();
    }

    public void deleteUser(){
        try {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            AuthCredential credential = EmailAuthProvider.getCredential(getEmail.getText().toString(), getPassword.getText().toString());

            // Prompt the user to re-provide their sign-in credentials
            if (user != null) {
                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> user.delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(signup.this, "delete user successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(signup.this, "Something went wrong to delete user", Toast.LENGTH_SHORT).show();
                                    }
                                }));
            }
        }catch (Exception e){
            StyleableToast.makeText(signup.this, "something went wrong" + e.getMessage(), R.style.styleRedToast).show();

        }

    }

    //for password validation
    public static boolean isValidPassword(final String password) {

            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);


            return matcher.matches();


    }



}