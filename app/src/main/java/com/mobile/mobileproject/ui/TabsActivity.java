package com.mobile.mobileproject.ui;

import android.os.Bundle;

import com.mobile.base.activity.BaseActivity;
import com.mobile.base.activity.BaseFragment;
import com.mobile.base.tab_container.TabsFragment;
import com.mobile.mobileproject.R;
import com.mobile.mobileproject.Test2Fragment;
import com.mobile.mobileproject.TestFragment;
import com.mobile.mobileproject.ui.home.HomeFragment;

import java.util.ArrayList;


/**
 * Created by alvinzhang on 2017/8/4.
 */

public class TabsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        TabsFragment tabsFragment = new TabsFragment();
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new TestFragment());
        fragments.add(new Test2Fragment());
        fragments.add(new TestFragment());

        int[] pics = new int[]{
                R.mipmap.ic_launcher_round,
                R.mipmap.ic_launcher_round,
                R.mipmap.ic_launcher_round,
                R.mipmap.ic_launcher_round
        };

        String[] titles = new String[]{
                "首页",
                "发现",
                "心动",
                "我的"
        };

        tabsFragment.setTabs(fragments,pics,titles);
        setContentView(tabsFragment);

    }
}
