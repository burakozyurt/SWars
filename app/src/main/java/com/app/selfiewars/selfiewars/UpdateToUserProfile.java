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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class UpdateToUserProfile extends AppCompatActivity {

    private EditText updated_name;
    private EditText updated_surname;
    private EditText updated_telephone;
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
                if (!updated_name.getText().toString().isEmpty() && !updated_surname.getText().toString().isEmpty() && !updated_telephone.getText().toString().isEmpty()
                        && !updated_gender.getText().toString().isEmpty() && !updated_day.getText().toString().isEmpty() && !updated_month.getText().toString().isEmpty()
                        && !updated_year.getText().toString().isEmpty()
                        && !updated_adress.getText().toString().isEmpty()) {

                    final UserInfoDetails userInfoDetails = new UserInfoDetails();
                    userInfoDetails.setDisplay_name(updated_name.getText().toString());
                    userInfoDetails.setDisplay_telephone(Long.valueOf(updated_telephone.getText().toString()));
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
                                        MainActivity.showPopUpInfo(null, "Bilgiler Başarıyla Güncellendi.", null, UpdateToUserProfile.this);
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
                                                        MainActivity.showPopUpInfo(R.drawable.diamond, "Bilgiler Başarıyla Güncellendi.", "Tebrikler " + diamondValue +" kazandınız!", UpdateToUserProfile.this);

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
        builder.setTitle("Choose Gender");
        String[] gender = {"Male", "Female"};
        builder.setItems(gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: updated_gender.setText("Male");
                    break;
                    case 1: updated_gender.setText("Female");
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


}

