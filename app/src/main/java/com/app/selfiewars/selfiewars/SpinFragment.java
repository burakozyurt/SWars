package com.app.selfiewars.selfiewars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpinFragment extends Fragment {
    LuckyWheelView luckyWheelView;
    List<LuckyItem> data;
    Button spin;
    final int idDiamond = 0, idJoker1 = 1, idJoker2 = 2, idReSpin = 3, idHealth = 4,idGreenGame = 5;
    public SpinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_spin, container, false);
       luckyWheelView = view.findViewById(R.id.luckyWheel);
       spin = view.findViewById(R.id.button3);
       spinClick();
       setupLuckyWheel();
       return view;
    }

    public void setupLuckyWheel(){
        setSpinItem();
        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                Toast.makeText(getContext(), ""+data.get(index-1).text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void spinClick(){
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luckyWheelView.startLuckyWheelWithTargetIndex(new Random().nextInt(12));
            }
        });
    }

    public void setSpinItem(){
        data = new ArrayList<>();
        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "x1";
        luckyItem1.valueNumber = 1;
        luckyItem1.icon = R.drawable.health;
        luckyItem1.color = 0xffFFF3E0;
        luckyItem1.id = idHealth;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "x3";
        luckyItem2.valueNumber = 3;
        luckyItem2.icon = R.drawable.joker2;
        luckyItem2.color = 0xffFFE0B2;
        luckyItem2.id = idJoker2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "x1";
        luckyItem3.valueNumber = 1;
        luckyItem3.icon = R.drawable.joker1;
        luckyItem3.color = 0xffFFF3E0;
        luckyItem3.id = idJoker1;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "x5";
        luckyItem4.valueNumber = 5;
        luckyItem4.id = idDiamond;
        luckyItem4.icon = R.drawable.diamond;
        luckyItem4.color = 0xffFFCC80;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "x1";
        luckyItem5.valueNumber = 1;
        luckyItem5.id = idGreenGame;
        luckyItem5.icon = R.drawable.greengem;
        luckyItem5.color = 0xffFFE0B2;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "x2";
        luckyItem6.id = idReSpin;
        luckyItem6.valueNumber = 2;
        luckyItem6.icon = R.drawable.spin;
        luckyItem6.color = 0xffFFCC80;
        data.add(luckyItem6);
        //////////////////

        //////////////////////
        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "x2";
        luckyItem7.valueNumber = 2;
        luckyItem7.id = idHealth;
        luckyItem7.icon = R.drawable.health;
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "x1";
        luckyItem8.id = idJoker2;
        luckyItem8.valueNumber = 1;
        luckyItem8.icon = R.drawable.joker2;
        luckyItem8.color = 0xffFFE0B2;
        data.add(luckyItem8);

        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.text = "x3";
        luckyItem9.id = idJoker1;
        luckyItem9.valueNumber = 3;
        luckyItem9.icon = R.drawable.joker1;
        luckyItem9.color = 0xffFFF3E0;
        data.add(luckyItem9);

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.text = "x10";
        luckyItem10.id = idDiamond;
        luckyItem10.valueNumber = 10;
        luckyItem10.icon = R.drawable.diamond;
        luckyItem10.color = 0xffFFCC80;
        data.add(luckyItem10);

        LuckyItem luckyItem11 = new LuckyItem();
        luckyItem11.text = "x2";
        luckyItem11.id = idGreenGame;
        luckyItem11.valueNumber = 2;
        luckyItem11.icon = R.drawable.greengem;
        luckyItem11.color = 0xffFFE0B2;
        data.add(luckyItem11);

        LuckyItem luckyItem12 = new LuckyItem();
        luckyItem12.text = "x1";
        luckyItem12.id = idReSpin;
        luckyItem12.valueNumber = 1;
        luckyItem12.icon = R.drawable.spin;
        luckyItem12.color = 0xffFFCC80;
        data.add(luckyItem12);

        luckyWheelView.setData(data);
        luckyWheelView.setRound(6);
    }

}
