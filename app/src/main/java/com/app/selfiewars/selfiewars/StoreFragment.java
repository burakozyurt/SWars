package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {
    List<StoreDiamond> storeDiamondList;
    List<StoreJoker> storeJokerList;

    public StoreFragment() {
        // Required empty public constructor
        storeDiamondList = new ArrayList<>();
        storeDiamondList.add(new StoreDiamond("10","1,99",R.drawable.diamond10));
        storeDiamondList.add(new StoreDiamond("20","4,49",R.drawable.diamond20));
        storeDiamondList.add(new StoreDiamond("50","10,99",R.drawable.diamond50));
        storeDiamondList.add(new StoreDiamond("100","20,99",R.drawable.diamond100));
        storeDiamondList.add(new StoreDiamond("250","49,99",R.drawable.diamond250));
        storeDiamondList.add(new StoreDiamond("500","89,99",R.drawable.diamond500));
        storeJokerList = new ArrayList<>();
        storeJokerList.add(new StoreJoker("1","3",R.drawable.joker1,0));
        storeJokerList.add(new StoreJoker("5","12",R.drawable.joker1,0));
        storeJokerList.add(new StoreJoker("10","22",R.drawable.joker1,0));
        storeJokerList.add(new StoreJoker("20","40",R.drawable.joker1,0));
        storeJokerList.add(new StoreJoker("1","3",R.drawable.joker2,1));
        storeJokerList.add(new StoreJoker("5","12",R.drawable.joker2,1));
        storeJokerList.add(new StoreJoker("10","22",R.drawable.joker2,1));
        storeJokerList.add(new StoreJoker("20","40",R.drawable.joker2,1));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store, container, false);
        diamondSetup(view);
        jokerSetup(view);
        return view;
    }

    public void diamondSetup(View view){
        RecyclerView storeDiamondRcyView = view.findViewById(R.id.store_recyclerview_diamond);
        StoreDiamondRecyclerViewAdapter storeRcycView =  new StoreDiamondRecyclerViewAdapter(getContext(),storeDiamondList);
        storeDiamondRcyView.setLayoutManager(new GridLayoutManager(getContext(),3));
        storeDiamondRcyView.setAdapter(storeRcycView);
    }
    public void jokerSetup(View view){
        RecyclerView storeJokerRcyView = view.findViewById(R.id.store_recyclerview_joker_cards);
        StoreJokerRecyclerViewAdapter storeRcycView =  new StoreJokerRecyclerViewAdapter(getContext(),storeJokerList);
        storeJokerRcyView.setLayoutManager(new GridLayoutManager(getContext(),4));
        storeJokerRcyView.setAdapter(storeRcycView);
    }

}