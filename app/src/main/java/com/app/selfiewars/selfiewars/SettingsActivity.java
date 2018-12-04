package com.app.selfiewars.selfiewars;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    LinearLayout follow;
    LinearLayout updatetouserprofile;
    LinearLayout useragreement;
    LinearLayout about;
    LinearLayout logout;
    ImageView backspace;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsItemsCreated();
        backspaceIconClicked();
        followClicked();
        updateToUserProfileClicked();
        logoutClicked();
    }

    public void settingsItemsCreated(){
        follow = findViewById(R.id.followLinearLayout);
        updatetouserprofile = findViewById(R.id.updatetouserprofileLinearLayout);
        useragreement = findViewById(R.id.useragreementLinearLayout);
        about = findViewById(R.id.aboutLinearLayout);
        logout = findViewById(R.id.logoutLinearLayout);
        backspace = findViewById(R.id.backspaceIcon);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void backspaceIconClicked(){
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void followClicked(){
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/"));
                startActivity(intent);
            }
        });
    }
    public void updateToUserProfileClicked(){
        updatetouserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UpdateToUserProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void logoutClicked(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                    }
                });
                Intent intent = new Intent(getApplicationContext(),SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public GoogleSignInClient getClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_lient_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        return mGoogleSignInClient;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();

    }

}
