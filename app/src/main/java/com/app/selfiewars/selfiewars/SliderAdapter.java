package com.app.selfiewars.selfiewars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Animation smalltoBig;
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
        smalltoBig = AnimationUtils.loadAnimation(context,R.anim.updownmove);
    }

    //Dizi
    public int[] slideimages = {
                R.drawable.selfiewarslogofit,
                R.drawable.slide1,
                R.drawable.slide2,
            R.drawable.slide3};
    public String[] slidetitle = {
            "Yarışmaya Hoşgeldin!",
            "Ödül Merkezi",
            "Elmas ve Jokerler",
            "Çark Çevir."
    };
    public String[] slidedesc = {
            "",
            "Her ay sizler için ödüllerimiz var.\n" +
                    "Puan tablosunda dereceye gir aylık ödülü kazan!",
            "Sorularda jokerlerini kullanarak daha çok \n" +
                    "tahmin yapabilirsin.",
            "Çark çevirerek yardımcıları kazanabilir ve\n" +
                    "puanını yükseltebilirsin."
    };

    @Override
    public int getCount() {
        return slidetitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slideImageView = view.findViewById(R.id.splashSlideImage);
        TextView title = view.findViewById(R.id.splashTitleTextView);
        TextView desc = view.findViewById(R.id.splahsdescTextView);
        slideImageView.setImageResource(slideimages[position]);
        title.setText(slidetitle[position]);
        desc.setText(slidedesc[position]);
        if(slideImageView.getAnimation() != null){
            slideImageView.getAnimation().reset();
            slideImageView.startAnimation(smalltoBig);
        }else {
            slideImageView.startAnimation(smalltoBig);
        }
        if(position == 0){
            title.startAnimation(smalltoBig);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

}
