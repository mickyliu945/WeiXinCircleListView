package com.micky.weixinxlistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.micky.weixinxlistview.wlistview.RotateLayout;
import com.micky.weixinxlistview.wlistview.WListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project WeiXinXListView
 * @Packate com.micky.weixinxlistview.xlistview
 *
 * @Description
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-01 12:22
 * @Version 1.0
 */
public class MainActivity extends AppCompatActivity implements WListView.IWListViewListener{

    private static final int PAGE_SIZE = 10;

    private WListView mListView;
    private UserAdapter mUserAdapter;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (WListView) findViewById(R.id.list_view);
        mListView.setWListViewListener(this);
        mUserAdapter = new UserAdapter(this);
        mListView.setAdapter(mUserAdapter);

        mUserAdapter.appendData(loadData());

        RotateLayout rotateLayout = (RotateLayout) findViewById(R.id.rotate_layout);
        mListView.setRotateLayout(rotateLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListView.autoRefresh();
    }

    private List<String> loadData() {
        return loadData(mCount++);
    }

    private List<String> loadData(int page) {
        List<String> list = new ArrayList<String>();
        int start = page * PAGE_SIZE;
        int end = (page + 1) * PAGE_SIZE;
        for (int i = start; i < end; i++) {
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
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUserAdapter.appendData(loadData());
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        }, 3000);
    }
}
