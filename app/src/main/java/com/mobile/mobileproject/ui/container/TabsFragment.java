package com.mobile.mobileproject.ui.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.base.activity.BaseFragment;
import com.mobile.base.widget.ScrollControllableViewPager;
import com.mobile.mobileproject.R;

/**
 * Created by alvinzhang on 2017/8/4.
 */

public class TabsFragment extends BaseFragment {

    private ScrollControllableViewPager viewPager;
    private TabGroup tabGroup;
    private TabFragmentPagerAdapter adapter;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showTitleBar(true);
        showTitleBarLeftBtn(false);
        viewPager = getView().findViewById(R.id.view_pager);
        tabGroup = getView().findViewById(R.id.tab_group);

        adapter = new TabFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setNoScroll(true);
        for (int i = 0; i < 4; i++) {
            tabGroup.addTab(
                    new Tab(getContext())
                            .setTabImage(R.mipmap.ic_launcher_round)
                            .setTabText(adapter.getTitle(i))
            );
        }

        tabGroup.setOnTabSelectListener(new TabGroup.OnTabSelectListener() {
            @Override
            public void onTabSelected(View tab, int position) {
                setTitle(adapter.getTitle(position));
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
