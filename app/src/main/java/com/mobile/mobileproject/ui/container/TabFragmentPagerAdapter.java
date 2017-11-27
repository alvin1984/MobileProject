package com.mobile.mobileproject.ui.container;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.mobileproject.R;
import com.mobile.mobileproject.Test2Fragment;
import com.mobile.mobileproject.TestFragment;
import com.mobile.mobileproject.ui.home.HomeFragment;

/**
 * Created by alvinzhang on 2017/8/4.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"首页", "发现", "进货单","我的"};
    private int[] mPics = {R.mipmap.ic_launcher_round,R.mipmap.plugin_file_ic_file_gray_116dp,
    R.mipmap.ic_launcher_round,R.mipmap.plugin_file_ic_file_gray_116dp};

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new Test2Fragment();
        } else if (position == 2) {
            return new TestFragment();
        }else if (position==3){
            return new Test2Fragment();
        }
        return new HomeFragment();
    }

    public String getTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

}
