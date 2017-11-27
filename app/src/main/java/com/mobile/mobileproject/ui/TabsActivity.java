package com.mobile.mobileproject.ui;

import android.os.Bundle;

import com.mobile.base.activity.BaseActivity;
import com.mobile.mobileproject.ui.container.TabsFragment;


/**
 * Created by alvinzhang on 2017/8/4.
 */

public class TabsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitleBar(false);
        setContentView(new TabsFragment());

    }
}
