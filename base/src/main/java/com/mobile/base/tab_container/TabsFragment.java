package com.mobile.base.tab_container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.base.R;
import com.mobile.base.activity.BaseFragment;
import com.mobile.base.widget.ScrollControllableViewPager;

import java.util.ArrayList;

/**
 * Created by alvinzhang on 2017/8/4.
 */

public class TabsFragment extends BaseFragment {

    private ScrollControllableViewPager viewPager;
    private TabGroup tabGroup;
    private TabFragmentPagerAdapter adapter;
    private String[] mTitles;
    private ArrayList<BaseFragment> mFragments;
    private int[] mPics;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_library_fragment_tabs, container, false);
    }

    public void setTabs(ArrayList<BaseFragment> fragments, int[] tabPics, String[] tabTitles){
        mFragments = fragments;
        mPics = tabPics;
        mTitles = tabTitles;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showTitleBar(true);
        showTitleBarLeftBtn(false);
        viewPager = getView().findViewById(R.id.view_pager);
        tabGroup = getView().findViewById(R.id.tab_group);
        adapter = new TabFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.setTabs(mFragments);
        viewPager.setAdapter(adapter);
        viewPager.setNoScroll(true);
        for (int i = 0; i < 4; i++) {
            tabGroup.addTab(
                    new Tab(getContext())
                            .setTabImage(mPics[i])
                            .setTabText(mTitles[i])
            );
        }

        tabGroup.setOnTabSelectListener(new TabGroup.OnTabSelectListener() {
            @Override
            public void onTabSelected(View tab, int position) {
                setTitle(mTitles[position]);
                Tab item = (Tab) tab;
                if (position % 2 == 0) {
                    item.showRedPoint(true);
                } else {
                    item.showRedPointText("99+");
                }
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(View tab, int position) {
                Tab item = (Tab) tab;
                item.showRedPoint(false);
            }
        });
        tabGroup.selectedTab(0);

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
}
