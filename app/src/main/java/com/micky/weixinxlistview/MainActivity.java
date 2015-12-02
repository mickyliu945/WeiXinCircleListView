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

import com.micky.weixinxlistview.xlistview.XListView;
import com.micky.weixinxlistview.xlistview.XScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{

    private XListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (XListView) findViewById(R.id.list_view);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        UserAdapter adapter = new UserAdapter(this, initData());
        mListView.setAdapter(adapter);
    }

    private List<String> initData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add("item-" + i);
        }
        return list;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        }, 2000);
    }
}
