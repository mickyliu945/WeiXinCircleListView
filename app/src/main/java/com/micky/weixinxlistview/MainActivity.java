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
 * @Version 0.1
 */
public class MainActivity extends AppCompatActivity implements WListView.IWListViewListener{

    private WListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (WListView) findViewById(R.id.list_view);
        mListView.setPullLoadEnable(true);
        mListView.setWListViewListener(this);
        UserAdapter adapter = new UserAdapter(this, initData());
        mListView.setAdapter(adapter);

        RotateLayout rotateLayout = (RotateLayout) findViewById(R.id.rotate_layout);
        mListView.setRotateLayout(rotateLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListView.autoRefresh();
    }

    private List<String> initData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
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
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        }, 3000);
    }
}
