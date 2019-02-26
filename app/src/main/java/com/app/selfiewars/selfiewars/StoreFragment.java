package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {
    List<StoreDiamond> storeDiamondList;
    List<StoreJoker> storeJokerList;
    FirebaseRemoteConfig firebaseRemoteConfig;
    private long cacheExpiration = 0;
    public StoreFragment() {
        // Required empty public constructor
        storeDiamondList = new ArrayList<>();
        /*storeDiamondList.add(new StoreDiamond("10","2,49",R.drawable.diamond10));
        storeDiamondList.add(new StoreDiamond("20","4,49",R.drawable.diamond20));
        storeDiamondList.add(new StoreDiamond("50","10,99",R.drawable.diamond50));
        storeDiamondList.add(new StoreDiamond("100","20,99",R.drawable.diamond100));
        storeDiamondList.add(new StoreDiamond("250","49,99",R.drawable.diamond250));
        storeDiamondList.add(new StoreDiamond("500","89,99",R.drawable.diamond500));
      */

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
        setDiamondValue();
        if(MainActivity.remoteDiamondLoad)
        diamondSetup(view);
        else {
            getRemoteConfig();
        }
        jokerSetup(view);
        return view;
    }
    private void getRemoteConfig(){
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetch(0).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getContext(),"Fetch is Succesfully Store",Toast.LENGTH_SHORT).show();
                    firebaseRemoteConfig.activateFetched();
                }else{
                    //Toast.makeText(getContext(),"Fetch is Failed",Toast.LENGTH_SHORT).show();
                }
                setDiamondValueAndShow();
            }
        });
    }

    private void setDiamondValueAndShow() {
        String diamondPrice10 = firebaseRemoteConfig.getString("diamondPrice10");
        String  diamondPrice20 = firebaseRemoteConfig.getString("diamondPrice20");
        String  diamondPrice50 = firebaseRemoteConfig.getString("diamondPrice50");
        String  diamondPrice100 = firebaseRemoteConfig.getString("diamondPrice100");
        String  diamondPrice250 = firebaseRemoteConfig.getString("diamondPrice250");
        String  diamondPrice500 = firebaseRemoteConfig.getString("diamondPrice500");
        storeDiamondList.clear();
        storeDiamondList.add(new StoreDiamond("10",diamondPrice10,R.drawable.diamond10));
        storeDiamondList.add(new StoreDiamond("20",diamondPrice20,R.drawable.diamond20));
        storeDiamondList.add(new StoreDiamond("50",diamondPrice50,R.drawable.diamond50));
        storeDiamondList.add(new StoreDiamond("100",diamondPrice100,R.drawable.diamond100));
        storeDiamondList.add(new StoreDiamond("250",diamondPrice250,R.drawable.diamond250));
        storeDiamondList.add(new StoreDiamond("500",diamondPrice500,R.drawable.diamond500));
        diamondSetup(getView());
    }

    private void setDiamondValue() {
        String diamondPrice10 = MainActivity.diamondPrice10;
        String diamondPrice20 = MainActivity.diamondPrice20;
        String diamondPrice50 = MainActivity.diamondPrice50;
        String diamondPrice100 = MainActivity.diamondPrice100;
        String diamondPrice250 = MainActivity.diamondPrice250;
        String diamondPrice500 = MainActivity.diamondPrice500;

        storeDiamondList.add(new StoreDiamond("10",diamondPrice10,R.drawable.diamond10));
        storeDiamondList.add(new StoreDiamond("20",diamondPrice20,R.drawable.diamond20));
        storeDiamondList.add(new StoreDiamond("50",diamondPrice50,R.drawable.diamond50));
        storeDiamondList.add(new StoreDiamond("100",diamondPrice100,R.drawable.diamond100));
        storeDiamondList.add(new StoreDiamond("250",diamondPrice250,R.drawable.diamond250));
        storeDiamondList.add(new StoreDiamond("500",diamondPrice500,R.drawable.diamond500));
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
