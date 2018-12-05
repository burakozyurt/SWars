package com.app.selfiewars.selfiewars;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;

public class GuessItActivity extends AppCompatActivity {
    private ImageView userProfileImageView;
    private TextView userNameTextView;
    private TextView joker1ValueTextView;
    private TextView joker2ValueTextView;
    private TextView timeValueTextView;
    private TextView scoreValueTextView;
    private LottieAnimationView joker1btn;
    private LottieAnimationView joker2btn;
    private LottieAnimationView btn1Lottie;
    private LottieAnimationView btn2Lottie;
    private LottieAnimationView btn3Lottie;
    private LottieAnimationView btn4Lottie;
    private TextView btn1TextView;
    private TextView btn2TextView;
    private TextView btn3TextView;
    private TextView btn4TextView;
    private Button passButton;
    private Boolean IsButtonAnimationIn;


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
        setButtonAnimationListener();
        IsButtonAnimationIn = true;
        btn1Lottie.playAnimation();
        setButtonOnClickListener();


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
        btn1Lottie = findViewById(R.id.guessit_btn1_lottieAnimationView);
        btn2Lottie = findViewById(R.id.guessit_btn2_lottieAnimationView);
        btn3Lottie = findViewById(R.id.guessit_btn3_lottieAnimationView);
        btn4Lottie = findViewById(R.id.guessit_btn4_lottieAnimationView);
        btn1TextView = findViewById(R.id.guessit_btn1_TextView);
        btn2TextView = findViewById(R.id.guessit_btn2_TextView);
        btn3TextView = findViewById(R.id.guessit_btn3_TextView);
        btn4TextView = findViewById(R.id.guessit_btn4_TextView);
        passButton = findViewById(R.id.guessit_pass_Button);
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
                Toast.makeText(GuessItActivity.this, "Başladı", Toast.LENGTH_SHORT).show();
                if(!IsButtonAnimationIn){
                    btn1TextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Toast.makeText(GuessItActivity.this, "Bitti", Toast.LENGTH_SHORT).show();
                btn2Lottie.playAnimation();
                if (IsButtonAnimationIn){
                    btn1TextView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Toast.makeText(GuessItActivity.this, "İptal", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this, ""+btn1Lottie.getDuration(), Toast.LENGTH_SHORT).show();
        }
    }
}
