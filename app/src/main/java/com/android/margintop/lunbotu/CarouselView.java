package com.android.margintop.lunbotu;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2017/2/21.
 * 未实现切换的动画样式，可以配置如下代码实现。
 * pager.setPageTransformer(true, new RotateUpTransformer());
 * 具体的实现代码可借鉴 https://github.com/ToxicBakery/ViewPagerTransforms 。
 *
 * @描述 轮播图。
 */

public class CarouselView extends FrameLayout {

    private ViewPager mVpIcon;
    private IndicateView mIvIndicate;
    private Handler mHandler = new Handler();
    private List<ImageView> mIconList = new ArrayList<>();
    private int mChildNum = 2;
    private int mWidth;
    private int mHeight;
    private int mLeftMargin;
    private int mBottomMargin;

    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_carousel, this, true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CarouselView);
        mWidth = a.getDimensionPixelSize(R.styleable.CarouselView_cv_width, 10);
        mHeight = a.getDimensionPixelSize(R.styleable.CarouselView_cv_height, 5);
        mLeftMargin = a.getDimensionPixelSize(R.styleable.CarouselView_cv_leftMargin, 20);
        mBottomMargin = a.getDimensionPixelSize(R.styleable.CarouselView_cv_bottomMargin, 20);
        a.recycle();
        initView();
    }

    private void initView() {
        mVpIcon = (ViewPager) findViewById(R.id.vp_icon);
        mIvIndicate = (IndicateView) findViewById(R.id.iv_indicate);

        mVpIcon.setAdapter(new CarouselAdapter());
        setViewPagerListeners();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvIndicate.getLayoutParams();
        params.bottomMargin = mBottomMargin;
        mIvIndicate.setLayoutParams(params);
        mIvIndicate.setIntputData(mChildNum, mWidth, mHeight, mLeftMargin);

        stopMove(); // 清空任务，防止任务还在执行从而执行重复任务。
        startMove();
    }

    private void setViewPagerListeners() {
        mVpIcon.addOnPageChangeListener(new OnPageChangeAdapter() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIvIndicate.setMoveX(position, positionOffset);
            }
        });
        mVpIcon.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopMove();
                        break;
                    case MotionEvent.ACTION_UP:
                        startMove();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 传入轮播图资源。
     *
     * @param iconList
     */
    public void setIconList(List<ImageView> iconList) {
        mIconList = iconList;
        notifyChanged();
    }

    /**
     * 资源更新。
     */
    private void notifyChanged() {
        mVpIcon.getAdapter().notifyDataSetChanged();
        mIvIndicate.updateChildNum(mIconList.size());
    }

    /**
     * 开始轮播。
     */
    private void startMove() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = mVpIcon.getCurrentItem();
                mVpIcon.setCurrentItem((currentItem + 1) % mVpIcon.getAdapter().getCount());
                mHandler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    /**
     * 停止轮播。
     */
    private void stopMove() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 轮播图适配器。
     */
    private class CarouselAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mIconList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mIconList.get(position);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    }
}
