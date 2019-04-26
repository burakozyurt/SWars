package com.app.selfiewars.selfiewars;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CekilisActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private static List<CekilisTask> cekilisTasksList;
    public  DatabaseReference cekilisRef;
    public  DatabaseReference referenceNodeRef;
    private TextView infoUsersNumbTextView;
    private TextView infoMyTicketsNumbTextView;
    private TextView infoTicketsNumbTextView;
    private TextView infoProbTextView;
    private ImageView infoPrizeImageView;
    private Button rulesButton;
    public static RewardedVideoAd mRewardedVideoAd;
    private RecyclerView cekilisRecyclerView;
    private static CekilisTaskRecyclerViewAdapter cekilisTaskRecyclerViewAdapter;
    private int davethakkı = 0;
    private int katılımadet;
    private int fordöngüsü;
    private Integer mytickets;
    private float yüzdelik;
    private String photoUrl;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cekilis);
        defineTextView();
        MobileAds.initialize(CekilisActivity.this, "ca-app-pub-7004761147200711~5104636223");
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        cekilisRef = database.getReference(""+getResources().getString(R.string.cekilis_dugum_adı));
        referenceNodeRef = database.getReference("UserReference");
        cekilisTasksList = new ArrayList<>();
        setRewardAds();
        //Reklam id 1
        //Davet id 2
        //Elmas id 3

        cekilisTasksList.add(new CekilisTask("Her reklam izlediğinizde bir katılım hakkı kazanırsınız.",
                "(1 günde en fazla 5 reklam gönderilmektedir.)","Reklam Hazırlanıyor...",1));

        cekilisTasksList.add(new CekilisTask("Referans ile her kayıtta 3 adet katılım hakkı kazanırsınız.",
                "(Referans Adı: "+MainActivity.userProperties.getUserName()+" )","Davet Et Katıl",2));

        cekilisTasksList.add(new CekilisTask("30 elmas ile bir katılım hakkı kazanırsınız.",
                "(Elmas Adediniz: "+MainActivity.diamondValue+" )","Elmas ile Katıl",3));
        TaskSetup();
        getCekilisInfoFromFirebase();
    }
    public  void setRewardAds(){
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(CekilisActivity.this);
        mRewardedVideoAd.loadAd("ca-app-pub-7004761147200711/7548594756",
                new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                //Toast.makeText(CekilisActivity.this, "Reklam Yüklendi", Toast.LENGTH_SHORT).show();
                cekilisTasksList.clear();
                cekilisTasksList.add(new CekilisTask("Her reklam izlediğinizde bir katılım hakkı kazanırsınız.",
                        "(1 günde en fazla 5 reklam gönderilmektedir.)","İzle Katıl",1));

                cekilisTasksList.add(new CekilisTask("Referans ile her kayıtta 3 adet katılım hakkı kazanırsınız.",
                        "(Referans Adı: "+MainActivity.userProperties.getUserName()+" )","Davet Et Katıl",2));

                cekilisTasksList.add(new CekilisTask("30 elmas ile bir katılım hakkı kazanırsınız.",
                        "(Elmas Adediniz: "+MainActivity.diamondValue+" )","Elmas ile Katıl",3));
                cekilisTaskRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                mRewardedVideoAd.loadAd("ca-app-pub-7004761147200711/7548594756",
                        new AdRequest.Builder().build());
                cekilisTasksList.clear();
                cekilisTasksList.add(new CekilisTask("Her reklam izlediğinizde bir katılım hakkı kazanırsınız.",
                        "(1 günde en fazla 5 reklam gönderilmektedir.)","Reklam Hazırlanıyor...",1));

                cekilisTasksList.add(new CekilisTask("Referans ile her kayıtta 3 adet katılım hakkı kazanırsınız.",
                        "(Referans Adı: "+MainActivity.userProperties.getUserName()+" )","Davet Et Katıl",2));

                cekilisTasksList.add(new CekilisTask("30 elmas ile bir katılım hakkı kazanırsınız.",
                        "(Elmas Adediniz: "+MainActivity.diamondValue+" )","Elmas ile Katıl",3));
                cekilisTaskRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                cekilisRef.child(mAuth.getUid()).runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer value = mutableData.getValue(Integer.class);
                        if(value == null){
                            value = 1;
                        }else
                            value+=1;
                        mutableData.setValue(value);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (b){
                            MainActivity.showPopUpInfo(null,"Tebrikler Başarıyla Katıldınız",null,CekilisActivity.this);
                        }else {
                            MainActivity.showPopUpInfo(null,"Bir Sorun Oluştu.","Çekilis sayfası hata listemize eklendi. Anlayışınız için teşekkür ederiz.",CekilisActivity.this);
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

    public static void showAds(){
        if (mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();
            mRewardedVideoAd.loadAd("ca-app-pub-7004761147200711/7548594756",
                    new AdRequest.Builder().build());
        }else {
            mRewardedVideoAd.loadAd("ca-app-pub-7004761147200711/7548594756",
                    new AdRequest.Builder().build());
        }
    }
    public void defineTextView(){
        infoMyTicketsNumbTextView = findViewById(R.id.cekilis_odul_info_YourTicketsNumb_TextView);
        infoUsersNumbTextView = findViewById(R.id.cekilis_odul_info_usersNumb_TextView);
        infoTicketsNumbTextView = findViewById(R.id.cekilis_odul_info_ticketsNumb_TextView);
        infoPrizeImageView = findViewById(R.id.cekilis_odul_info_imageView);
        infoProbTextView = findViewById(R.id.cekilis_odul_info_prob_TextView);
        rulesButton = findViewById(R.id.cekilis_odul_info_rules_Button);
        swipeRefreshLayout = findViewById(R.id.cekilis_swipe_refresh_layout);
        FirebaseDatabase.getInstance().getReference("Announcement").child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photoUrl = dataSnapshot.getValue(String.class);
                if (photoUrl != null)
                    Picasso.with(CekilisActivity.this).load(photoUrl).networkPolicy(NetworkPolicy.OFFLINE).resize(500, 500).into(infoPrizeImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                            Picasso.with(CekilisActivity.this).load(photoUrl).resize(500, 500).into(infoPrizeImageView);
                        }
                    });
                //Toast.makeText(context, "Ödül resmi yüklenemedi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        infoPrizeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoUrl!=null)
                MainActivity.showPopupProductInfo(photoUrl, "", CekilisActivity.this);
            }
        });
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showPopUpInfo(null,"Çekiliş Kuralları ve Bilgilendirme","Bize ayırdığın vakitin önemli olduğunu düşünüyoruz. " +
                        "Bu yüzden skor tablosunda dereceye giremesende çekiliş ile ayın ödülünü kazanma fırsatı yakalayabilirsin." +
                        "\n\n1 - Çekilişe katılmak için 18 yaş ve üstü olmalısın." +
                        "\n\n2 - Çekilişe katıldıktan sonra iptal edemezsin. " +
                        "\n\n3 - Çekilişe katıldığında düşen elmasların için iade talebinde bulunamazsın. " +
                        "\n\n4 - Ödülü alacak olan ile çekilişe katılan kişi ile aynı olmalıdır. Çekilişi kazanan kişinin yerine bir başkası ödül alamaz.",CekilisActivity.this );
            }
        });

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        referencerefresh();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                    }
                }
        );
    }
    public void getCekilisInfoFromFirebase(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    infoUsersNumbTextView.setText("" + dataSnapshot.getChildrenCount());
                    katılımadet = 0;
                    fordöngüsü=0;
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        fordöngüsü++;
                        katılımadet += ds.getValue(Integer.class);
                        if(fordöngüsü == dataSnapshot.getChildrenCount()){
                            infoTicketsNumbTextView.setText(""+katılımadet);
                            if (dataSnapshot.hasChild(""+mAuth.getUid())){
                                mytickets = dataSnapshot.child(mAuth.getUid()).getValue(Integer.class);
                                infoMyTicketsNumbTextView.setText("" + mytickets.toString());
                                yüzdelik = ((float) 100 * (float) mytickets / (float) katılımadet);
                                String oran;
                                if (String.valueOf(yüzdelik).length()>3)
                                oran = String.valueOf(yüzdelik).substring(0,4);
                                else
                                    oran = String.valueOf(yüzdelik).substring(0,3);
                                infoProbTextView.setText("%"+oran);
                            }
                            else {
                                infoMyTicketsNumbTextView.setText("0");
                                infoProbTextView.setText("%0");
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        cekilisRef.addValueEventListener(valueEventListener);
        referencerefresh();;
    }
    public void referencerefresh(){
        referenceNodeRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    davethakkı = 0;
                    Integer listsize=0;
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        listsize++;
                        if(ds.getValue(Boolean.class).equals(true)){
                            davethakkı += 3;
                            //Log.d("Davet",""+davethakkı);
                        }
                        if (listsize == dataSnapshot.getChildrenCount() && davethakkı > 0){
                            cekilisRef.child(mAuth.getUid()).runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                    Integer value = mutableData.getValue(Integer.class);
                                    if(value == null){
                                        value = davethakkı;
                                    }else
                                        value += davethakkı;
                                    mutableData.setValue(value);
                                    return Transaction.success(mutableData);
                                }
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                    if (b){
                                        MainActivity.showPopUpInfo(null,"Tebrikler "+davethakkı/3+" kişinin referansıyla "+davethakkı+" katılım hakkı kazandınız.",null,CekilisActivity.this);
                                        FirebaseDatabase.getInstance().getReference("UserReference").child(mAuth.getUid()).removeValue();
                                    }else {
                                        MainActivity.showPopUpInfo(null,"Bir Sorun Oluştu.","Çekilis sayfası hata listemize eklendi. Anlayışınız için teşekkür ederiz.",CekilisActivity.this);
                                    }
                                    if(swipeRefreshLayout.isRefreshing()){
                                        swipeRefreshLayout.setRefreshing(false);

                                    }
                                }
                            });
                        }
                    }
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static void updateDiamondValue(){
        cekilisTasksList.clear();
        if (mRewardedVideoAd.isLoaded()){
            cekilisTasksList.add(new CekilisTask("Her reklam izlediğinizde bir katılım hakkı kazanırsınız.",
                    "(1 günde en fazla 5 reklam gönderilmektedir.)", "İzle Katıl", 1));
        }else {
            cekilisTasksList.add(new CekilisTask("Her reklam izlediğinizde bir katılım hakkı kazanırsınız.",
                    "(1 günde en fazla 5 reklam gönderilmektedir.)","Reklam Hazırlanıyor...",1));
        }
        cekilisTasksList.add(new CekilisTask("Referans ile her kayıtta 3 adet katılım hakkı kazanırsınız.",
                "(Referans Adı: "+MainActivity.userProperties.getUserName()+" )","Davet Et Katıl",2));

        cekilisTasksList.add(new CekilisTask("30 elmas ile bir katılım hakkı kazanırsınız.",
                "(Elmas Adediniz: "+MainActivity.diamondValue+" )","Elmas ile Katıl",3));
        cekilisTaskRecyclerViewAdapter.notifyDataSetChanged();
    }
    public void TaskSetup(){
        cekilisRecyclerView = findViewById(R.id.cekilis_recycler_view);
        cekilisTaskRecyclerViewAdapter = new CekilisTaskRecyclerViewAdapter(CekilisActivity.this,cekilisTasksList);
        cekilisRecyclerView.setLayoutManager(new LinearLayoutManager(CekilisActivity.this));
        cekilisRecyclerView.setAdapter(cekilisTaskRecyclerViewAdapter);
        cekilisRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = view.getVerticalScrollbarPosition();
                // call fragment
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelfieWars.context = CekilisActivity.this;
    }
}
