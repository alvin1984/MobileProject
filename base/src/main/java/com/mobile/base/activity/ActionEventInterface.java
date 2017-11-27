package com.mobile.base.activity;

import android.content.Intent;
import android.os.Bundle;

/**
 * BaseFragment的启动，关闭等封装方法的响应回调，以便通知他的宿主BaseActivity能够处理这些事件
 *
 */
public interface ActionEventInterface {
    void onEvent(ActionMode mode, BaseFragment currentFragment, Class<? extends BaseFragment> clazz, Bundle args, int
            requestCode, int resultCode,
                 Intent data);
}
