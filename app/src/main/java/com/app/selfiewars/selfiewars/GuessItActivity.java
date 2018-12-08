package com.app.selfiewars.selfiewars;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class GuessItActivity extends AppCompatActivity {
    private ImageView userProfileImageView;
    private TextView userNameTextView;
    private TextView joker1ValueTextView;
    private TextView joker2ValueTextView;
    private TextView timeValueTextView;
    private TextView scoreValueTextView;
    private TextView loadingFeedBackTextView;
    private LottieAnimationView joker1btn;
    private LottieAnimationView joker2btn;
    private LottieAnimationView btn1Lottie;
    private LottieAnimationView btn2Lottie;
    private LottieAnimationView btn3Lottie;
    private LottieAnimationView btn4Lottie;
    private LottieAnimationView lottieAnimationView;
    private TextView btn1TextView;
    private TextView btn2TextView;
    private TextView btn3TextView;
    private TextView btn4TextView;
    private ConstraintLayout loadinglayout;
    private ConstraintLayout guessitlayout;
    private ConstraintLayout loselayout;
    private Button passButton;
    private Boolean IsButtonAnimationIn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myUserRef;
    private DatabaseReference mAllUsersRef;
    private DatabaseReference mApprovedUserRef;
    private DatabaseReference mCorrectUserRef;
    private OnGetDataListener onGetUserDataListener;
    private Integer score;
    private Long time;
    private boolean isGameStart;
    private Integer counQuestion;
    private List<UserProperties> listUserProperties;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_it);
        define();
        defineFirebase();
        setButtonAnimationListener();
        IsButtonAnimationIn = true;
        btn1Lottie.playAnimation();
        setButtonOnClickListener();
        getUserListFromDatabase();
    }
    private void defineFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myUserRef = mDatabase.getReference("Users/"+mAuth.getUid());
        mAllUsersRef = mDatabase.getReference("Users");
        mApprovedUserRef = mDatabase.getReference(getResources().getString(R.string.ApprovedUser));
        mCorrectUserRef = mDatabase.getReference(getResources().getString(R.string.CorrectUsers));
    }
    private void define(){
        userProfileImageView = findViewById(R.id.guessit_user_profile_imageView);
        userNameTextView = findViewById(R.id.guessit_user_profile_username_TextView);
        scoreValueTextView = findViewById(R.id.guessit_scoreValueTextView);
        timeValueTextView = findViewById(R.id.guessit_timeValueTextView);
        joker1btn = findViewById(R.id.guessit_joker1_lottieAnimationView);
        joker1ValueTextView = findViewById(R.id.guessit_joker1Value_TextView);
        joker2btn = findViewById(R.id.guessit_joker2_lottieAnimationView);
        joker2ValueTextView = findViewById(R.id.guessit_joker2Value_TextView);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        btn1Lottie = findViewById(R.id.guessit_btn1_lottieAnimationView);
        btn2Lottie = findViewById(R.id.guessit_btn2_lottieAnimationView);
        btn3Lottie = findViewById(R.id.guessit_btn3_lottieAnimationView);
        btn4Lottie = findViewById(R.id.guessit_btn4_lottieAnimationView);
        btn1TextView = findViewById(R.id.guessit_btn1_TextView);
        btn2TextView = findViewById(R.id.guessit_btn2_TextView);
        btn3TextView = findViewById(R.id.guessit_btn3_TextView);
        btn4TextView = findViewById(R.id.guessit_btn4_TextView);
        passButton = findViewById(R.id.guessit_pass_Button);
        loadingFeedBackTextView = findViewById(R.id.loading_feedback_TextView);
        loadinglayout = findViewById(R.id.guess_it_activity_loading_ConstrainLayout);
        guessitlayout = findViewById(R.id.guess_it_activity_guess_it_ConstrainLayout);
        loselayout = findViewById(R.id.guess_it_activity_lose_ConstrainLayout);
        btn1Lottie.setSpeed(10);
        btn2Lottie.setSpeed(10);
        btn3Lottie.setSpeed(10);
        btn4Lottie.setSpeed(10);
    }
    private void setButtonAnimationListener(){
        btn1Lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setButtonClickable(false);
              //  Toast.makeText(GuessItActivity.this, "Başladı", Toast.LENGTH_SHORT).show();
                if(!IsButtonAnimationIn){
                    btn1TextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
               // Toast.makeText(GuessItActivity.this, "Bitti", Toast.LENGTH_SHORT).show();
                btn2Lottie.playAnimation();
                if (IsButtonAnimationIn){
                    btn1TextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //Toast.makeText(GuessItActivity.this, "İptal", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        btn2Lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(!IsButtonAnimationIn) {
                    btn2TextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn3Lottie.playAnimation();
                if (IsButtonAnimationIn){
                    btn2TextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        btn3Lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(!IsButtonAnimationIn) {
                    btn3TextView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn4Lottie.playAnimation();
                if (IsButtonAnimationIn){
                    btn3TextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        btn4Lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(!IsButtonAnimationIn) {
                    btn4TextView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (IsButtonAnimationIn){
                    btn4TextView.setVisibility(View.VISIBLE);
                }
                setButtonClickable(true);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    private void setButtonOnClickListener(){
        btn1Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsButtonAnimationIn = !IsButtonAnimationIn;
                setButtonAnimation();
            }
        });
        btn2Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn3Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn4Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    private void setBtnTextVisibility(Boolean visibility){
        if(visibility){
            btn1TextView.setVisibility(View.VISIBLE);
            btn2TextView.setVisibility(View.VISIBLE);
            btn3TextView.setVisibility(View.VISIBLE);
            btn4TextView.setVisibility(View.VISIBLE);
        }
        else {
            btn1TextView.setVisibility(View.INVISIBLE);
            btn2TextView.setVisibility(View.INVISIBLE);
            btn3TextView.setVisibility(View.INVISIBLE);
            btn4TextView.setVisibility(View.INVISIBLE);
        }
    }
    private void setButtonClickable(Boolean clickable){
        btn1Lottie.setClickable(clickable);
        btn2Lottie.setClickable(clickable);
        btn3Lottie.setClickable(clickable);
        btn4Lottie.setClickable(clickable);
    }
    private void setButtonAnimation(){
        if (!btn1Lottie.isAnimating()&& !btn2Lottie.isAnimating() && !btn3Lottie.isAnimating()&& !btn4Lottie.isAnimating()){
            btn1Lottie.reverseAnimationSpeed();
            btn2Lottie.reverseAnimationSpeed();
            btn3Lottie.reverseAnimationSpeed();
            btn4Lottie.reverseAnimationSpeed();
            btn1Lottie.playAnimation();
           // Toast.makeText(this, ""+btn1Lottie.getDuration(), Toast.LENGTH_SHORT).show();
        }
    }
    private void getUserListFromDatabase(){
        new Database().mReadDataAndGetUserListForGuessIt(mAuth, getResources().getString(R.string.ApprovedUser), getResources().getString(R.string.CorrectUsers), new OnGetUserlistDataListener() {
            @Override
            public void onStart() {
                loadinglayout.setVisibility(View.VISIBLE);
                guessitlayout.setVisibility(View.GONE);
                loselayout.setVisibility(View.GONE);

            }

            @Override
            public void onProgress(String string) {
                loadingFeedBackTextView.append(" "+string);
            }

            @Override
            public void onSuccess(final List<UserProperties> userPropertiesList) {
                listUserProperties = userPropertiesList;
                Toast.makeText(GuessItActivity.this, "succes oldu size:"+userPropertiesList.size(), Toast.LENGTH_SHORT).show();
                final List<Bitmap> bitmapList = new ArrayList<>();
                for(UserProperties properties: listUserProperties){
                    Picasso.get().load(properties.getPhotoUrl()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Toast.makeText(GuessItActivity.this, "Oldu", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Toast.makeText(GuessItActivity.this, "Düzenleniyor", Toast.LENGTH_SHORT).show();

                        }
                    });
                   break;
                }
            }

            @Override
            public void onFailed() {

            }
        });
    }
    /*OnGetDataListener onGetDataListener = new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot data) {

            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        }*/
}
