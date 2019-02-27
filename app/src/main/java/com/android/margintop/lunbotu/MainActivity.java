package com.android.margintop.lunbotu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int[] iconResId = {R.mipmap.banner, R.mipmap.banner, R.mipmap.banner};
    private List<ImageView> mImageViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CarouselView cvCarousel = (CarouselView) findViewById(R.id.cv_carousel);
        for (int i = 0; i < iconResId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(iconResId[i]);
            mImageViewList.add(imageView);
        }
        cvCarousel.setIconList(mImageViewList);
    }
}
