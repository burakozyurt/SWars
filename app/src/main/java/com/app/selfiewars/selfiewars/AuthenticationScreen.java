package com.app.selfiewars.selfiewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class AuthenticationScreen extends Activity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRefAccountState;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private Button googleSignImageView;
    private LottieAnimationView signloadanim;
    private TextView signtext;
    private TextView signGoogleTextView;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_screen);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefAccountState = database.getReference("Users");
        googleSignImageView = findViewById(R.id.googlesign_imageview);
        signGoogleTextView = findViewById(R.id.auth_screen_sign_with_google_textView);
        googleSignInOptions();
        googleSignImageView.setVisibility(View.GONE);
        signGoogleTextView.setVisibility(View.GONE);
        googleSignImageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   signIn();
               }
           });
        signGoogleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        signtext = findViewById(R.id.auth_screen_sign_text);
        signloadanim = findViewById(R.id.auth_screen_sign_lottie);
        setSignProgresVisiblity(false);
        animation = AnimationUtils.loadAnimation(this,R.anim.smalltobig);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                googleSignImageView.startAnimation(animation);
                googleSignImageView.setVisibility(View.VISIBLE);
                signGoogleTextView.startAnimation(animation);
                signGoogleTextView.setVisibility(View.VISIBLE);

            }
        };
        handler.postDelayed(runnable,400);
    }
    private void  setSignProgresVisiblity(boolean visiblity){
        if(visiblity){
            signloadanim.setVisibility(View.VISIBLE);
            signtext.setVisibility(View.VISIBLE);
        }else {
            signloadanim.setVisibility(View.GONE);
            signtext.setVisibility(View.GONE);
        }
    }
    public void googleSignInOptions (){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_lient_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(AuthenticationScreen.this,gso);
        }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        setSignProgresVisiblity(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            myRefAccountState.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Gmail hesabı ile giriş yapılmış fakat gerekli bilgilerin dolurulup doldurumladığını kontrol eder.
                                    if(dataSnapshot.exists()){
                                        Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                                        if(isReady){
                                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"Gmail ile giriş yapılmıştır. Lütfen gerekli bilgileri doldurunuz.",Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(getApplicationContext(),AuthUsersInfoActivity.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    }else {
                                        Boolean isReady = false;
                                        //İlk kayıtlı kullanıcının başarılı işlemi sonucu
                                        myRefAccountState.child(user.getUid()).child(getResources().getString(R.string.account_State)).child((getResources().getString(R.string.isReady))).setValue(isReady).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent i = new Intent(getApplicationContext(),AuthUsersInfoActivity.class);
                                                startActivity(i);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });

    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelfieWars.context = AuthenticationScreen.this;

    }
}
