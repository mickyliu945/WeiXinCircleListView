package com.micky.weixinxlistview.wlistview;

import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.micky.weixinxlistview.R;
import com.micky.weixinxlistview.utils.ViewUtils;

/**
 * @Project WeiXinXListView
 * @Packate com.micky.weixinxlistview.xlistview
 *
 * @Description ListView
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-01 14:02
 * @Version 1.0
 */
public class WListView extends ListView implements OnScrollListener {

    private float mLastY = -1;
    private Scroller mScroller;
    private IWListViewListener mListViewListener;


    private WListViewHeader mHeaderView;

    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight = 0;
    private boolean mPullRefreshing = false;

    private WListViewFooter mFooterView;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    private int mTotalItemCount;
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;
    private final static int PULL_LOAD_MORE_DELTA = 150;
    private final static int PULL_REFRESH_DELTA = 100;

    private final static float OFFSET_RADIO = 1.8f;


    private LinearLayout footerPl;

    private RotateLayout mRotateLayout;
    private int mLastItem = 0;

    public WListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public WListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public WListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
//        setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        DisplayMetrics metrics =  ViewUtils.getScreenInfo(context);
        mHeaderViewHeight = (int) (60 * metrics.density);

        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        mHeaderView = new WListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        addHeaderView(mHeaderView);

        mFooterView = new WListViewFooter(context);
        footerPl = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        footerPl.addView(mFooterView, params);

        ViewTreeObserver observer = mHeaderView.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mHeaderViewHeight = mHeaderViewContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();
                    if (null != observer) {
                        observer.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!mIsFooterReady) {
            mIsFooterReady = true;
            addFooterView(footerPl);
        }
        super.setAdapter(adapter);
    }

    public void setRotateLayout(RotateLayout rotateLayout) {
        mRotateLayout = rotateLayout;
    }

    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            mRotateLayout.stopAnimation();
        }
    }

    public void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(WListViewFooter.STATE_NORMAL);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        setSelection(0);
    }


    public void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0)
            return;

        int finalHeight = mHeaderViewHeight;
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        mFooterView.setBottomMargin(height);
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(WListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight && mHeaderView.getTop()  == 0) {
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                       if (mHeaderView.getVisiableHeight() >= (mHeaderViewHeight + PULL_REFRESH_DELTA)) {
                           mRotateLayout.showRotate();
                           mRotateLayout.rotate(-2 * (int) deltaY);
                           if (mListViewListener != null && !mPullRefreshing) {
                               mPullRefreshing = true;
                               mListViewListener.onRefresh();
                           }
                       } else {
//                           resetHeaderHeight();
                       }
                    }
                }
                if (deltaY < 0 && getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() >= 0 )) {
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
//                    mFooterView.setState(WListViewFooter.STATE_READY);
                }

                if (mHeaderView.getTop() <= (PULL_REFRESH_DELTA * -0.5)) {
                    mRotateLayout.stopAnimation();
                }
                break;
            default:
                mLastY = -1;
                if (getFirstVisiblePosition() == 0) {

                    if (mHeaderView.getVisiableHeight() >= (mHeaderViewHeight + PULL_REFRESH_DELTA) && !mPullRefreshing) {
                        mPullRefreshing = true;
                        if (mListViewListener != null && !mPullRefreshing) {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }
                resetFooterHeight();
//                if ((getLastVisiblePosition() == mTotalItemCount - 1)) {
//                    if (mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
//                        startLoadMore();
//                    }
//                    resetFooterHeight();
//                }
                mRotateLayout.rotateAnimation();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void autoRefresh() {
        mPullRefreshing = true;
        mListViewListener.onRefresh();
        mRotateLayout.showRotate();
        mRotateLayout.rotateAnimation();
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (mLastItem == getCount() - 1) {
                startLoadMore();
                setSelection(getCount());
            }
            if (mFooterView != null && mFooterView.getState() != WListViewFooter.STATE_LOADING) {
                mFooterView.setState(WListViewFooter.STATE_NORMAL);
            }
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        mTotalItemCount = totalItemCount;
        mLastItem = firstVisibleItem + visibleItemCount - 1;
    }

    public void setWListViewListener(IWListViewListener l) {
        mListViewListener = l;
    }

    public interface IWListViewListener {
        void onRefresh();

        void onLoadMore();
    }
}
