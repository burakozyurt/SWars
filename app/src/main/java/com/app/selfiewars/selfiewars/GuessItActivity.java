package com.app.selfiewars.selfiewars;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GuessItActivity extends AppCompatActivity {
    private ImageView userProfileImageView;
    private ImageView loadTempImageView;
    private TextView userNameTextView;
    private TextView joker1ValueTextView;
    private TextView joker2ValueTextView;
    private TextView timeValueTextView;
    private TextView scoreValueTextView;
    private TextView loadingFeedBackTextView;
    private TextView loseLAyoutRightOfGameTextView;
    private LottieAnimationView joker1btn;
    private LottieAnimationView joker2btn;
    private LottieAnimationView btn1Lottie;
    private LottieAnimationView btn2Lottie;
    private LottieAnimationView btn3Lottie;
    private LottieAnimationView btn4Lottie;
    private LottieAnimationView healthAnimView;
    private LottieAnimationView lottieAnimationView;
    private TextView btn1TextView;
    private TextView btn2TextView;
    private TextView btn3TextView;
    private TextView btn4TextView;
    private TextView loseScore;
    private TextView playAgainTextView;
    private TextView questionCounTextView;
    private TextView healthValueTextView;
    private Button   homeButton;
    private ConstraintLayout loadinglayout;
    private ConstraintLayout guessitlayout;
    private ConstraintLayout loselayout;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myUserRef;
    private DatabaseReference myScoreRef;
    private DatabaseReference mAllUsersRef;
    private DatabaseReference mApprovedUserRef;
    private DatabaseReference mCorrectUserRef;
    private DatabaseReference myRightOfGame;
    private DatabaseReference myEndTime;
    private OnGetDataListener onGetUserDataListener;
    private Integer score = 0;
    private Long time;
    private boolean isGameStart;
    private Integer countQuestion = 1; //
    private List<GuessItUserData> listUserProperties;
    private Integer listUserPropertiesIndex = 0;
    private Integer allquestionsCount;
    private CountDownTimer countDowTimer;
    private List<Integer> optionsList;
    private Integer secondfiftyFityOption;
    private boolean isCalledJokerFiftyFifty = false;
    private boolean isCalledJokerDoubleDip = false;
    private boolean isCalledJokerHealth = false;
    private Integer joker1DecValue = 1;
    private Integer joker1UsingCount = 0;
    private Integer healthUsingCount = 0;
    private Integer joker2UsingCount = 0;
    private Integer joker2DecValue = 1;
    private Integer healthDecValue = 1;
    private Integer rightOfGame;
    private Handler healthHandler;
    private Runnable healthRunnable;
    private boolean isNextUserList = false;
    private boolean buttonClick = false;
    private boolean isBetaGame = false;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_it);
        define();
        defineFirebase();
        setJokerClickListener();
        setButtonAnimationListener();
        setButtonOnClickListener();
        getUserListFromDatabase();
        setLoselayoutItemsClickListener();
    }
    private void defineFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myUserRef = mDatabase.getReference("Users/"+mAuth.getUid());
        mAllUsersRef = mDatabase.getReference("Users");
        mApprovedUserRef = mDatabase.getReference(getResources().getString(R.string.ApprovedUser));
        mCorrectUserRef = mDatabase.getReference(getResources().getString(R.string.CorrectUsers));
        myScoreRef = mDatabase.getReference(getResources().getString(R.string.Scores));
        myRightOfGame = mDatabase.getReference("RightOfGame/" + mAuth.getUid());
        myEndTime = mDatabase.getReference("EndTime");
    }
    private void define(){
        userProfileImageView = findViewById(R.id.guessit_user_profile_imageView);
        loadTempImageView = findViewById(R.id.guess_it_load_imageView_TEMP);
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
        healthAnimView = findViewById(R.id.guess_it_health_lottieAnimView);
        btn1TextView = findViewById(R.id.guessit_btn1_TextView);
        btn2TextView = findViewById(R.id.guessit_btn2_TextView);
        btn3TextView = findViewById(R.id.guessit_btn3_TextView);
        btn4TextView = findViewById(R.id.guessit_btn4_TextView);
        playAgainTextView = findViewById(R.id.guess_it_lose_play_again_TextView);
        loseLAyoutRightOfGameTextView = findViewById(R.id.guess_it_lose_right_of_game_textView);
        questionCounTextView = findViewById(R.id.guess_it_question_count_TextView);
        homeButton = findViewById(R.id.guess_it_lose_home_Button);
        loseScore = findViewById(R.id.guess_it_lose_score_textView);
        loadingFeedBackTextView = findViewById(R.id.loading_feedback_TextView);
        loadinglayout = findViewById(R.id.guess_it_activity_loading_ConstrainLayout);
        guessitlayout = findViewById(R.id.guess_it_activity_guess_it_ConstrainLayout);
        loselayout = findViewById(R.id.guess_it_activity_lose_ConstrainLayout);
        healthValueTextView = findViewById(R.id.guess_it_health_TextView);
        btn1Lottie.setSpeed(3);
        btn2Lottie.setSpeed(3);
        btn3Lottie.setSpeed(3);
        btn4Lottie.setSpeed(3);
        scoreValueTextView.setText(""+0);
        joker1SetClickableAndVisible(false);
        joker2SetClickableAndVisible(false);
    }
    private void gameManagement(){
        if(isGameStart){
            if(listUserProperties != null && listUserPropertiesIndex < allquestionsCount){
                setUserImageAndUserName(listUserProperties.get(listUserPropertiesIndex));
                if(listUserPropertiesIndex < allquestionsCount-1){
                    loadNextImage(listUserPropertiesIndex + 1);
                }else {
                    getSecondUserListFromDatabase();
                }
            }else {
                isGameStart = false;
                MainActivity.showPopUpInfo(null,"Tüm kullanıcıların yaşları tahmin edildi","Veritbanındaki tüm onaylı kullanıcıların yaşını tahmin ettiniz. Yeni kullanıcılar kısa sürede eklenecektir.Puanların hesabına eklendi. Daha sonra tekrar gel",GuessItActivity.this);
                setScoreDataInFirebaseAndLoseLayoutOpen();

            }
        }
    }

    private void setButtonOnClickListener(){

        btn1Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGameStart){
                    setButtonClickable(false);
                    selectOptionAndWaitCorrectAnswer(btn1TextView);
                    countDowTimer.cancel();
                }
            }
        });
        btn2Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isGameStart){
                   setButtonClickable(false);
                   selectOptionAndWaitCorrectAnswer(btn2TextView);
                   countDowTimer.cancel();
               }

            }
        });
        btn3Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGameStart){
                    setButtonClickable(false);
                    selectOptionAndWaitCorrectAnswer(btn3TextView);
                    countDowTimer.cancel();
                }

            }
        });
        btn4Lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGameStart){
                    setButtonClickable(false);
                    selectOptionAndWaitCorrectAnswer(btn4TextView);
                    countDowTimer.cancel();
                }

            }
        });

    }
    private boolean isAnswerCorrect(String buttonText){
        if (buttonText.equalsIgnoreCase(listUserProperties.get(listUserPropertiesIndex).getAge().toString())){
            return true;
        }else {
            return false;
        }

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
    private void setButtonsText(List<Integer> ıntegerList){
        optionsList = ıntegerList;
        btn1TextView.setText(""+ıntegerList.get(0).toString());
        btn2TextView.setText(""+ıntegerList.get(1).toString());
        btn3TextView.setText(""+ıntegerList.get(2).toString());
        btn4TextView.setText(""+ıntegerList.get(3).toString());
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
    private void selectOptionAndWaitCorrectAnswer(final TextView btnTextView){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(isAnswerCorrect(btnTextView.getText().toString())){
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            setScore();
                            countQuestion++;
                            listUserPropertiesIndex++;
                            gameManagement();
                        }
                    };
                    if (!isBetaGame){
                        mCorrectUserRef.child(mAuth.getUid()).child(listUserProperties.get(listUserPropertiesIndex).getUid()).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                handler.postDelayed(runnable,1000);
                                btnTextView.setBackgroundResource(R.drawable.guess_it_true_answer);
                            }
                        });
                    }else {
                        handler.postDelayed(runnable,1000);
                        btnTextView.setBackgroundResource(R.drawable.guess_it_true_answer);
                    }
                    joker1btn.setClickable(false);
                    joker2btn.setClickable(false);

                }else {
                    isGameStart = false;
                    if(isCalledJokerDoubleDip){
                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                isGameStart = true;
                                eliminateOneOption(btnTextView);
                            }
                        };
                        handler.postDelayed(runnable,500);
                        btnTextView.setBackgroundResource(R.drawable.guess_it_false_answer);
                    }else {
                        btnTextView.setBackgroundResource(R.drawable.guess_it_false_answer);
                        healthSetClickableAndVisible(true);
                        healthHandler = new Handler();
                        healthRunnable = new Runnable() {
                            @Override
                            public void run() {
                                healthSetClickableAndVisible(false);
                                setScoreDataInFirebaseAndLoseLayoutOpen();
                            }
                        };
                        healthHandler.postDelayed(healthRunnable,4000);
                    }
                }
                buttonClick = false;
            }
        };
        if(!buttonClick) {
            buttonClick = true;
            handler.postDelayed(runnable,1000);
            btnTextView.setBackgroundResource(R.drawable.guess_it_selected_answer);
        }

    }
    private void setScoreDataInFirebaseAndLoseLayoutOpen(){
        myScoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mAuth.getUid())){
                    myScoreRef.child(mAuth.getUid()).runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            if(score != 0){
                                Integer dbscore = mutableData.getValue(Integer.class);
                                dbscore +=score;
                                mutableData.setValue(dbscore);
                                return Transaction.success(mutableData);
                            }else {
                                return null;
                            }
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            guessitlayout.setVisibility(View.GONE);
                            loselayout.setVisibility(View.VISIBLE);
                            loseScore.setText("" + score + " Puan");
                        }
                    });
                }else {
                    myScoreRef.child(mAuth.getUid()).setValue(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            guessitlayout.setVisibility(View.GONE);
                            loselayout.setVisibility(View.VISIBLE);
                            loseScore.setText("" + score +" Puan");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setUserImageAndUserName(final GuessItUserData guessItUserData){
            userNameTextView.setVisibility(View.INVISIBLE);
            Picasso.get().load(guessItUserData.getPhotoUrl()).networkPolicy(NetworkPolicy.NO_STORE,NetworkPolicy.NO_CACHE).into(userProfileImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            setButtonsText(options(listUserProperties.get(listUserPropertiesIndex).getAge()));
                            playAnimationButtons();
                            userNameTextView.setText(guessItUserData.getUserName());
                            userNameTextView.setVisibility(View.VISIBLE);
                            startCountDownTimer();
                            setJokerReset();
                            questionCounTextView.setText(""+countQuestion);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(GuessItActivity.this, "Hata resim yüklenmedi", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
    private void setButtonAnimationListener(){
        btn1Lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setButtonClickable(false);
                btn1TextView.setVisibility(View.INVISIBLE);
                btn1TextView.setBackgroundResource(R.drawable.guess_it_transparent_answer);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setButtonClickable(true);
                btn1TextView.setVisibility(View.VISIBLE);
                if(isCalledJokerFiftyFifty || isCalledJokerDoubleDip){
                    btn1Lottie.reverseAnimationSpeed();
                    btn1Lottie.setClickable(false);
                    btn1TextView.setVisibility(View.INVISIBLE);
                    isCalledJokerDoubleDip = false;
                    joker2btn.setClickable(true);
                    joker1btn.setClickable(true);
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
                setButtonClickable(false);
                btn2TextView.setVisibility(View.INVISIBLE);
                btn2TextView.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setButtonClickable(true);
                btn2TextView.setVisibility(View.VISIBLE);
                if(isCalledJokerFiftyFifty || isCalledJokerDoubleDip){
                    btn2Lottie.reverseAnimationSpeed();
                    btn2Lottie.setClickable(false);
                    btn2TextView.setVisibility(View.INVISIBLE);
                    isCalledJokerDoubleDip = false;
                    joker2btn.setClickable(true);
                    joker1btn.setClickable(true);

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
                setButtonClickable(false);
                btn3TextView.setVisibility(View.INVISIBLE);
                btn3TextView.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // isOnScreenbnt3 = !isOnScreenbnt3;
                setButtonClickable(true);
                btn3TextView.setVisibility(View.VISIBLE);
                if(isCalledJokerFiftyFifty || isCalledJokerDoubleDip){
                    btn3Lottie.reverseAnimationSpeed();
                    btn3Lottie.setClickable(false);
                    btn3TextView.setVisibility(View.INVISIBLE);
                    isCalledJokerDoubleDip = false;
                    joker2btn.setClickable(true);
                    joker1btn.setClickable(true);


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
                setButtonClickable(false);
                btn4TextView.setVisibility(View.INVISIBLE);
                btn4TextView.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setButtonClickable(true);
                btn4TextView.setVisibility(View.VISIBLE);
                if(isCalledJokerFiftyFifty || isCalledJokerDoubleDip){
                    btn4Lottie.reverseAnimationSpeed();
                    btn4Lottie.setClickable(false);
                    btn4TextView.setVisibility(View.INVISIBLE);
                    isCalledJokerDoubleDip = false;
                    joker2btn.setClickable(true);
                    joker1btn.setClickable(true);

                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    private void setJokerClickListener(){
        joker1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buttonClick && isGameStart){
                    joker1SetClickableAndVisible(false);
                    joker2btn.setClickable(false);
                    myUserRef.child("wildcards").child("fiftyFiftyValue").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer value = mutableData.getValue(Integer.class);
                            value -= joker1DecValue;
                            if(value<0){
                                return null;
                            }else {
                                mutableData.setValue(value);
                                return Transaction.success(mutableData);
                            }
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            if(b){
                                isCalledJokerFiftyFifty = true;
                                joker1UsingCount++;
                                eliminateTwoOption();
                            }else {
                                MainActivity.showPopUpInfo(null,"Yeterli jokeriniz bulunmamaktadır.",null,GuessItActivity.this);
                                joker2btn.setClickable(true);
                            }
                        }
                    });
                }

            }
        });
        joker2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buttonClick && isGameStart){
                    joker2SetClickableAndVisible(false);
                    joker1btn.setClickable(false);
                    myUserRef.child("wildcards").child("doubleDipValue").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer value = mutableData.getValue(Integer.class);
                            value -=joker2DecValue;
                            if(value<0){
                                return null;
                            }else {
                                mutableData.setValue(value);
                                return Transaction.success(mutableData);
                            }
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            if(b){
                                isCalledJokerDoubleDip = true;
                                joker2UsingCount++;
                            }else {
                                MainActivity.showPopUpInfo(null,"Yeterli jokeriniz bulunmamaktadır.",null,GuessItActivity.this);
                                joker1btn.setClickable(true);

                            }
                        }
                    });
                }
            }
        });
        healthAnimView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthSetClickableAndVisible(false);
                if(!buttonClick){
                    myUserRef.child("wildcards").child("healthValue").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Integer value = mutableData.getValue(Integer.class);
                            value -= healthDecValue;
                            if(value<0){
                                return null;
                            }else {
                                mutableData.setValue(value);
                                return Transaction.success(mutableData);
                            }
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            if(b){
                                healthHandler.removeCallbacks(healthRunnable);
                                healthSetClickableAndVisible(false);
                                healthUsingCount++;
                                countQuestion++;
                                listUserPropertiesIndex++;
                                isGameStart = true;
                                gameManagement();
                            }else {
                                isGameStart = false;
                                MainActivity.showPopUpInfo(null,"Yeterli jokeriniz bulunmamaktadır.",null,GuessItActivity.this);
                            }
                        }
                    });
                }
            }
        });

    }
    private void setLoselayoutItemsClickListener(){
        myRightOfGame.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rightOfGame  = dataSnapshot.getValue(Integer.class);
                loseLAyoutRightOfGameTextView.setText("Kalan Tahmin: "+rightOfGame+"/10");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        playAgainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameStart){
                    isGameStart = true;
                    myUserRef.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        myUserRef.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final Long nowtimeStamp = dataSnapshot.getValue(Long.class);
                                myEndTime.child(getResources().getString(R.string.timestamp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Long endTime= dataSnapshot.getValue(Long.class);
                                        if(nowtimeStamp < endTime){
                                            myRightOfGame.runTransaction(new Transaction.Handler() {
                                                @NonNull
                                                @Override
                                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                    Integer i = mutableData.getValue(Integer.class);
                                                    if(i == 0){
                                                        return null;
                                                    }else {
                                                        i--;
                                                        mutableData.setValue(i);
                                                        return Transaction.success(mutableData);
                                                    }
                                                }

                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                    if(b){
                                                        if(rightOfGame == 9){
                                                            myUserRef.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            Long nowTimestamp = dataSnapshot.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).getValue(Long.class);
                                                                            myUserRef.child(getResources().getString(R.string.timestamp)).child("guessItMilisecond").setValue(nowTimestamp + 86400000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Intent i = new Intent(getApplicationContext(),GuessItActivity.class);
                                                                                    startActivity(i);
                                                                                    finish();
                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            });

                                                        }else {
                                                            Intent i = new Intent(getApplicationContext(),GuessItActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                }
                                            });
                                        }else {
                                            MainActivity.showPopUpInfo(null,"Hafta yenilenmemiştir!!",
                                                    "Sistem bu haftanın yapılandırmasını tamamladıktan sonra yeni hafta başlayacaktır. Başladığında bildirim gönderilecektir. \n \n ~Selfie Wars Tavsiyesi~ \n Yeni hafta için joker hazırlığı yapmak sıralamada avantaj sağlayacaktır."
                                                    , GuessItActivity.this);
                                            isGameStart = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameStart){
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void setJokerReset(){
        healthDecValue = (int) Math.pow(2,healthUsingCount);
        joker1DecValue = (int) Math.pow(2,joker1UsingCount);
        joker2DecValue = (int) Math.pow(2,joker2UsingCount);
        isCalledJokerFiftyFifty = false;
        isCalledJokerDoubleDip = false;
        joker1SetClickableAndVisible(true);
        joker2SetClickableAndVisible(true);
        healthSetClickableAndVisible(false);
        joker1ValueTextView.setText("" + joker1DecValue);
        joker2ValueTextView.setText("" + joker2DecValue);
        healthValueTextView.setText("" + healthDecValue);

    }
    private void healthSetClickableAndVisible(Boolean visibility){
        if(visibility){
            healthValueTextView.setVisibility(View.VISIBLE);
            healthValueTextView.setClickable(true);
            healthAnimView.setClickable(true);
            healthAnimView.setVisibility(View.VISIBLE);
            healthAnimView.playAnimation();
        }else {
            healthValueTextView.setVisibility(View.GONE);
            healthValueTextView.setClickable(false);
            healthAnimView.setVisibility(View.GONE);
            healthAnimView.setClickable(false);
        }
    }
    private List<Integer> getShuffleThreeOptions(List<Integer> alloptions){
        Collections.shuffle(alloptions);
        List<Integer> ıntegerList =  new ArrayList<>();
        ıntegerList.add(alloptions.get(0));
        ıntegerList.add(alloptions.get(1));
        ıntegerList.add(alloptions.get(2));
        return ıntegerList;
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
                loadingFeedBackTextView.setText(""+string);
            }

            @Override
            public void onSuccess(final List<GuessItUserData> guessItUserDataList,Boolean isBeta) {
                isBetaGame = isBeta;
                listUserProperties = guessItUserDataList;
                allquestionsCount = listUserProperties.size();
                Toast.makeText(GuessItActivity.this, "Toplam kullanıcı Sayısı: "+listUserProperties.size(), Toast.LENGTH_SHORT).show();
                loadFirstUserImage(0);
            }

            @Override
            public void onFailed() {
                loadinglayout.setVisibility(View.GONE);
                MainActivity.showPopUpInfo(null,"Tüm kullanıcıların yaşları tahmin edildi","Veritbanındaki tüm onaylı kullanıcıların yaşını tahmin ettiniz. Yeni kullanıcılar kısa sürede eklenecektir. Daha sonra tekrar gel.",GuessItActivity.this);
                setScoreDataInFirebaseAndLoseLayoutOpen();
            }
        });
    }
    public void getSecondUserListFromDatabase(){
        new Database().mReadDataAndGetUserListForGuessIt(mAuth, getResources().getString(R.string.ApprovedUser), getResources().getString(R.string.CorrectUsers), new OnGetUserlistDataListener() {
            @Override
            public void onStart() {
                isNextUserList = false;
            }

            @Override
            public void onProgress(String string) {

            }

            @Override
            public void onSuccess(final List<GuessItUserData> guessItUserDataList,Boolean isBeta) {
                isBetaGame = isBeta;
                listUserProperties.addAll(guessItUserDataList);
                allquestionsCount = listUserProperties.size();
                isNextUserList = true;
            }

            @Override
            public void onFailed() {
                isNextUserList = false;
            }
        });

    }
    private void joker1SetClickableAndVisible(Boolean clickable){
        if (clickable){
            joker1btn.setClickable(true);
            joker1btn.setVisibility(View.VISIBLE);
            joker1ValueTextView.setVisibility(View.VISIBLE);
        }else {
            joker1btn.setClickable(false);
            joker1btn.setVisibility(View.INVISIBLE);
            joker1ValueTextView.setVisibility(View.INVISIBLE);
        }
    }
    private void joker2SetClickableAndVisible(Boolean clickable){
        if (clickable){
            joker2btn.setClickable(true);
            joker2btn.setVisibility(View.VISIBLE);
            joker2ValueTextView.setVisibility(View.VISIBLE);
        }else {
            joker2btn.setClickable(false);
            joker2btn.setVisibility(View.INVISIBLE);
            joker2ValueTextView.setVisibility(View.INVISIBLE);
        }
    }
    private void eliminateOneOption(TextView textView){
        for (int i = 0; i<4;i++){
            if(textView.getText().equals(optionsList.get(i).toString())){
                switch (i){
                    case 0:{
                        btn1Lottie.reverseAnimationSpeed();
                        btn1Lottie.playAnimation();
                        btn1Lottie.setClickable(false);
                        break;
                    }
                        /*btn1Lottie.setVisibility(View.INVISIBLE);
                        btn1Lottie.setClickable(false);
                        btn1TextView.setVisibility(View.INVISIBLE);*/
                    case 1: {
                        btn2Lottie.reverseAnimationSpeed();
                        btn2Lottie.playAnimation();
                        btn2Lottie.setClickable(false);
                        break;
                    }
                    case 2:{
                        btn3Lottie.reverseAnimationSpeed();
                        btn3Lottie.playAnimation();
                        btn3Lottie.setClickable(false);
                        break;
                    }
                    case 3:{
                        btn4Lottie.reverseAnimationSpeed();
                        btn4Lottie.playAnimation();
                        btn4Lottie.setClickable(false);
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
        }
    }
    private void eliminateTwoOption(){
        for (int i = 0; i<4;i++){
            if(!listUserProperties.get(listUserPropertiesIndex).getAge().equals(optionsList.get(i)) && !secondfiftyFityOption.equals(optionsList.get(i))){
                switch (i){
                    case 0:{
                        btn1Lottie.reverseAnimationSpeed();
                        btn1Lottie.playAnimation();
                        break;
                    }
                        /*btn1Lottie.setVisibility(View.INVISIBLE);
                        btn1Lottie.setClickable(false);
                        btn1TextView.setVisibility(View.INVISIBLE);*/
                    case 1: {
                        btn2Lottie.reverseAnimationSpeed();
                        btn2Lottie.playAnimation();
                        break;
                    }
                        /*btn2Lottie.setVisibility(View.INVISIBLE);
                        btn2Lottie.setClickable(false);
                        btn2TextView.setVisibility(View.INVISIBLE);*/
                    case 2:{
                        btn3Lottie.reverseAnimationSpeed();
                        btn3Lottie.playAnimation();
                        break;
                    }

                        /*btn3Lottie.setVisibility(View.INVISIBLE);
                        btn3Lottie.setClickable(false);
                        btn3TextView.setVisibility(View.INVISIBLE);*/
                    case 3:{
                        btn4Lottie.reverseAnimationSpeed();
                        btn4Lottie.playAnimation();
                        break;
                    }
                        /*btn4Lottie.setVisibility(View.INVISIBLE);
                        btn4Lottie.setClickable(false);
                        btn4TextView.setVisibility(View.INVISIBLE);*/
                        default:
                            break;
                }
            }
        }
    }
    private void playAnimationButtons(){
        btn1Lottie.playAnimation();
        btn2Lottie.playAnimation();
        btn3Lottie.playAnimation();
        btn4Lottie.playAnimation();
    }
    private List<Integer> options(Integer answer){
        if(countQuestion >= 1 && countQuestion <= 3){
            List<Integer> allOptions = new ArrayList<>();
            List<Integer> options = new ArrayList<>();
            if(answer >= 15 ){
                for (int i = 1; i<6; i++){
                    //Toast.makeText(this, "i nin değeri: "+i, Toast.LENGTH_SHORT).show();
                    allOptions.add(answer - i); //20 19 18 17 16
                }
                for (int i = 1; i<6; i++){
                    allOptions.add(answer + i); //22 23 24 25 26 all options // 16 17 18 19 20 22 23 24 25 26
                }
                if(allOptions.size() == 10){
                    options = getShuffleThreeOptions(allOptions);
                    secondfiftyFityOption = options.get(0);
                    options.add(answer);
                    Collections.shuffle(options);
                    return options;
                }
            }
        }else if(countQuestion >=4 && countQuestion <=6){
            List<Integer> allOptions = new ArrayList<>();
            List<Integer> options = new ArrayList<>();
            if(answer >= 15 ){
                for (int i = 1; i<5; i++){
                    //Toast.makeText(this, "i nin değeri: "+i, Toast.LENGTH_SHORT).show();
                    allOptions.add(answer - i); //20 19 18 17
                }
                for (int i = 1; i<5; i++){
                    allOptions.add(answer + i); //22 23 24 25 all options // 17 18 19 20 22 23 24 25
                }
                if(allOptions.size() == 8){
                    options = getShuffleThreeOptions(allOptions);
                    secondfiftyFityOption = options.get(0);
                    options.add(answer);
                    Collections.shuffle(options);
                    return options;
                }
            }
            return null;

        }else if(countQuestion >=7 && countQuestion <=10){
            List<Integer> allOptions = new ArrayList<>();
            List<Integer> options = new ArrayList<>();
            if(answer >= 15 ){
                for (int i = 1; i<4; i++){
                    //Toast.makeText(this, "i nin değeri: "+i, Toast.LENGTH_SHORT).show();
                    allOptions.add(answer - i); //20 19 18
                }
                for (int i = 1; i<4; i++){
                    allOptions.add(answer + i); //22 23 24 all options // 18 19 20 22 23 24
                }
                if(allOptions.size() == 6){
                    options = getShuffleThreeOptions(allOptions);
                    secondfiftyFityOption = options.get(0);
                    options.add(answer);
                    Collections.shuffle(options);
                    return options;
                }
            }
        }else {
            List<Integer> allOptions = new ArrayList<>();
            List<Integer> options = new ArrayList<>();
            if(answer >= 15 ){
                for (int i = 1; i<3; i++){
                    ///Toast.makeText(this, "i nin değeri: "+i, Toast.LENGTH_SHORT).show();
                    allOptions.add(answer - i); //20 19
                }
                for (int i = 1; i<3; i++){
                    allOptions.add(answer + i); //22 23 all options // 19 20 22 23
                }
                if(allOptions.size() == 4){
                    options = getShuffleThreeOptions(allOptions);
                    secondfiftyFityOption = options.get(0);
                    options.add(answer);
                    Collections.shuffle(options);
                    return options;
                }
            }
        }
        return null;
    }
    private void loadFirstUserImage(final Integer index){
        Picasso.get().load(listUserProperties.get(index).getPhotoUrl()).noFade().networkPolicy(NetworkPolicy.NO_STORE).into(loadTempImageView, new Callback() {
            @Override
            public void onSuccess() {
                loadinglayout.setVisibility(View.GONE);
                guessitlayout.setVisibility(View.VISIBLE);
                isGameStart = true;
                gameManagement();
            }

            @Override
            public void onError(Exception e) {
                isNextUserList = false;
            }
        });
    }
    private void setVisibleLottie(){
        btn1Lottie.setVisibility(View.VISIBLE);
        btn2Lottie.setVisibility(View.VISIBLE);
        btn3Lottie.setVisibility(View.VISIBLE);
        btn4Lottie.setVisibility(View.VISIBLE);
    }
    private void loadNextImage(final Integer index){
        Picasso.get().load(listUserProperties.get(index).getPhotoUrl()).noFade().networkPolicy(NetworkPolicy.NO_STORE).into(loadTempImageView, new Callback() {
            @Override
            public void onSuccess() {
                //  Toast.makeText(GuessItActivity.this, "Index arka planda yüklendi :" +index   , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void startCountDownTimer(){
        try{
            if(countDowTimer !=null){
                countDowTimer.cancel();
                countDowTimer = new CountDownTimer(15000,1000) {
                    @Override
                    public void onTick(long l) {
                        timeValueTextView.setText(""+l/1000);
                        if(l/1000 < 6){
                            timeValueTextView.setTextColor(Color.RED);
                        }else {
                            timeValueTextView.setTextColor(Color.WHITE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        isGameStart = false;
                        setScoreDataInFirebaseAndLoseLayoutOpen();

                    }
                }.start();
            }else {
                countDowTimer = new CountDownTimer(15000,1000) {
                    @Override
                    public void onTick(long l) {
                        timeValueTextView.setText(""+l/1000);
                        if(l/1000 < 6){
                            timeValueTextView.setTextColor(Color.RED);
                        }else {
                            timeValueTextView.setTextColor(Color.WHITE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        isGameStart = false;
                        setScoreDataInFirebaseAndLoseLayoutOpen();

                    }
                }.start();
            }
        }catch (Exception e){

        }
    }
    private void setScore(){
        score += (countQuestion * 50);
        scoreValueTextView.setText(""+score);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
 /* @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }*/

    //.networkPolicy(NetworkPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
}
