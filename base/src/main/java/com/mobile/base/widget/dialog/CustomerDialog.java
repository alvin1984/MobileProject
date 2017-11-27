package com.mobile.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * 通用弹窗
 * Created by alvinzhang on 2017/7/21.
 */

public class CustomerDialog {

    private BaseDialog baseDialog;

    public CustomerDialog(Context context) {
        baseDialog = new BaseDialog(context);
    }

    /**
     * 设置弹窗的显示时间
     * @param seconds 显示时间
     * @return 当前窗口对象
     */
    public CustomerDialog setLifeTime(int seconds){
        baseDialog.setLifeTime(seconds);
        return this;
    }

    /**
     * 设置弹窗出现动画
     * @param style 动画样式
     * @return 当前窗口对象
     */
    public CustomerDialog setWindowAnimation(int style){
        baseDialog.setWindowAnimation(style);
        return this;
    }

    /**
     * 是否显示关闭按钮
     * @param isShow 是否显示
     * @return 当前窗口对象
     */
    public CustomerDialog showCloseBtn(boolean isShow){
        baseDialog.showCloseBtn(isShow);
        return this;
    }

    /**
     * 设置关闭按钮的点击事件
     * @param listener 点击事件监听器
     * @return 当前窗口对象
     */
    public CustomerDialog setCloseBtnClickListener(View.OnClickListener listener){
        baseDialog.setCloseBtnClickListener(listener);
        return this;
    }

    /**
     * 窗口大小与屏幕保持一致，高度自适应
     * @return 当前窗口对象
     */
    public CustomerDialog setMatchParent() {
        baseDialog.setMatchParent();
        return this;
    }

    public CustomerDialog setWindowBackground(int color){
        baseDialog.setWindowBackground(color);
        return this;
    }

    public CustomerDialog setDialogSize(float widthDP,float heightDP){
        baseDialog.setDialogSize(widthDP,heightDP);
        return this;
    }
    public CustomerDialog setDialogWidthSize(float widthDP){
        baseDialog.setDialogWidthSize(widthDP);
        return this;
    }

    public CustomerDialog setGravity(int gravity) {
        baseDialog.setGravity(gravity);
        return this;
    }

    public CustomerDialog setIsCancelable(boolean isCancelable) {
        baseDialog.setIsCancelable(isCancelable);
        return this;
    }

    public CustomerDialog setCanCancelOutside(boolean isCan){
        baseDialog.setCanCancelOutside(isCan);
        return this;
    }

    /**
     * 设置窗口内容区的显示内容
     * @param layoutId 内容区的显示布局
     * @return 当前窗口对象
     */
    public CustomerDialog setCustomerContent(int layoutId) {
        baseDialog.setCustomerContent(layoutId);
        return this;
    }

    public CustomerDialog setBackgroundResource(int viewId, int resId) {
        baseDialog.setBackgroundResource(viewId,resId);
        return this;
    }

    /**
     * 设置指定view的显示文字内容
     * @param viewId 需要显示文字内容的view id
     * @param textId 文字的引用
     * @return 当前窗口对象
     */
    public CustomerDialog setText(int viewId, int textId) {
        baseDialog.setText(viewId,textId);
        return this;
    }

    /**
     * 设置指定view的显示文字内容
     * @param viewId 需要显示文字内容的view id
     * @param text 文字
     * @return 当前窗口对象
     */
    public CustomerDialog setText(int viewId, String text) {
        baseDialog.setText(viewId,text);
        return this;
    }

    /**
     * 设置指定view的显示文字颜色
     * @param viewId 需要显示文字内容的view id
     * @param colorId 文字颜色
     * @return 当前窗口对象
     */
    public CustomerDialog setTextColor(int viewId, int colorId) {
        baseDialog.setTextColor(viewId,colorId);
        return this;
    }

    /**
     * 设置指定view的显示图案
     * @param viewId 需要显示图案的view id
     * @param resId 图案的资源索引
     * @return 当前窗口对象
     */
    public CustomerDialog setImageResource(int viewId, int resId) {
        baseDialog.setImageResource(viewId,resId);
        return this;
    }

    /**
     * 设置指定view的设置点击事件监听器
     * @param viewId 需要显示图案的view id
     * @param listener 点击事件监听器
     * @return 当前窗口对象
     */
    public CustomerDialog setViewOnClickListener(int viewId, final Dialog.OnClickListener listener) {
        baseDialog.setViewOnClickListener(viewId,listener);
        return this;
    }

    public CustomerDialog show() {
        baseDialog.show();
        return this;
    }

    /**
     * 为setBtnPanelView方法设置按钮布局添加点击事件监听器
     * @param listener 点击监听器
     * @return 当前窗口对象
     */
    public CustomerDialog setListener(DialogInterface.OnClickListener listener){
        baseDialog.setListener(listener);
        return this;
    }

    /**
     * 设置按钮布局：如确定与取消按钮布局等
     * @param layoutId 布局资源索引
     * @return 当前窗口对象
     */
    public CustomerDialog setBtnPanelView(int layoutId){
        baseDialog.setBtnPanelView(layoutId);
        return this;
    }

    /**
     * 设置按钮布局并添加点击事件监听器
     * @param layoutId 布局资源索引
     * @param listener 点击监听器
     * @return 当前窗口对象
     */
    public CustomerDialog setBtnPanelView(int layoutId, Dialog.OnClickListener listener) {
        baseDialog.setBtnPanelView(layoutId,listener);
        return this;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        baseDialog.onDismiss(dialogInterface);
    }


}
