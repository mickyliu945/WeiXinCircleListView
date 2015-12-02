package com.micky.weixinxlistview.wlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.micky.weixinxlistview.R;

/**
 * @Project WeiXinXListView
 * @Packate com.micky.weixinxlistview.xlistview
 *
 * @Description 进度旋转动画
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-02 13:07
 * @Version 0.1
 */
public class RotateLayout extends LinearLayout {

    private static final int DURATION = 400;

    private View mContainer;
    private ImageView mIvLoading;
    private Bitmap mLoadingBitmap = null;
    private float mCurDegrees;
    private boolean mShowed = false;
    private boolean mScroll = false;


    public RotateLayout(Context context) {
        super(context);
        init();
    }

    public RotateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContainer = LayoutInflater.from(getContext()).inflate(R.layout.rotate_layout, null);
        mLoadingBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.loading);
        mIvLoading = (ImageView) mContainer.findViewById(R.id.iv_loading);
        mIvLoading.setImageBitmap(mLoadingBitmap);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, 320);
        addView(mContainer, params);
    }

    public void rotateAnimation() {
        if (mShowed) {
            RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            LinearInterpolator lin = new LinearInterpolator();
            animation.setInterpolator(lin);
            animation.setDuration(1000);
            animation.setRepeatCount(-1);
            animation.setFillAfter(true);
            mIvLoading.startAnimation(animation);
        }
    }

    public void clearAnimation() {
        mIvLoading.clearAnimation();
    }

    public void rotate(float roate) {
        int bmWidth = mLoadingBitmap.getWidth();
        int bmHeight = mLoadingBitmap.getHeight();
        mCurDegrees = mCurDegrees - roate;
        Matrix matrix = new Matrix();
        matrix.setRotate(mCurDegrees, bmWidth / 2, bmHeight / 2);
        Bitmap resizeBm = Bitmap.createBitmap(mLoadingBitmap, 0, 0, bmWidth, bmHeight, matrix, true);
        mIvLoading.setScaleType(ImageView.ScaleType.CENTER);
        mIvLoading.setImageBitmap(resizeBm);
    }

    public void showRotate() {
        if (!mShowed) {
            mShowed = true;
            mIvLoading.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_top);
            animation.setDuration(DURATION);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mScroll = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    mScroll = true;
                }
            });
        }
    }

    public void stopAnimation() {
//        clearAnimation();
        if (mShowed) {
            mShowed = false;
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_top);
            animation.setDuration(DURATION);
            animation.setFillAfter(true);
            mIvLoading.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    scrollTo(0, 0);
                }
            });
        }
    }

}
