package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;

public class UpdateToUserProfile extends AppCompatActivity {

    private EditText updated_name;
    private EditText updated_surname;
    private MaskedEditText updated_telephone;
    private EditText updated_gender;
    private EditText updated_adress;
    private EditText updated_day;
    private EditText updated_month;
    private EditText updated_year;
    private Button updated_updatebtn;
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private DatabaseReference myRefUserInfoAward;
    private FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_to_user_profile);
        updatedDataCreate();
        updateButtonClicked();
        genderClicked();
        getUserProfile();

    }

    public void updatedDataCreate() {
        updated_name = findViewById(R.id.settingsActivityNameEditText);
        updated_surname = findViewById(R.id.settingsActivitySurnameEditText);
        updated_telephone = findViewById(R.id.settingsActivityTelephoneNumberEditText);
        updated_gender = findViewById(R.id.settingsActivityGenderEditText);
        updated_day = findViewById(R.id.settingsActivityDateOfBirthEditTextDay);
        updated_month = findViewById(R.id.settingsActivityDateOfBirthEditTextMonth);
        updated_year = findViewById(R.id.settingsActivityDateOfBirthEditTextYear);
        updated_adress = findViewById(R.id.settingsActivityAdressEditText);
        updated_updatebtn = findViewById(R.id.settingsActivityUpdateButton);
        myDatabase = FirebaseDatabase.getInstance();
        myAuth = FirebaseAuth.getInstance();
        myRefUserInfoAward = myDatabase.getReference("UserInfoAward/" + getResources().getString(R.string.diamondValue));
        myRef = myDatabase.getReference("Users/" + myAuth.getUid());
    }

    public void updateButtonClicked() {
        updated_updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!updated_name.getText().toString().isEmpty() && !updated_surname.getText().toString().isEmpty() && !updated_telephone.getText().toString().isEmpty() && !updated_telephone.getUnmaskedText().contains("*")
                        && !updated_gender.getText().toString().isEmpty() && !updated_day.getText().toString().isEmpty() && !updated_month.getText().toString().isEmpty()
                        && !updated_year.getText().toString().isEmpty()
                        && !updated_adress.getText().toString().isEmpty()) {

                    final UserInfoDetails userInfoDetails = new UserInfoDetails();
                    userInfoDetails.setDisplay_name(updated_name.getText().toString() + " " + updated_surname.getText().toString());
                    userInfoDetails.setDisplay_telephone(Long.valueOf(updated_telephone.getUnmaskedText()));
                    userInfoDetails.setDisplay_gender(updated_gender.getText().toString());
                    userInfoDetails.setDisplay_dateofbirth(updated_day.getText().toString() + "/" + updated_month.getText().toString() + "/" + updated_year.getText().toString());
                    userInfoDetails.setDisplay_adress(updated_adress.getText().toString());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(getResources().getString(R.string.infodetail))) {
                                myRef.child(getResources().getString(R.string.infodetail)).setValue(userInfoDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       // Toast.makeText(UpdateToUserProfile.this, "Başarıyla Güncellendi", Toast.LENGTH_SHORT).show();
                                        MainActivity.showPopUpInfo(null, "Bilgiler Başarıyla Güncellendi.",null, UpdateToUserProfile.this);
                                    }
                                });
                            } else {
                                myRef.child(getResources().getString(R.string.infodetail)).setValue(userInfoDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        myRefUserInfoAward.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                final Integer diamondValue = dataSnapshot.getValue(Integer.class);
                                                myRef.child(getResources().getString(R.string.token)).child(getResources().getString(R.string.diamondValue)).runTransaction(new Transaction.Handler() {
                                                    @NonNull
                                                    @Override
                                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                        Integer i = mutableData.getValue(Integer.class);
                                                        i+=diamondValue;
                                                        mutableData.setValue(i);
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                       // Toast.makeText(UpdateToUserProfile.this, "Başarıyla Güncellendi", Toast.LENGTH_SHORT).show();
                                                        MainActivity.showPopUpInfo(null, "Bilgiler Başarıyla Güncellendi.", "Tebrikler " + diamondValue +" elmas kazandınız!", UpdateToUserProfile.this);

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
    private void genderClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateToUserProfile.this);
        builder.setTitle(getResources().getString(R.string.Choose_Gender));
        String[] gender = {getResources().getString(R.string.Gender_Male), getResources().getString(R.string.Gender_Female)};
        builder.setItems(gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: updated_gender.setText(getResources().getString(R.string.Gender_Male));
                    break;
                    case 1: updated_gender.setText(getResources().getString(R.string.Gender_Female));
                    break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        updated_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void getUserProfile(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getResources().getString(R.string.infodetail))) {
                    UserInfoDetails userInfoDetails = dataSnapshot.child(getResources().getString(R.string.infodetail)).getValue(UserInfoDetails.class);
                    Integer nameindx=userInfoDetails.getDisplay_name().indexOf(" ");
                    Integer surnameindx = userInfoDetails.getDisplay_name().lastIndexOf(" ");
                    updated_name.setText(userInfoDetails.getDisplay_name().substring(0,nameindx) + userInfoDetails.getDisplay_name().substring(nameindx,surnameindx));
                    String surname=userInfoDetails.getDisplay_name().substring(surnameindx + 1);
                    updated_surname.setText(surname);
                    updated_telephone.setMaskedText(userInfoDetails.getDisplay_telephone().toString());
                    updated_gender.setText(userInfoDetails.getDisplay_gender());
                    Integer dayindex = userInfoDetails.getDisplay_dateofbirth().indexOf("/");
                    String day = userInfoDetails.getDisplay_dateofbirth().substring(0,dayindex);
                    String month = userInfoDetails.getDisplay_dateofbirth().substring(dayindex + 1,userInfoDetails.getDisplay_dateofbirth().length());
                    Integer monthindex = month.indexOf("/");
                    month = month.substring(0,monthindex);
                    String year = userInfoDetails.getDisplay_dateofbirth().substring(monthindex+dayindex+2);
                    updated_day.setText(day);
                    updated_month.setText(month);
                    updated_year.setText(year);
                    updated_adress.setText(userInfoDetails.getDisplay_adress());

                }else {
                    if(dataSnapshot.hasChild(getResources().getString(R.string.properties)))
                    {
                        UserProperties userProperties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                        Integer nameindx=userProperties.getDisplayName().indexOf(" ");
                        Integer surnameindx = userProperties.getDisplayName().lastIndexOf(" ");
                        updated_name.setText(userProperties.getDisplayName().substring(0,nameindx) + userProperties.getDisplayName().substring(nameindx,surnameindx));
                        String surname=userProperties.getDisplayName().substring(surnameindx + 1);
                        updated_surname.setText(surname);
                        Date date = new Date(); // your date
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int year = cal.get(Calendar.YEAR);
                        updated_year.setText(""+(year - userProperties.getAge()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

