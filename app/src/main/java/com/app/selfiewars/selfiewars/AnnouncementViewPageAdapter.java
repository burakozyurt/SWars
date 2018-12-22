package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.concurrent.TimeUnit;


public class AnnouncementViewPageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer image = R.drawable.instagram;
    private String photoUrl;
    private boolean isClick = false;

    public AnnouncementViewPageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.announcement_view_page, null);
        final ImageView announceLogoImageView = view.findViewById(R.id.ivAnnounceLogo);
        final ImageView announceLogoImageViewInstagram = view.findViewById(R.id.ivAnnounceLogoInstagram);
        final TextView announcementTitle = view.findViewById(R.id.tvAnnounceTitle);
        TextView announcementDesc = view.findViewById(R.id.tvAnnounceDesc);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.ivAnnounceLogoLottieView);
        if (position == 0) {
            lottieAnimationView.setVisibility(View.INVISIBLE);
            announceLogoImageView.setVisibility(View.VISIBLE);
            announceLogoImageViewInstagram.setVisibility(View.INVISIBLE);
            announcementTitle.setText(R.string.announcement_title0);
            announcementDesc.setText(R.string.announcement_description0);
            FirebaseDatabase.getInstance().getReference("Announcement").child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    photoUrl = dataSnapshot.getValue(String.class);
                    if (photoUrl != null) Picasso.with(context).load(photoUrl).resize(500, 500).into(announceLogoImageView);
                    else Toast.makeText(context, "Ödül resmi yüklenemedi", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (position == 1) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            announceLogoImageView.setVisibility(View.INVISIBLE);
            announceLogoImageView.setVisibility(View.INVISIBLE);
            announcementTitle.setText(R.string.announcement_title1);
            announcementDesc.setText(R.string.announcement_description1);
            rightOfDailyAward(announcementTitle);

        } else {
            lottieAnimationView.setVisibility(View.INVISIBLE);
            announceLogoImageView.setVisibility(View.INVISIBLE);
            announceLogoImageViewInstagram.setVisibility(View.VISIBLE);
            announceLogoImageViewInstagram.setImageResource(image);
            announcementTitle.setText(R.string.announcement_title2);
            announcementDesc.setText(R.string.announcement_description2);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isClick){
                    isClick = true;
                    if (position == 0) {
                    MainActivity.showPopupProductInfo(photoUrl, "Steel Series Efsanevi Mouse", context);
                    isClick = false;
                } else if (position == 1) {
                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                        rightOfDailyAwardIncreaseValue(announcementTitle);
                } else {
                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/"));
                    context.startActivity(intent);
                    isClick = false;
                    }

                }
            }

        });
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    public void rightOfDailyAward(final TextView title) {
        final FirebaseDatabase mydatabase;
        final DatabaseReference myRefUser;
        final FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance();
        myRefUser = mydatabase.getReference("Users/"+mAuth.getUid());

        myRefUser.child("nowtimestamp").child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("nowtimestamp").hasChild("timestamp")) {
                            final Long rightofdailyawardMilis;
                            final Long nowTimestamp;
                            nowTimestamp = dataSnapshot.child("nowtimestamp").child("timestamp").getValue(Long.class);
                            if(dataSnapshot.hasChild("rightofdailyaward")){
                                rightofdailyawardMilis = dataSnapshot.child("rightofdailyaward").child("dailyAwardValue").getValue(Long.class);
                                if (nowTimestamp < rightofdailyawardMilis) {
                                    title.setText("Günlük Ödüller");
                                } else {
                                    title.setText("Günlük Ödüller Hazır");
                                    }
                            }else {
                                title.setText("Günlük Ödüller Hazır");
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    public void rightOfDailyAwardIncreaseValue(final TextView title){
        final FirebaseDatabase mydatabase;
        final DatabaseReference myRefUser;
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mydatabase = FirebaseDatabase.getInstance();
        myRefUser = mydatabase.getReference("Users/"+mAuth.getUid());

        myRefUser.child("nowtimestamp").child("timestamp").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("nowtimestamp").hasChild("timestamp")) {
                            final Long rightofdailyawardMilis;
                            final Long nowTimestamp;
                            nowTimestamp = dataSnapshot.child("nowtimestamp").child("timestamp").getValue(Long.class);
                            if(dataSnapshot.hasChild("rightofdailyaward")){ // Kullanıcı daha önceden ödül almışmı almamışmı (Yeni kullanıcı mı değil mi kontrolü)
                                rightofdailyawardMilis = dataSnapshot.child("rightofdailyaward").child("dailyAwardValue").getValue(Long.class);
                                if (nowTimestamp < rightofdailyawardMilis) {// ödülün süresi dolmuşmu dolmaş mı (24 saat geçim mi geçmemiş mi kontrolü)
                                    MainActivity.showPopUpInfo(null,"Günlük ödül henüz yenilenmedi","Günlük ödülünüz "+
                                            TimeUnit.MILLISECONDS.toHours(rightofdailyawardMilis-nowTimestamp)+" saat sonra yenilenecektir.",context);
                                    title.setText("Günlük Ödüller");
                                    isClick = false;
                                } else { // 24 saat geçmiş demektir ve kullanıcıya ödül verilir.
                                    final Integer diamondValue = dataSnapshot.child("token").child("diamondValue").getValue(Integer.class) + 1;
                                    Integer fiftyFiftyValue = dataSnapshot.child("wildcards").child("fiftyFiftyValue").getValue(Integer.class);
                                    Integer doubleDipValue = dataSnapshot.child("wildcards").child("doubleDipValue").getValue(Integer.class);
                                    Integer healthValue = dataSnapshot.child("wildcards").child("healthValue").getValue(Integer.class);
                                    final Wildcards wildcards = new Wildcards(doubleDipValue + 1,fiftyFiftyValue+1,healthValue+1);
                                    myRefUser.child("rightofdailyaward").child("dailyAwardValue").setValue(nowTimestamp + 86400000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            myRefUser.child("token").child("diamondValue").setValue(diamondValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    myRefUser.child("wildcards").setValue(wildcards).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            title.setText("Günlük Ödüller");
                                                            MainActivity.showPopUpInfo(null,"Günlük ödüller başarılya alındı",null,context);
                                                            isClick = false;
                                                        }
                                                    }) ;
                                                }
                                            });
                                        }
                                    });

                                }
                            }else { //düğüm yoksa kullanıcı yeni kayıt olmuş demektir. Ödül verirlir.
                                final Integer diamondValue = dataSnapshot.child("token").child("diamondValue").getValue(Integer.class) + 1;
                                Integer fiftyFiftyValue = dataSnapshot.child("wildcards").child("fiftyFiftyValue").getValue(Integer.class);
                                Integer doubleDipValue = dataSnapshot.child("wildcards").child("doubleDipValue").getValue(Integer.class);
                                Integer healthValue = dataSnapshot.child("wildcards").child("healthValue").getValue(Integer.class);
                                final Wildcards wildcards = new Wildcards(doubleDipValue + 1,fiftyFiftyValue+1,healthValue+1);
                                myRefUser.child("rightofdailyaward").child("dailyAwardValue").setValue(nowTimestamp + 86400000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        myRefUser.child("token").child("diamondValue").setValue(diamondValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                myRefUser.child("wildcards").setValue(wildcards).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        title.setText("Günlük Ödüller");
                                                        MainActivity.showPopUpInfo(null,"Günlük ödüller başarılya alındı",null,context);
                                                        isClick = false;
                                                    }
                                                }) ;
                                            }
                                        });
                                    }
                                });
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
