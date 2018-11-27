package com.app.selfiewars.selfiewars;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;


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
    private TextView usernameTextView;
    private TextView rightOfGameTextView;
    private CircleImageView profileImageView;
    private String photoUrl;
    private FirebaseDatabase mdatabase;
    private DatabaseReference myUserRef;
    private FirebaseAuth mAuth;
    private Picasso mPicasso;
    private Button guessitButton;
    private Wildcards wildcards;
    private Integer diamondToken;
    private Integer rightOfGame;
    private ProgressDialog mProgressDialog;


    public HomeFragment() {
        // Required empty public constructor
        mPicasso = Picasso.get();
        mPicasso.setIndicatorsEnabled(true);
        rightOfGame = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        myUserRef = mdatabase.getReference("Users/" + mAuth.getUid());
        define(rootview);
        //getUserData();
        onClick_GuessIt();
        mCheckInforInServer("Users/" + mAuth.getUid());
        mGetInforInServer("RightOfGame/" + mAuth.getUid());
        return rootview;
    }

    private void mGetInforInServer(String child) {
        new Database().mReadDataRealTime(child, new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot data) {
                rightOfGame = data.getValue(Integer.class);
                rightOfGameTextView.setText(""+rightOfGame);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
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
        usernameTextView = rootview.findViewById(R.id.home_profile_username_textView);
        profileImageView = rootview.findViewById(R.id.home_profile_circleImageView);
        guessitButton = rootview.findViewById(R.id.home_profile_guessit_button);
        rightOfGameTextView = rootview.findViewById(R.id.home_profile_rightOfGame_textView);
    }
    private void getUserData(){
        myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                if(isReady){
                    UserProperties properties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                    displayNameTextView.setText(properties.getDisplayName());
                    usernameTextView.setText(properties.getUserName());
                    ageTextView.setText(String.valueOf(properties.getAge()));
                    photoUrl = properties.getPhotoUrl();
                    mPicasso.load(photoUrl).resize(400,400).into(profileImageView);
                    if(dataSnapshot.hasChild(getResources().getString(R.string.token))){
                        diamondToken =dataSnapshot.child(getResources().getString(R.string.token)).child((getResources().getString(R.string.diamondValue))).getValue(Integer.class);
                        diamondTextView.setText(""+diamondToken);}
                    if(dataSnapshot.hasChild(getResources().getString(R.string.wildcards))) {
                        wildcards = dataSnapshot.child(getResources().getString(R.string.wildcards)).getValue(Wildcards.class);
                        doubleDipTextView.setText(""+wildcards.getDoubleDipValue());
                        healthTextView.setText(""+wildcards.getHealthValue());
                        fiftyFiftyTextView.setText(""+wildcards.getFiftyFiftyValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void onClick_GuessIt(){
        guessitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),GuessItActivity.class);
                startActivity(i);

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
                Boolean isReady = dataSnapshot.child(getResources().getString(R.string.account_State)).child(getResources().getString(R.string.isReady)).getValue(Boolean.class);
                if(isReady){
                    UserProperties properties = dataSnapshot.child(getResources().getString(R.string.properties)).getValue(UserProperties.class);
                    displayNameTextView.setText(properties.getDisplayName());
                    usernameTextView.setText(properties.getUserName());
                    ageTextView.setText(String.valueOf(properties.getAge()));
                    photoUrl = properties.getPhotoUrl();
                    mPicasso.load(photoUrl).resize(400,400).into(profileImageView);
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
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

}
