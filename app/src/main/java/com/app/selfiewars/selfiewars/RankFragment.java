package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {
    List<RankingInfoActivity> rankingInfoActivityList;

    public RankFragment() {

        rankingInfoActivityList = new ArrayList<>();

      /*  rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        rankSetup(view);
        return view;
    }

    public void rankSetup(final View view) {
        FirebaseDatabase mydatabase;
        final DatabaseReference myRefScore;
        final DatabaseReference myRefUsers;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance();
        myRefUsers = mydatabase.getReference("Users");
        myRefScore = mydatabase.getReference("Scores");
        myRefScore.orderByValue().limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<ScoreInfo> scoreInfos = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ScoreInfo scoreInfo = new ScoreInfo();
                    scoreInfo.setScoreValue(ds.getValue(Integer.class));
                    scoreInfo.setUid(ds.getKey());
                    scoreInfos.add(scoreInfo);
                    if (scoreInfos.size() == 6){
                        myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i;
                                for (i = 0; i < scoreInfos.size();i++){
                                    UserProperties userProperties = dataSnapshot.child(scoreInfos.get(i).getUid()).child(getResources().getString(R.string.properties))
                                            .getValue(UserProperties.class);
                                    RankingInfoActivity  rankingInfoActivity = new RankingInfoActivity();
                                    rankingInfoActivity.setUserName(userProperties.getUserName());
                                    rankingInfoActivity.setUserPhotoImageView(userProperties.getPhotoUrl());
                                    rankingInfoActivityList.add(rankingInfoActivity);
                                    if (rankingInfoActivityList.size() == 6){
                                        Collections.reverse(rankingInfoActivityList);
                                        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRank);
                                        RankFragmentAdapter rankRcycView = new RankFragmentAdapter(getContext(), rankingInfoActivityList);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setAdapter(rankRcycView);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
