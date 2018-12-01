package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AuthUsersInfoActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;
    private DatabaseReference myRefAward;
    private DatabaseReference myRefRightOfGame;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    private UserProperties userProperties;
    private ImageView profileImageView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_users_info);
        profileImageView = findViewById(R.id.signin_info_profile_imageview);
        usernameText =findViewById(R.id.signin_info_username_editText);
        ageText = findViewById(R.id.signin_info_age_editText);
        chooseprofileText = findViewById(R.id.signin_profile_desc_textView);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference("Users/"+mAuth.getUid()+"/profileimage/image.jpg");
        userProperties = new UserProperties();
        continueButton = findViewById(R.id.signin_info_continue_button);
        myRefUser = database.getReference("Users/" + mAuth.getUid());
        myRefAward = database.getReference("FirstAward");
        myRefRightOfGame = database.getReference("RightOfGame");

        continueButtonClick();
    }
    public void continueButtonClick(){
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UserProperties userProperties = new UserProperties();
                if(isFilled()){
                 userName = usernameText.getText().toString();
                 age = Integer.valueOf(ageText.getText().toString());
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
                                        Toast.makeText(getApplicationContext(),"Hesap Oluşturuldu",Toast.LENGTH_LONG).show();
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
                                                                                    myRefUser.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                                                                            startActivity(i);
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
                }
            }
        });
    }
    public void uploadToFirebase(){

    }
    public boolean isFilled(){
        if(!usernameText.getText().toString().isEmpty() && usernameText.getText().toString().length() > 6 && !ageText.getText().toString().isEmpty()
                && Integer.parseInt(ageText.getText().toString()) > 15 &&
                !selectedImage.toString().isEmpty()){
            return true;
        }
        else return false;
    }
    public void handlerInsertData(View view) {
        Intent pickerPhotoIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickerPhotoIntent,1);
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
                    Picasso.get().load(selectedImage).transform(new ExifTransformation(this,selectedImage)).into(profileImageView);
                }break;
        }
    }
}
