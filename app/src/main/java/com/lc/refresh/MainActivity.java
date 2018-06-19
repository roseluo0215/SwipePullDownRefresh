package com.lc.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private SwipePullDownRefresh mRefreshList;
  private List<ListData> mData = new ArrayList<>();
  private NewsListAdapter mAdapter;
  private TextView mTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mRefreshList = findViewById(R.id.refresh_list);
    mTextView = findViewById(R.id.tv_no_data);
    mAdapter = new NewsListAdapter(this);
    mAdapter.setData(mData);
    mRefreshList.setAdapter(mAdapter);
    setListener();

    new Handler().postDelayed(new Runnable() {// 模拟网络请求延迟两秒
      @Override
      public void run() {
        // 制作假数据
        ArrayList<ListData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
          ListData data = new ListData();
          data.setTitle("数据" + i);
          list.add(data);
        }
        setData(list);
      }
    }, 2000);

  }

  private void setData(List<ListData> data) {
    mTextView.setVisibility(mData.size() == 0 ? View.VISIBLE : View.GONE);
    mData.addAll(data);
    mAdapter.setData(mData);
    mAdapter.notifyDataSetChanged();
  }

  private void setListener() {
    mRefreshList.setRefreshListener(new SwipePullDownRefresh.IRefreshListener() {
      @Override
      public void onPullDownToRefresh() {
        mRefreshList.setRefreshing(false);
      }

      @Override
      public void onPullUpToRefresh() {
        // 制作假数据
        final ArrayList<ListData> listData = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
          ListData data = new ListData();

          data.setTitle("加载更多" + i);
          listData.add(data);
        }
        new Handler().postDelayed(new Runnable() {// 模拟网络请求延迟两秒
          @Override
          public void run() {
            setData(listData);
            mRefreshList.setLoading(false);
          }
        }, 2000);
      }
    });
  }
}
