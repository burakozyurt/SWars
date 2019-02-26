package com.app.selfiewars.selfiewars;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InAppBillingActivity extends AppCompatActivity{
    private BillingClient mBillingClient;
    private String itemid;
    private Integer diamondValue;
    String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArF24rEi8440rpP/s9fCK+gg5UyeybUG6TD5TPCEc+AGVKC1WElChAztG3ZnllFYMSwJlV6oE0p+liVjeALDVTG28FzW9cNb2pramyjLckvp06SBatRXWaOPkE9hN2mlkjM14bp/R416kBA5Nz1U0ieUkcrgHufLz+4uK/b482ufwLplAsaJSSpU+MNbvhFvfeUoBNiG5FAWMVsUyaSC20yeX4IMq2Hpaib5AVFLPLB5p8Ua+fGHuVcUZtqrCbhZAwo4PISovvurcv/56hNg/HsPceXLDxPCAYtyc3Yot3/Js3ZNUFKTBwrRWcjElCDeafU4vtqgazUMFN6uSeqnxTQIDAQAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_billing);
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            itemid = bundle.getString("skuid");
            Log.d("InAppBillingBB",itemid);
            diamondValue = Integer.valueOf(itemid);
            mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
                @Override
                public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                    if (responseCode == BillingClient.BillingResponse.OK
                            && purchases != null) { //satın alma başarılı
                        for (final Purchase purchase : purchases) {
                            if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                                // Invalid purchase
                                // show error to user
                                Toast.makeText(InAppBillingActivity.this, "Got a purchase: " + purchase + "; but signature is bad. Skipping...", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                // purchase is valid
                                // Perform actions
                                mBillingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
                                    @Override
                                    public void onConsumeResponse(int responseCode, String purchaseToken) {
                                        if (responseCode == BillingClient.BillingResponse.OK) {
                                            //satın alma tamamlandı yapacağınız işlemler
                                            //Toast.makeText(InAppBillingActivity.this, "Satın alındı"+responseCode, Toast.LENGTH_SHORT).show();
                                            MainActivity.myRefUser.child("token").child(getResources().getString(R.string.diamondValue)).runTransaction(new Transaction.Handler() {
                                                @NonNull
                                                @Override
                                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                    Integer oldvalue = mutableData.getValue(Integer.class);
                                                    if (oldvalue != null) {
                                                        oldvalue += diamondValue;
                                                        mutableData.setValue(oldvalue);
                                                        return Transaction.success(mutableData);
                                                    } else {
                                                        return null;
                                                    }
                                                }

                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                                    if (b) {
                                                        //onBackPressed();
                                                        showPopUpInfo(null,"Satın Alma İşlemi Başarılı","Hesabınıza "+diamondValue+" adet elmsa aktarıldı.");

                                                    } else {
                                                        MainActivity.myRefUser.child("token").child(getResources().getString(R.string.diamondValue)).setValue(MainActivity.diamondValue + diamondValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                showPopUpInfo(null,"Satın Alma İşlemi Başarılı","Hesabınıza "+diamondValue+" adet elmsa aktarıldı.");
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                showPopUpInfo(null,"Satın Alma İşlemi Başarısız","Bir aksaklık yaşandı. Lütfen selfiewarinfo@gmail.com adresi ile iletişime geçiniz.");
                                                                //Toast.makeText(InAppBillingActivity.this, "Bir aksaklık yaşandı. Lütfen selfiewarinfo@gmail.com adresi ile iletişime geçiniz. : ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });


                                        }
                                    }
                                });
                            }

                        }
                    } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {//kullanıcı iptal etti
                        // Handle an error caused by a user canceling the purchase flow.
                        //billingCanceled(); //kullanıcı iptal etti
                        //Toast.makeText(InAppBillingActivity.this, "Satın alma işlemi başarısız. : " + responseCode, Toast.LENGTH_SHORT).show();
                        //onBackPressed();
                        //showPopUpInfo(null,"İptal Edildi","Sunucudan bilgi alınırken hata oluştu. (DF-AA-20)");
                        finish();
                    } else if (responseCode == BillingClient.BillingResponse.ERROR) {
                        //billingCanceled(); //bir sorun var
                        showPopUpInfo(null,"Bağlantı Hatası","Sunucudan bilgi alınırken hata oluştu. (DF-AA-20)");
                        //Toast.makeText(InAppBillingActivity.this, "Bağlantı Hatası. : " + responseCode, Toast.LENGTH_SHORT).show();
                    } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED){
                        showPopUpInfo(null,"Ürün Önceden Satın Alınmıştı!","Bu ürünü zaten satın almıştınız.");
                        //Toast.makeText(InAppBillingActivity.this, "Bu ürünü zaten satın almıştınız.", Toast.LENGTH_SHORT).show();
                        //finish();
                    }else{
                        Toast.makeText(InAppBillingActivity.this, "Sorun Oluştu. : "+responseCode, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).build();

            mBillingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                    if (billingResponseCode == BillingClient.BillingResponse.OK) {
                        // The billing client is ready. You can query purchases here.
                        // BUTONLARI AKTIF ET
                        List<String> skuList = new ArrayList<>();
                        skuList.add(itemid+"_diamond");

                        SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                                .setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build();
                        mBillingClient.querySkuDetailsAsync(skuDetailsParams,
                                new SkuDetailsResponseListener() {
                                    @Override
                                    public void onSkuDetailsResponse(int responseCode,
                                                                     List<SkuDetails> skuDetailsList) {
                                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(skuDetailsList.get(0))
                                                .build();
                                        int billingResponseCode = mBillingClient.launchBillingFlow(InAppBillingActivity.this, flowParams);
                                        if (billingResponseCode == BillingClient.BillingResponse.OK) {
                                            // do something you want
                                           // Toast.makeText(InAppBillingActivity.this, "Başarılı", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                        //Toast.makeText(InAppBillingActivity.this, "Başarılı", Toast.LENGTH_SHORT).show();
                        //querySkuDetail();

                    } else {
                        //TODO Kullanıcıya uyarı ver
                        //Toast.makeText(InAppBillingActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    //TODO Kullanıcıya uyarı ver
                    Toast.makeText(InAppBillingActivity.this, "Bağlantı kesildi.", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public void showPopUpInfo(Integer ImageViewDraw, String Title, String Description) {
        final Dialog mydialog = new Dialog(InAppBillingActivity.this);
        mydialog.setContentView(R.layout.popup_info);
        mydialog.getWindow().getAttributes().windowAnimations = R.style.UptoDown;
        ImageView ımageView;
        TextView titleView;
        TextView descriptionView;
        Button btnOk;

        ımageView = mydialog.findViewById(R.id.popupInfo_Image);
        titleView = mydialog.findViewById(R.id.popupInfo_TitleTextView);
        descriptionView = mydialog.findViewById(R.id.popupInfo_descriptionTextView);
        btnOk = mydialog.findViewById(R.id.popupInfo_BtnOkey);

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
                finish();
                mydialog.dismiss();
            }
        });
    }
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            //Log.e(TAG, "Got an exception trying to validate a purchase: " + e);
            return false;
        }
    }
}
