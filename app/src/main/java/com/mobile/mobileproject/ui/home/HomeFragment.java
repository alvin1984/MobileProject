package com.mobile.mobileproject.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobile.base.activity.BaseFragment;
import com.mobile.base.util.DensityUtils;
import com.mobile.base.util.ToastUtils;
import com.mobile.base.widget.recycler_view.FreshRecyclerView;
import com.mobile.base.widget.recycler_view.OnLoadListener;
import com.mobile.mobileproject.R;
import com.mobile.mobileproject.TestFragment;
import com.mobile.mobileproject.ui.main.GlideImageLoader;
import com.mobile.network.NetworkManager;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import okhttp3.Response;

public class HomeFragment extends BaseFragment {

    private FreshRecyclerView freshRecyclerView;
    private MyAdapter adapter;
    private Banner banner;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(false);

        freshRecyclerView = getView().findViewById(R.id.fresh_recycler_view);
        freshRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.toast(getActivity(), "clicked me");
            }
        });

        adapter = new MyAdapter();
        freshRecyclerView.setAdapter(adapter);


        banner = (Banner) LayoutInflater.from(getActivity()).inflate(R.layout.banner_layout,null,false);
        banner.setImageLoader(new GlideImageLoader());
        ArrayList<String> images1 = new ArrayList<>();
        images1.add("http://img4.duitang.com/uploads/item/201408/30/20140830185456_Eijik.jpeg");
        images1.add("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg");
        images1.add("http://static10.photo.sina.com.cn/middle/5a3ab1b1x9961016a8699&690");
        banner.setImages(images1);
        banner.setBannerAnimation(Transformer.Default);
        banner.setDelayTime(4000);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.toast(getActivity(),"position:" + position);
                startFragment(TestFragment.class,null);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getActivity(),160));
        banner.setLayoutParams(layoutParams);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        freshRecyclerView.addHeaderView(banner);

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
                        ToastUtils.toast(getActivity(), "onLoadingMore");
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
                if (position == 0){
                    NetworkManager.doGet("http://www.sojson.com/open/api/weather/json.shtml?city=北京",true)
                                .subscribe(new Consumer<Response>() {
                                    @Override
                                    public void accept(Response response) throws Exception {
                                        if (response.isSuccessful()){
                                            Log.d("zhanghx",response.body().string());
//                                            ToastUtils.toast(getActivity(),response.body().string());
                                        }
                                    }
                                });
                }else {
                    NetworkManager.doGet("http://www.sojson.com/open/api/weather/json.shtml?city=深圳",true)
                            .subscribe(new Consumer<Response>() {
                                @Override
                                public void accept(Response response) throws Exception {
                                    if (response.isSuccessful()){
                                        Log.d("zhanghx",response.body().string());
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main,container,false);
    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    @Override
    public void onDestroyFragment() {

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
            View item = LayoutInflater.from(getActivity()).inflate(R.layout.test_item, parent, false);
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
