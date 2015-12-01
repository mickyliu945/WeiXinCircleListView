package com.micky.weixinxlistview.xlistview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micky.weixinxlistview.R;

public class XListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mLayout;
    private ImageView mProgressBar;
    private TextView mHintView;
    //private ImageView mHintImage;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;
    private int mState = STATE_NORMAL;

    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        if (state == mState)
            return;
        
        if (state == STATE_LOADING) {
//            mHintImage.clearAnimation();
//            mHintImage.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mHintView.setVisibility(View.INVISIBLE);

            AnimationDrawable spinner = (AnimationDrawable) mProgressBar.getBackground();
            spinner.start();
        } else {
            mHintView.setVisibility(View.VISIBLE);
//            mHintImage.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);

            AnimationDrawable spinner = (AnimationDrawable) mProgressBar.getBackground();
            spinner.stop();
        }
        
        switch (state) {
        case STATE_NORMAL:
//            if (mState == STATE_READY) {
//                mHintImage.startAnimation(mRotateDownAnim);
//            }
//            if (mState == STATE_LOADING) {
//                mHintImage.clearAnimation();
//            }
            mHintView.setText(R.string.xlistview_footer_hint_normal);
            break;
        case STATE_READY:
            if (mState != STATE_READY) {
//                mHintImage.clearAnimation();
//                mHintImage.startAnimation(mRotateUpAnim);
                mHintView.setText(R.string.xlistview_footer_hint_ready);
            }
            break;
        case STATE_LOADING:
            break;
        }
        mState = state;
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mLayout
                .getLayoutParams();
        lp.bottomMargin = height;
        mLayout.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout
                .getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mLayout
                .getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mLayout
                .getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mContext = context;
        mLayout = LayoutInflater.from(mContext).inflate(R.layout.vw_xlistview_footer, null);
        addView(mLayout);
        mLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        mProgressBar = (ImageView) mLayout.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (TextView) mLayout.findViewById(R.id.xlistview_footer_hint_textview);
//        mHintImage = (ImageView) mLayout
//                .findViewById(R.id.xlistview_footer_hint_image);

        mRotateUpAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

}
