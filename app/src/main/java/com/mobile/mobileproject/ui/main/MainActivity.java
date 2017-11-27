package com.mobile.mobileproject.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.base.activity.BaseActivity;
import com.mobile.base.util.PermissionHelper;
import com.mobile.base.util.ToastUtils;
import com.mobile.base.widget.recycler_view.FreshRecyclerView;
import com.mobile.base.widget.recycler_view.OnLoadListener;
import com.mobile.mobileproject.R;
import com.mobile.mobileproject.TestFragment;
import com.mobile.mobileproject.ui.html.FileReviewActivity;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private FreshRecyclerView freshRecyclerView;
    private MyAdapter adapter;
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        setContentView(R.layout.activity_main);



        freshRecyclerView = (FreshRecyclerView) findViewById(R.id.fresh_recycler_view);
//        banner = (Banner) findViewById(R.id.banner);

        //设置图片加载器
        banner = new Banner(this);
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        ArrayList<String> images = new ArrayList<>();
        images.add("http://img4.duitang.com/uploads/item/201408/30/20140830185456_Eijik.jpeg");
        images.add("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg");
        images.add("http://static10.photo.sina.com.cn/middle/5a3ab1b1x9961016a8699&690");
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.Default);
        banner.setDelayTime(4000);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.toast(MainActivity.this,"position:" + position);
                startFragment(TestFragment.class,null);
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();


        freshRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.toast(MainActivity.this, "clicked me");
            }
        });

        adapter = new MyAdapter();
        freshRecyclerView.setAdapter(adapter);
        freshRecyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onRefresh() {
                adapter = new MyAdapter();
                freshRecyclerView.setAdapter(adapter);
                freshRecyclerView.onComplete();
            }

            @Override
            public void onLoadMore() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.toast(MainActivity.this, "onLoadingMore");
//                        adapter.addData();
                        freshRecyclerView.onComplete();
                        freshRecyclerView.setManualLoad(true);
                    }
                }, 3000);
            }
        });

        freshRecyclerView.setOnItemClickListener(new FreshRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View item) {
                Intent intent = new Intent(MainActivity.this, FileReviewActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionHelper.requestPermissions("", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                ToastUtils.toast(MainActivity.this,permission[0]);
            }

            @Override
            public void doAfterDenied(String... permission) {
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE);
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<String> data;

        public MyAdapter() {
            data = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                data.add("AA");
            }
        }

        public void addData(){
            ArrayList<String> result = new ArrayList<>();
            for (int i = 0; i < 14; i++) {
                result.add("AA");
            }
            data.addAll(result);
            notifyDataSetChanged();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.test_item, parent, false);
            return new MyViewHolder(item);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
