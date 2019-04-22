package com.app.selfiewars.selfiewars;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class SelfieWars extends Application implements InternetConnectivityListener {
    private Dialog mydialog;
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    public static Context context;
    private boolean isLowMemory = false;
    public static boolean connection = true;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseInAppMessaging.getInstance().setMessagesSuppressed(false);
        /* Picasso Builder*/
        context = SelfieWars.this;
        mydialog = new Dialog(context);
        InternetAvailabilityChecker.init(this);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true );
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        connection = isConnected;
        if (!isLowMemory){
            if (!isConnected){
                if (!mydialog.isShowing()){
                    showPopUpInfo(R.drawable.noconnection,"Bağlantı Kesildi","Lütfen internet bağlantını kontrol et.",context,false);
                }else {
                    mydialog.dismiss();
                    showPopUpInfo(R.drawable.noconnection,"Bağlantı Kesildi","Lütfen internet bağlantını kontrol et.",context,false);
                }
            }else {
                if (mydialog != null && mydialog.isShowing()){
                    mydialog.dismiss();
                    showPopUpInfo(R.drawable.connection,"Başarıyla Bağlanıldı.",null,context,true);
                }
            }
        }

    }
    private void showPopUpInfo(Integer ImageViewDraw, String Title, String Description, Context context,Boolean isButton) {
        mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.popup_info);
        //mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        ImageView ımageView;
        TextView titleView;
        TextView descriptionView;
        Button btnOk;

        ımageView = mydialog.findViewById(R.id.popupInfo_Image);
        titleView = mydialog.findViewById(R.id.popupInfo_TitleTextView);
        descriptionView = mydialog.findViewById(R.id.popupInfo_descriptionTextView);
        btnOk = mydialog.findViewById(R.id.popupInfo_BtnOkey);
        mydialog.setCancelable(false);
        if (!isButton){
            btnOk.setVisibility(View.GONE);
        }else {
            btnOk.setVisibility(View.VISIBLE);
            descriptionView.setVisibility(View.GONE);


        }
        if (ImageViewDraw !=null){
            ımageView.setImageResource(ImageViewDraw);
            ımageView.setVisibility(View.VISIBLE);
        }
        else ımageView.setVisibility(View.GONE);
        if (Title !=null)
            titleView.setText(Title);

        if(Description !=null)
            descriptionView.setText(Description);
        mydialog.setCanceledOnTouchOutside(false);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        isLowMemory = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLowMemory = true;
    }
}
