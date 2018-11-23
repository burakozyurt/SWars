package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {
    List<RankingInfoActivity> rankingInfoActivityList;

    public RankFragment() {

        rankingInfoActivityList = new ArrayList<>();

        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));
        rankingInfoActivityList.add(new RankingInfoActivity(R.drawable.star, R.drawable.markzuckerberg, "mrkzckrbg"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        rankSetup(view);
        return view;
    }

    public void rankSetup(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRank);
        RankFragmentAdapter rankRcycView = new RankFragmentAdapter(getContext(), rankingInfoActivityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rankRcycView);
    }

}
