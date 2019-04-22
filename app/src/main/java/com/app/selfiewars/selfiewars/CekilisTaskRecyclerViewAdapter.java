package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.List;

public class CekilisTaskRecyclerViewAdapter  extends RecyclerView.Adapter<CekilisTaskRecyclerViewAdapter.MyVievHolder> {
    private Context mcontext;
    private List<CekilisTask> tasklist;
    private FirebaseDatabase database;
    private DatabaseReference cekilisRef;

    public CekilisTaskRecyclerViewAdapter(Context mcontext, List<CekilisTask> tasklist) {
        this.mcontext = mcontext;
        this.tasklist = tasklist;
        database = FirebaseDatabase.getInstance();
        cekilisRef = database.getReference("Cekilis");
    }



    @NonNull
    @Override
    public CekilisTaskRecyclerViewAdapter.MyVievHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mcontext);
        view = mInflater.inflate(R.layout.cekilis_katil_task_cardview,viewGroup,false);
        return new CekilisTaskRecyclerViewAdapter.MyVievHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CekilisTaskRecyclerViewAdapter.MyVievHolder myVievHolder, final int i) {
        myVievHolder.descTextView.setText(""+tasklist.get(i).getDesc());
        myVievHolder.bracketDescTextView.setText(""+tasklist.get(i).getBracketsdesc());
        myVievHolder.taskButton.setText(""+tasklist.get(i).getButtonText());
        myVievHolder.taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0){
                    CekilisActivity.showAds();
                }else if (i == 1){
                    //Toast.makeText(mcontext, "Davet Seçildi", Toast.LENGTH_SHORT).show();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "http://onelink.to/j3xpf3 "+"\n SelfieWars - Oyna Kazan - Ödüllü Tahmin Yarışmasını Google Play Store üzerinden indirip (" +MainActivity.userProperties.getUserName()+") kullanıcı adımla bana destek olabilirsin." );
                    sendIntent.setType("text/plain");
                    mcontext.startActivity(Intent.createChooser(sendIntent, "Paylaş"));
                }else if( i== 2){
                    //Toast.makeText(mcontext, "Elmas Seçildi", Toast.LENGTH_SHORT).show();
                    entryDiamond();
                }else {

                }
            }
        });

    }


    public void entryDiamond(){
        if (MainActivity.diamondValue >= 30){
            MainActivity.myRefUser.child("token").child("diamondValue").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer dmndValue = mutableData.getValue(Integer.class);
                    if (dmndValue !=null){
                        dmndValue -= 30;
                        mutableData.setValue(dmndValue);
                        return Transaction.success(mutableData);
                    }else {
                        return null;
                    }
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if (b){
                        cekilisRef.child(FirebaseAuth.getInstance().getUid()).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                Integer value = mutableData.getValue(Integer.class);
                                if(value == null){
                                    value = 1;
                                }else
                                    value += 1;
                                mutableData.setValue(value);
                                return Transaction.success(mutableData);
                            }
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                if (b){
                                    MainActivity.showPopUpInfo(null,"Tebrikler Başarıyla Katıldınız",null,mcontext);
                                    CekilisActivity.updateDiamondValue();
                                }else {
                                    MainActivity.showPopUpInfo(null,"Bir Sorun Oluştu.","Çekilis sayfası hata listemize eklendi. Anlayışınız için teşekkür ederiz.",mcontext);
                                }
                            }
                        });
                    }else {
                    }
                }
            });
        }else
            MainActivity.showPopUpInfo(null,"Yetersiz Elmas!",null,mcontext);
    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }

    public static class MyVievHolder extends RecyclerView.ViewHolder {
        TextView descTextView;
        TextView bracketDescTextView;
        Button taskButton;
        public MyVievHolder(@NonNull View itemView) {
            super(itemView);
            descTextView = itemView.findViewById(R.id.cekilis_katil_task_desc_TextView);
            bracketDescTextView = itemView.findViewById(R.id.cekilis_katil_task_brackets_TextView);
            taskButton = itemView.findViewById(R.id.cekilis_katil_task_button);

        }
    }
}
