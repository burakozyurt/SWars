package com.app.selfiewars.selfiewars;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class UserAgreementandSSSActivity extends AppCompatActivity {

    private List<SSSItem> sssItemList;
    private RecyclerView sss_RecyclerView;
    private SSSRecyclerViewAdapter sssRecyclerViewAdapter;
    private NestedScrollView agreementScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_agreementand_sss);
        sssItemList = new ArrayList<>();
        sss_RecyclerView = findViewById(R.id.sss_RecyclerView);
        agreementScrollView = findViewById(R.id.agreementScrollView);
        sssRecyclerViewAdapter = new SSSRecyclerViewAdapter(getApplicationContext(), sssItemList);
        sss_RecyclerView.setLayoutManager(new LinearLayoutManager(UserAgreementandSSSActivity.this));
        sss_RecyclerView.setAdapter(sssRecyclerViewAdapter);
        Bundle bundle = getIntent().getExtras();
        if ((bundle != null)) {
            if (bundle.get("type").equals("agreement")) {
                agreementScrollView.setVisibility(View.VISIBLE);
                sss_RecyclerView.setVisibility(View.GONE);
            } else {
                sss_RecyclerView.setVisibility(View.VISIBLE);
                agreementScrollView.setVisibility(View.GONE);

            }
        }
        setItemSSS();
    }

    public void setItemSSS() {
        sssItemList.add(new SSSItem("SelfieWars Nedir?\n", "SelfieWars temelde tahmin yapma üzerine kurulu bir mobil oyundur. " +
                "Karşına çıkan kişilerin fotoğraflarına bakarak tahminler yaparsın ve tahminlerden kazandığın puanlarla aylık ödülleri kazanma fırsatı yakalarsın. " +
                "Unutma her soru daha yüksek puan değerine sahiptir. "));

        sssItemList.add(new SSSItem("Günde ne kadar tahmin yapabilirim?\n", "Her oyuncuya günlük 10 adet tahmin etme hakkı verilmektedir. " +
                "Toplamda günlük 20 adet tahmin etme hakkına sahipsin"));

        sssItemList.add(new SSSItem("Günlük tahmin limitini arttırabilir miyim?\n", "Market bölümünden elmaslarla günlük tahmin hakkınızın üzerine çıkabilirsin.\n "));

        sssItemList.add(new SSSItem("Kazandığım puanlar ne işime yarar?\n", "Tahminlerden kazandığın puanlarla aylık ödüllere bir adım daha yaklaş! "));

        sssItemList.add(new SSSItem("Günlük ödüller nedir?\n", "Bize ayırmış olduğunuz vaktin değerli olduğunu düşünüyoruz. " +
                "Bu yüzden sana günlük hediyelerimiz var. x2 joker, 1 elmas ve 1 can her gün bizden hediye!\n"));

        sssItemList.add(new SSSItem("Jokerler ne işe yarar?\n", "Verilen jokerler tahminlerine yardımcı olabilmek için tasarlandı." +
                " %50-%50 jokerimiz ile 4 şıktan 2 tanesi kaybolur ve tahmin etmek kolaylaşır. " +
                "X2 jokerimiz ile iki kez şık işaretleme yapabilirsin. Can ile kaldığın sorudan devam edebilir ve daha yüksek skorlar elde edebilirsin. \n"));

        sssItemList.add(new SSSItem("Aylık ödül sistemi nasıl çalışıyor?\n", "Her ay sizler için ödüllerimiz var. " +
                "Skor tablosunda dereceye gir ve aylık ödülü kazanma fırsatını kaçırma!\n"));

        sssItemList.add(new SSSItem("Ödül almak için ne yapmalıyım?\n", "Bolca rekabetin olduğu SelfieWars'ta en iyi tahminleri sen yap ve aylık ödülü kap! " +
                "Soruların değerleri giderek artarak sana daha çok puan kazandırır bu yüzden yapabildiğinin en iyisini yap. Bol şans!\n"));

        sssItemList.add(new SSSItem("Profil bilgilerimi neden girmeliyim?\n", "Mağazadan aldığın ürünlerin kargolanabilmesi için adres bilgilerine ihtiyacımız var. " +
                "Bizimle paylaştığın için elmaslarla ödüllendirileceksin.\n"));

        sssItemList.add(new SSSItem("Ay bitiminde puanlar sıfırlanır mı?\n", "Her ay puanlar sıfırlanır ve ayın ödülleri değişir.\n"));

        sssItemList.add(new SSSItem("Çark sistemi nasıl işliyor?\n", "Bizlere ayırdığın vakitin önemli olduğunu düşünüyoruz." +
                " Bu yüzden çark çevirerek yardımcıları kazan ve puanını yükselt. " +
                "Unutma çarkı günde bir kere çevirebilirsin!\n"));

        sssRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelfieWars.context = UserAgreementandSSSActivity.this;

    }
}
