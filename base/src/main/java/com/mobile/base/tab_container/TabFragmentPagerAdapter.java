package com.mobile.base.tab_container;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.base.activity.BaseFragment;

import java.util.ArrayList;


/**
 * Created by alvinzhang on 2017/8/4.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mFragments;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTabs(ArrayList<BaseFragment> fragments){
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
//        if (position == 1) {
//            return new Test2Fragment();
//        } else if (position == 2) {
//            return new TestFragment();
//        }else if (position==3){
//            return new Test2Fragment();
//        }
//        return new HomeFragment();
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

}
