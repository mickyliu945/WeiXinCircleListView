package com.micky.weixinxlistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.micky.weixinxlistview.xlistview.XScrollView;

public class MainActivity extends AppCompatActivity implements XScrollView.IXScrollViewListener{

    private XScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollView = (XScrollView) findViewById(R.id.scroll_view);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setIXScrollViewListener(this);
        View contentView = getLayoutInflater().inflate(R.layout.content_main, null);
        mScrollView.setView(contentView);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.stopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {

    }
}
