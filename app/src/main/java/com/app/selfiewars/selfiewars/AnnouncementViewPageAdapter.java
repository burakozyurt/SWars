package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class AnnouncementViewPageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer image = R.drawable.instagram;
    private String photoUrl;

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
        TextView announcementTitle = view.findViewById(R.id.tvAnnounceTitle);
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
                    if(photoUrl !=null)Picasso.get().load(photoUrl).resize(500, 500).into(announceLogoImageView);
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
               if(position ==0){
                   MainActivity.showPopupProductInfo(photoUrl,"Steel Series Efsanevi Mouse",context);
               }else if(position == 1){
                   Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(Intent.ACTION_VIEW);
                   intent.setData(Uri.parse("https://www.youtube.com/"));
                   context.startActivity(intent);
               }
            }
        });
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
