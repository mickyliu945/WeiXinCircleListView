package com.micky.weixinxlistview.wlistview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micky.weixinxlistview.R;

/**
 * @Project WeiXinXListView
 * @Packate com.micky.weixinxlistview.xlistview
 *
 * @Description ListView Footer (加载更多)
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-01 14:08
 * @Version 1.0
 */
public class WListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mLayout;
    private ImageView mProgressBar;
    private int mState = STATE_NORMAL;

    public WListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public WListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }
        switch (state) {
            case STATE_READY:
                mLayout.setVisibility(View.INVISIBLE);
                break;
            case STATE_LOADING:
                show();
                break;
            default:
                hide();
        }
        mState = state;
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = height;
        lp.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    public void hide() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
        mLayout.setVisibility(View.INVISIBLE);
    }

    public void show() {
        mLayout.setVisibility(View.VISIBLE);
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(lp);
        startAnimation();
    }

    public int getState() {
        return mState;
    }

    private void initView(Context context) {
        mContext = context;
        mLayout = LayoutInflater.from(mContext).inflate(R.layout.wlistview_footer, null);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        addView(mLayout, lp);
        mLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mProgressBar = (ImageView) mLayout.findViewById(R.id.wlistview_footer_progressbar);
        hide();
    }

    public void startAnimation() {
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        animation.setDuration(800);
        animation.setRepeatCount(-1);
        animation.setFillAfter(true);
        mProgressBar.startAnimation(animation);
    }
}
