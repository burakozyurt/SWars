package com.app.selfiewars.selfiewars;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView displayNameTextView;
    private TextView ageTextView;
    private TextView rankTextView;
    private TextView scoreTextView;
    private TextView healthTextView;
    private TextView fiftyFiftyTextView;
    private TextView doubleDipTextView;
    private TextView diamondTextView;
    private TextView diamondAdsTextView;
    private TextView izlekazanAdsTextView;
    private static TextView righOfGameEndTimeTextView;
    private TextView usernameTextView;
    private TextView rightOfGameTextView;
    private CircleImageView profileImageView;
    private ImageView rightOfGameDiamondImageView;
    private String photoUrl;
    private FirebaseDatabase mdatabase;
    private DatabaseReference myCorrectUsersRef;
    private FirebaseAuth mAuth;
    private Picasso mPicasso;
    private Button guessitButton;
    private Wildcards wildcards;
    private Integer diamondToken;
    private Integer rightOfGame;
    private ProgressDialog mProgressDialog;
    private ImageView settingsIcon;
    private static CountDownTimer countDownTimer;
    private static Long guessItMilisecond;
    private Integer guessItAdsCount;
    private LottieAnimationView lottieAds;
    private AnnouncementViewPageAdapter announcementViewPageAdapter;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Handler updateHandler;
    private Timer timer;
    private LinearLayout announcementLinearLayout;
    private boolean isClickGuessitButton = false;
    private RewardedVideoAd mRewardedVideoAd;
    public HomeFragment() {
        // Required empty public constructor
        rightOfGame = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        /*myUserRef = mdatabase.getReference("Users/" + mAuth.getUid());
        myRigtOfGameRef = mdatabase.getReference("RightOfGame/" + mAuth.getUid());
        myAnnouncementRef = mdatabase.getReference("Announcement");*/
        myCorrectUsersRef = mdatabase.getReference("CorrectUsers/"+mAuth.getUid());
        //myScoreRef = mdatabase.getReference(getResources().getString(R.string.Scores));
        /*myUserRef.keepSynced(true);
        myRigtOfGameRef.keepSynced(true);
        myAnnouncementRef.keepSynced(true);*/
        //myScoreRef.keepSynced(true);
        try {
            updateHandler=new Handler();
            define(rootview);
            setViewPager();
            //getUserData();
            onClick_GuessIt();
            mCheckInforInServer("Users/" + mAuth.getUid());
            mGetInforInServer("RightOfGame/" + mAuth.getUid());
            //getTimeStampControl();
            settingsIconClicked();
            setRewardAds();
        }catch (Exception e){
            Toast.makeText(getActivity(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        return rootview;
    }
    private void define(View rootview){
        displayNameTextView = rootview.findViewById(R.id.home_profile_display_name_textView);
        ageTextView = rootview.findViewById(R.id.home_profile_age_textView);
        rankTextView = rootview.findViewById(R.id.home_profile_rank_textView);
        scoreTextView = rootview.findViewById(R.id.home_profile_score_textView);
        healthTextView = rootview.findViewById(R.id.home_profile_health_textView);
        fiftyFiftyTextView = rootview.findViewById(R.id.home_profile_fiftyfifty_textView);
        doubleDipTextView = rootview.findViewById(R.id.home_profile_double_dip_textView);
        diamondTextView = rootview.findViewById(R.id.home_profile_diamond_textView);
        diamondAdsTextView = rootview.findViewById(R.id.rightOfGameDiamondTextView);
        usernameTextView = rootview.findViewById(R.id.home_profile_username_textView);
        profileImageView = rootview.findViewById(R.id.home_profile_circleImageView);
        guessitButton = rootview.findViewById(R.id.home_profile_guessit_button);
        rightOfGameTextView = rootview.findViewById(R.id.home_profile_rightOfGame_textView);
        lottieAds = rootview.findViewById(R.id.lottieAds);
        izlekazanAdsTextView = rootview.findViewById(R.id.home_fragment_lottieAdsTextView);
        rightOfGameDiamondImageView = rootview.findViewById(R.id.rightOfGameDiamond);
        settingsIcon = rootview.findViewById(R.id.settingsIcon);
        righOfGameEndTimeTextView = rootview.findViewById(R.id.rightofgame_endtime_textView);
        viewPager = rootview.findViewById(R.id.announcementViewPager);
        circleIndicator = rootview.findViewById(R.id.announcement_indicator);
        announcementLinearLayout = rootview.findViewById(R.id.linearLayoutAnnouncement);
        MobileAds.initialize(getActivity(), "ca-app-pub-7004761147200711~5104636223");
    }
    private void setViewPager(){
        announcementViewPageAdapter =  new AnnouncementViewPageAdapter(getContext());
        announcementViewPageAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        viewPager.setAdapter(announcementViewPageAdapter);
        circleIndicator.setViewPager(viewPager);
        timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                updateHandler.post(new Runnable() {
                    public void run() {
                        if(viewPager.getCurrentItem() == 0){
                            viewPager.setCurrentItem(1);
                        }else if(viewPager.getCurrentItem() == 1){
                            viewPager.setCurrentItem(2);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 5000, 5000);
    }
    private void onClick_GuessIt(){
        guessitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isClickGuessitButton){
                    isClickGuessitButton = true;
                    MainActivity.myRefUser.child("nowtimestamp").child(getResources().getString(R.string.timestamp)).runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            mutableData.setValue(ServerValue.TIMESTAMP);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            final Long nowtimeStamp = dataSnapshot.getValue(Long.class);
                            if(MainActivity.endTimeStamp > nowtimeStamp ){
                                MainActivity.myRigtOfGameRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        Integer i = mutableData.getValue(Integer.class);
                                        if(i == 0){
                                            return null;
                                        }
                                        else {
                                            i--;
                                            if(i == 0){
                                                getTimeStampControl();
                                            }
                                            mutableData.setValue(i);
                                            return Transaction.success(mutableData);
                                        }
                                    }
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                        if(b){
                                            if(rightOfGame == 9){
                                                MainActivity.myRefUser.child(getResources().getString(R.string.timestamp)).child("guessItMilisecond").setValue(nowtimeStamp + 86400000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) { isClickGuessitButton = false;
                                                        MainActivity.myRefUser.child(getResources().getString(R.string.timestamp)).child("guessItAdsCount").setValue(3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Intent i = new Intent(getActivity(),GuessItActivity.class);
                                                                startActivity(i);
                                                            }
                                                        });

                                                    }
                                                });
                                            }else {
                                                isClickGuessitButton = false;
                                                Intent i = new Intent(getActivity(),GuessItActivity.class);
                                                startActivity(i);
                                            }
                                        }else {
                                            MainActivity.showPopUpInfo(null,"Günlük tahmin hakkınız bitmiştir!!",null,getContext());
                                            Log.d("rightofgame:" ,"rightofgame:--"+rightOfGame+"---    guessadsCount : "+guessItAdsCount);
                                            if(rightOfGame == 0 && MainActivity.guessItAdsCount > 0){
                                                rightOfGameDiamondImageView.setVisibility(View.GONE);
                                                lottieAds.setVisibility(View.VISIBLE);
                                                izlekazanAdsTextView.setVisibility(View.GONE);

                                            }
                                            else if(rightOfGame == 0 && MainActivity.guessItAdsCount == 0 && diamondToken >=5){
                                                lottieAds.setVisibility(View.GONE);
                                                izlekazanAdsTextView.setVisibility(View.GONE);
                                                rightOfGameDiamondImageView.setVisibility(View.VISIBLE);
                                                diamondAdsTextView.setVisibility(View.VISIBLE);
                                            }
                                            righOfGameEndTimeTextView.setVisibility(View.VISIBLE);
                                            isClickGuessitButton = false;
                                        }

                                    }

                                });
                            }else {
                                MainActivity.showPopUpInfo(null,"Hafta yenilenmemiştir!!",
                                        "Sistem bu haftanın yapılandırmasını tamamladıktan sonra yeni hafta başlayacaktır. Başladığında bildirim gönderilecektir. \n \n ~Selfie Wars Tavsiyesi~ \n Yeni hafta için joker hazırlığı yapmak sıralamada avantaj sağlayacaktır."
                                        ,getActivity());
                                isClickGuessitButton = false;
                            }
                        }
                    });
                }


            }
        });
    }
    private void setRewardAds(){
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.loadAd("ca-app-pub-7004761147200711/4140582849",
                new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
               // Toast.makeText(getContext(), "Reklam Yüklendi", Toast.LENGTH_SHORT).show();
                onClickListener_LottieAds();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                getTimeStampControl();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Log.d("RewardItemFB","Rewarded");
                MainActivity.myRigtOfGameRef.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer rightOfGameValue = mutableData.getValue(Integer.class);
                        rightOfGameValue++;
                        mutableData.setValue(rightOfGameValue);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (b){
                            getTimeStampControl();
                            setRewardAds();
                            MainActivity.showPopUpInfo(null,"Tebrikler tekrar tahmin edebilirsiniz.",null,getContext());
                        }
                    }
                });
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }

    private void onClickListener_LottieAds(){
        izlekazanAdsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottieAds.setClickable(false);
                izlekazanAdsTextView.setClickable(false);
                // Toast.makeText(getContext(), "Reklam Yükleniyor", Toast.LENGTH_SHORT).show();
                MainActivity.myRefUser.child("timestamp").child("guessItAdsCount").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer value= mutableData.getValue(Integer.class);
                        if(value == 0){
                            return null;
                        }
                        else {
                            value--;
                            mutableData.setValue(value);
                            return Transaction.success(mutableData);
                        }
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (b){
                            lottieAds.setClickable(true);
                            izlekazanAdsTextView.setClickable(true);
                            if(mRewardedVideoAd.isLoaded())
                                mRewardedVideoAd.show();
                            else
                                mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                                        new AdRequest.Builder().build());
                        }
                        else {
                            getTimeStampControl();
                        }
                    }
                });
            }
        });
        lottieAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottieAds.setClickable(false);
                izlekazanAdsTextView.setClickable(false);
                // Toast.makeText(getContext(), "Reklam Yükleniyor", Toast.LENGTH_SHORT).show();
                MainActivity.myRefUser.child("timestamp").child("guessItAdsCount").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer value= mutableData.getValue(Integer.class);
                        if(value == 0){
                            return null;
                        }
                        else {
                            value--;
                            mutableData.setValue(value);
                            return Transaction.success(mutableData);
                        }
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (b){
                            lottieAds.setClickable(true);
                            izlekazanAdsTextView.setClickable(true);
                            if(mRewardedVideoAd.isLoaded())
                                mRewardedVideoAd.show();
                            else
                                mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                                        new AdRequest.Builder().build());
                        }
                        else {
                            getTimeStampControl();
                        }
                    }
                });
            }
        });
    }
    private void onClickListener_DiamondsAds(){
        rightOfGameDiamondImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightOfGameDiamondImageView.setClickable(false);
                MainActivity.myRefUser.child("token").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer diamond = mutableData.child("diamondValue").getValue(Integer.class);
                        if(diamond == 0)
                            return null;
                        else if (diamond >=5){
                            diamond-=5;
                            mutableData.child("diamondValue").setValue(diamond);
                            return Transaction.success(mutableData);
                        }else {
                            return null;
                        }
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (b){
                                MainActivity.myRigtOfGameRef.setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        getTimeStampControl();
                                        rightOfGameDiamondImageView.setClickable(true);

                                    }
                                });
                        }else {
                            MainActivity.showPopUpInfo(null,"Elmas Yetersiz!!",null,getContext());
                            righOfGameEndTimeTextView.setVisibility(View.GONE);
                            diamondAdsTextView.setVisibility(View.GONE);
                            rightOfGameDiamondImageView.setVisibility(View.GONE);
                            rightOfGameDiamondImageView.setClickable(true);
                        }
                    }
                });
            }
        });

    }
    private void mCheckInforInServer(String child) {
        new Database().mReadDataRealTime(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(getContext());
                    mProgressDialog.setMessage(getResources().getString(R.string.loadingMessage));
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                }

                mProgressDialog.show();
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                Log.d("UserListener  ","Çalışıyor ");
                try {
                    Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                    if(isReady){
                        UserProperties properties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                        displayNameTextView.setText(properties.getDisplayName());
                        usernameTextView.setText(properties.getUserName());
                        ageTextView.setText(String.valueOf(properties.getAge()));
                        photoUrl = properties.getPhotoUrl();
                        mPicasso.with(getContext()).load(photoUrl).networkPolicy(NetworkPolicy.OFFLINE).into(profileImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("BPiccasso","Yüklendi");
                            }
                            @Override
                            public void onError() {
                                Log.d("BPiccasso","Hata Verdi");
                                mPicasso.with(getContext()).load(photoUrl).into(profileImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {}
                                    @Override
                                    public void onError() {
                                        mPicasso.with(getContext()).load(photoUrl).into(profileImageView);
                                    }
                                });
                            }
                        });
                        if(dataSnapshot.hasChild(getResources().getString(R.string.token))){
                            diamondToken =dataSnapshot.child(getResources().getString(R.string.token)).child((getResources().getString(R.string.diamondValue))).getValue(Integer.class);
                            diamondTextView.setText(""+diamondToken);}
                        if(dataSnapshot.hasChild(getResources().getString(R.string.wildcards))) {
                            wildcards = dataSnapshot.child(getResources().getString(R.string.wildcards)).getValue(Wildcards.class);
                            doubleDipTextView.setText(""+wildcards.getDoubleDipValue());
                            healthTextView.setText(""+wildcards.getHealthValue());
                            fiftyFiftyTextView.setText(""+wildcards.getFiftyFiftyValue());
                        }
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        getScore();
                        getUserData();
                        getTimeStampControl();
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }
    private void getUserData(){
        myCorrectUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>100){
                    myCorrectUsersRef.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MainActivity.myRefUser.child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    diamondToken = dataSnapshot.child((getResources().getString(R.string.diamondValue))).getValue(Integer.class);
                    diamondTextView.setText(""+diamondToken);
                    MainActivity.diamondValue = diamondToken;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MainActivity.myRefUser.child("wildcards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Log.d("wildcards","çalıştı");
                    wildcards = dataSnapshot.getValue(Wildcards.class);
                    doubleDipTextView.setText(""+wildcards.getDoubleDipValue());
                    healthTextView.setText(""+wildcards.getHealthValue());
                    fiftyFiftyTextView.setText(""+wildcards.getFiftyFiftyValue());
                    MainActivity.wildcards = wildcards;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MainActivity.myRefUser.child("properties").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProperties properties = dataSnapshot.getValue(UserProperties.class);
                MainActivity.userProperties = properties;
                MainActivity.userDataSnapShot = dataSnapshot;
                displayNameTextView.setText(properties.getDisplayName());
                usernameTextView.setText(properties.getUserName());
                ageTextView.setText(String.valueOf(properties.getAge()));
                photoUrl = properties.getPhotoUrl();
                mPicasso.with(getContext()).load(photoUrl).networkPolicy(NetworkPolicy.OFFLINE).into(profileImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Log.d("BPiccasso","Yüklendi");
                    }
                    @Override
                    public void onError() {
                        //Log.d("BPiccasso","Hata Verdi");
                        mPicasso.with(getContext()).load(photoUrl).into(profileImageView);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getScore(){
        MainActivity.myScoreRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Integer userscore = dataSnapshot.getValue(Integer.class);
                    scoreTextView.setText(""+userscore );
                    MainActivity.UserScore = userscore;
                    //Log.d("OptionsFirebase","UserScore"+MainActivity.UserScore);

                }
                else {
                    //Log.d("DbScore","User");
                    MainActivity.myScoreRef.child(mAuth.getUid()).setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void settingsIconClicked(){
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
    private void mGetInforInServer(String child) {

        MainActivity.myRigtOfGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rightOfGame = dataSnapshot.getValue(Integer.class);
                //Log.d("RightOfGame",""+rightOfGame);
                rightOfGameTextView.setText(""+rightOfGame);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MainActivity.myScoreRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long i = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    String uid;
                    uid = ds.getKey();
                    if(uid.equals(mAuth.getUid())){
                        i = Math.abs(dataSnapshot.getChildrenCount() - i);
                        rankTextView.setText(""+i);
                        break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static String getTimeDate(long timeStamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timeStamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }

    private void startOrRefreshCountTime(Long myDate){
        try {

            if(countDownTimer !=null){
                countDownTimer.cancel();
                countDownTimer = new CountDownTimer((Math.abs(MainActivity.guessItMilisecond-myDate)),1000) {
                    @Override
                    public void onTick(long l) {
                        righOfGameEndTimeTextView.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) +getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                +(TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))+getResources().getString(R.string.second) );
                    }

                    @Override
                    public void onFinish() {
                        getTimeStampControl();

                    }
                }.start();
            }
            else {
                countDownTimer = new CountDownTimer((Math.abs(MainActivity.guessItMilisecond-myDate)),1000) {
                    @Override
                    public void onTick(long l) {
                        righOfGameEndTimeTextView.setText(getResources().getString(R.string.Endtime) + TimeUnit.MILLISECONDS.toHours(l) +getResources().getString(R.string.hours)
                                + (TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l))) + getResources().getString(R.string.minute)
                                +(TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))+getResources().getString(R.string.second) );
                    }
                    @Override
                    public void onFinish() {
                        getTimeStampControl();

                    }
                }.start();
            }
        }catch (Exception e){
        }
    }

    private void getTimeStampControl(){
        MainActivity.myRefUser.child(getResources().getString(R.string.nowtimestamp)).child(getResources().getString(R.string.timestamp)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                mutableData.setValue(ServerValue.TIMESTAMP);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Long nowtimeStamp = dataSnapshot.getValue(Long.class);
                final Integer controlrightOfGame = MainActivity.myRightOfGameDataSnapShot.getValue(Integer.class);
                if(nowtimeStamp != null){
                    //Log.d("TagRightOFGameControl","MainActivity Accessed:  ");
                    if(nowtimeStamp< MainActivity.guessItMilisecond){
                        if(MainActivity.myRigtOfGame == 0){
                            righOfGameEndTimeTextView.setVisibility(View.VISIBLE);
                            startOrRefreshCountTime(nowtimeStamp);
                            if(MainActivity.guessItAdsCount > 0){
                                if(!mRewardedVideoAd.isLoaded()){
                                    setRewardAds();
                                }
                                lottieAds.setVisibility(View.VISIBLE);
                                izlekazanAdsTextView.setVisibility(View.VISIBLE);
                                rightOfGameDiamondImageView.setVisibility(View.GONE);
                                diamondAdsTextView.setVisibility(View.GONE);
                            }
                            else {
                                lottieAds.setVisibility(View.GONE);
                                izlekazanAdsTextView.setVisibility(View.GONE);
                                if(diamondToken>=5){
                                    rightOfGameDiamondImageView.setVisibility(View.VISIBLE);
                                    diamondAdsTextView.setVisibility(View.VISIBLE);
                                    onClickListener_DiamondsAds();
                                }
                            }
                        }else {
                            righOfGameEndTimeTextView.setVisibility(View.INVISIBLE);
                            rightOfGameDiamondImageView.setVisibility(View.GONE);
                            lottieAds.setVisibility(View.GONE);
                            izlekazanAdsTextView.setVisibility(View.GONE);
                            diamondAdsTextView.setVisibility(View.GONE);

                        }
                    }if(nowtimeStamp > MainActivity.guessItMilisecond){
                        righOfGameEndTimeTextView.setVisibility(View.INVISIBLE);
                        rightOfGameDiamondImageView.setVisibility(View.GONE);
                        diamondAdsTextView.setVisibility(View.GONE);
                        lottieAds.setVisibility(View.GONE);
                        izlekazanAdsTextView.setVisibility(View.GONE);
                        if(MainActivity.myRigtOfGame < 10){
                            MainActivity.myRigtOfGameRef.setValue(10);
                        }
                    }
                }
            }
        });
        }
            /*
        * Şu anki zamanla kıyasla
eğer future ise
rightofgame bak
rightofgame 0 ise ekrana göster
değilse kaybet
past ise
righofgame bak 10 dan küçük ise
righofgame 10 yap
10 ise
ekranda gösterme*/

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null){
            timer.cancel();
        }
        if (countDownTimer !=null){
            //Toast.makeText(getContext(),"Sayaç Bekletildi" , Toast.LENGTH_SHORT).show();
            countDownTimer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(countDownTimer !=null){
            getTimeStampControl();
            //  Toast.makeText(getContext(),"Sayaç Devam Ediyor" , Toast.LENGTH_SHORT).show();
        }
    }


}
