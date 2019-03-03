package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class AuthUsersInfoActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;
    private DatabaseReference myRefAward;
    private DatabaseReference myRefRightOfGame;
    private DatabaseReference   myRefUserName;
    private DatabaseReference myRefRequest;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    private UserProperties userProperties;
    private ImageView profileImageView;
    private LottieAnimationView loadlottie;
    private TextView signupText;
    private Button continueButton;
    private Uri selectedImage;
    private EditText usernameText;
    private EditText ageText;
    private EditText genderText;
    private TextView chooseprofileText;
    private String userName;
    private Integer age;
    private String photoUrl;
    private final Integer firstRightOfGameValue = 10;
    private Animation animation;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_users_info);
        profileImageView = findViewById(R.id.signin_info_profile_imageview);
        usernameText =findViewById(R.id.signin_info_username_editText);
        ageText = findViewById(R.id.signin_info_age_editText);
        chooseprofileText = findViewById(R.id.signin_profile_desc_textView);
        loadlottie = findViewById(R.id.auth_info_screen_sign_lottie2);
        signupText = findViewById(R.id.auth_info_screen_sign_text3);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference("Users/"+mAuth.getUid()+"/profileimage/image.jpg");
        userProperties = new UserProperties();
        continueButton = findViewById(R.id.signin_info_continue_button);
        myRefUser = database.getReference("Users/" + mAuth.getUid());
        myRefAward = database.getReference("FirstAward");
        myRefRightOfGame = database.getReference("RightOfGame");
        myRefUserName = database.getReference("UserName");
        myRefRequest = database.getReference("Requests");
        continueButtonClick();
        animation = AnimationUtils.loadAnimation(this,R.anim.smalltobig);
        profileImageView.startAnimation(animation);
        mInterstitialAd = new InterstitialAd(this);
        MobileAds.initialize(AuthUsersInfoActivity.this, "ca-app-pub-7004761147200711~5104636223");
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-7004761147200711/6381566949");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        setmInterstitialAd();
    }
    public void setmInterstitialAd(){
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void continueButtonClick(){
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UserProperties userProperties = new UserProperties();
                if(isFilled()){
                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    signupText.setVisibility(View.VISIBLE);
                    loadlottie.setVisibility(View.VISIBLE);
                    userName = usernameText.getText().toString();
                    age = Integer.valueOf(ageText.getText().toString());
                    myRefUserName.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                mStorageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                photoUrl = uri.toString();
                                                userProperties.setAge(age);
                                                userProperties.setDisplayName(mAuth.getCurrentUser().getDisplayName());
                                                userProperties.setEmail(mAuth.getCurrentUser().getEmail());
                                                userProperties.setPhotoUrl(photoUrl);
                                                userProperties.setUserName(userName);
                                                myRefUser.child(getString(R.string.properties)).setValue(userProperties).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        myRefAward.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                final FirstAward firstAward = dataSnapshot.getValue(FirstAward.class);
                                                                myRefUser.child(getResources().getString(R.string.token)).child(getResources().getString(R.string.diamondValue)).setValue(firstAward.getDiamondValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Wildcards wildcards = new Wildcards(firstAward.getDoubleDipValue(),
                                                                                firstAward.getFiftyFiftyValue(),firstAward.getHealthValue());
                                                                        myRefUser.child(getResources().getString(R.string.wildcards)).setValue(wildcards).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                myRefRightOfGame.child(mAuth.getUid()).setValue(firstRightOfGameValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        myRefUser.child("timestamp").child("guessItMilisecond").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                myRefUser.child("timestamp").child("guessItAdsCount").setValue(3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                        myRefUser.child("rightofspin").child("spinValue").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                myRefUser.child("rightofspin").child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        myRefUserName.child(userName).setValue(mAuth.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                                myRefRequest.child(mAuth.getUid()).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void aVoid) {
                                                                                                                                    myRefUser.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                                                            signupText.setText("Hesap Oluşturuldu.");
                                                                                                                                            if (mInterstitialAd.isLoaded()) {
                                                                                                                                                mInterstitialAd.show();
                                                                                                                                            } else {
                                                                                                                                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                                                                                                                                startActivity(i);
                                                                                                                                                finish();
                                                                                                                                            }

                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            });

                                                                                                                            }
                                                                                                                        });

                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        });

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });

                                                                            }
                                                                        });
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
                                        });
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        chooseprofileText.setText(progress.toString());
                                    }
                                });
                            }else {
                                showPopUpInfo("Kullanıcı adı kullanılmaktadır.","Girdiğiniz kullanıcı adı bir başkası tarafından kullanılmaktadır. Lütfen başka bir ad giriniz.");
                            }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
//*/
                }
            }
        });
    }
    public boolean isFilled(){
        if(!usernameText.getText().toString().isEmpty() && usernameText.getText().toString().length() > 4 && !ageText.getText().toString().isEmpty()
                && Integer.parseInt(ageText.getText().toString()) > 13 &&
                selectedImage !=null){
            return true;
        }
        else if(usernameText.getText().toString().length() <= 4) {
            showPopUpInfo("Karakter sayısı yetersiz!","'"+ usernameText.getText()+"' Kullanıcı adınız en az 5 karakterden oluşmalı.");
            return false;
        }else if( selectedImage ==null){
            showPopUpInfo("Fotoğraf Seçilmedi.",""+getResources().getString(R.string.sign_in_choose_image_alert_desc));
            return false;
        }else return false;
    }
    public void handlerInsertData(View view) {
        showPopUpInfoGallery(getResources().getString(R.string.sign_in_choose_image_alert_title),
                getResources().getString(R.string.sign_in_choose_image_alert_desc));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){

                }break;
            case 1:
                if (resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    Picasso.with(getApplicationContext()).load(selectedImage).transform(new ExifTransformation(this,selectedImage)).into(profileImageView);
                }break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    selectedImage = result.getUri();
                    if(profileImageView.getAnimation() !=null){
                        profileImageView.getAnimation().cancel();
                    }
                    Picasso.with(getApplicationContext()).load(selectedImage).networkPolicy(NetworkPolicy.OFFLINE).into(profileImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getApplicationContext()).load(selectedImage).networkPolicy(NetworkPolicy.OFFLINE).into(profileImageView);
                        }
                    });
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //Toast.makeText(this, "Seçilmedi Resim", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showPopUpInfo(String Title, String Description) {
        final Dialog mydialog = new Dialog(AuthUsersInfoActivity.this);
        mydialog.setContentView(R.layout.popup_info);
        mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        ImageView ımageView;
        TextView titleView;
        TextView descriptionView;
        Button btnOk;

        ımageView = mydialog.findViewById(R.id.popupInfo_Image);
        titleView = mydialog.findViewById(R.id.popupInfo_TitleTextView);
        descriptionView = mydialog.findViewById(R.id.popupInfo_descriptionTextView);
        btnOk = mydialog.findViewById(R.id.popupInfo_BtnOkey);
        ımageView.setVisibility(View.GONE);
        if (Title !=null)
            titleView.setText(Title);
        if(Description !=null) {
            descriptionView.setText(Description);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent pickerPhotoIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(pickerPhotoIntent,1);
                mydialog.dismiss();
            }
        });
        mydialog.setCanceledOnTouchOutside(false);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
    }
    private void showPopUpInfoGallery(String Title, String Description) {
        final Dialog mydialog = new Dialog(AuthUsersInfoActivity.this);
        mydialog.setContentView(R.layout.popup_info);
        mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        ImageView ımageView;
        TextView titleView;
        TextView descriptionView;
        Button btnOk;

        ımageView = mydialog.findViewById(R.id.popupInfo_Image);
        titleView = mydialog.findViewById(R.id.popupInfo_TitleTextView);
        descriptionView = mydialog.findViewById(R.id.popupInfo_descriptionTextView);
        btnOk = mydialog.findViewById(R.id.popupInfo_BtnOkey);
        ımageView.setVisibility(View.GONE);
        if (Title !=null)
            titleView.setText(Title);
        if(Description !=null) {
            descriptionView.setText(Description);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent pickerPhotoIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(pickerPhotoIntent,1);
                startCropImageActivity();
                mydialog.dismiss();
            }
        });
        mydialog.setCanceledOnTouchOutside(false);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
    }

    private void startCropImageActivity() {
        CropImage.activity().setMinCropResultSize(1000,1000).setMaxCropResultSize(2500,2500)
                .setBackgroundColor(R.drawable.gradient_color).setAspectRatio(3,4).setAutoZoomEnabled(false)
                .start(this);
    }
    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }
}
