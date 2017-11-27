package com.mobile.base.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mobile.base.activity.BaseActivity;
import com.mobile.base.util.ImageUtils;
import com.mobile.base.util.ToastUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by pxy on 2017/11/1.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    public static final String WX_SHARE_PAGE = "we_chat_share_page";
    public static final String WX_SHARE_TEXT = "we_chat_share_text";
    public static final String WX_SHARE_PICTURE = "we_chat_share_picture";
    public static String WE_CHAT_APP_ID = "wxd81dc79ff25577d0";

    private IWXAPI api;

    private Tencent mTencent;

    private String QQ_APP_ID = "1106440881";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this,WE_CHAT_APP_ID,true);
        api.registerApp(WE_CHAT_APP_ID);
        api.handleIntent(getIntent(),this);

        mTencent = Tencent.createInstance(QQ_APP_ID, getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }

    private boolean judgeCanGo(){
        if (!api.isWXAppInstalled()){
            ToastUtils.toast(this,"请先安装微信应用");
            return false;
        }else if (!api.isWXAppSupportAPI()){
            ToastUtils.toast(this,"请先更新微信应用");
            return false;
        }
        return true;
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {

        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK://分享成功
                if (baseResp.transaction.equals(WX_SHARE_PAGE)){

                }else if (baseResp.transaction.equals(WX_SHARE_PICTURE)){

                }else if (baseResp.transaction.equals(WX_SHARE_TEXT)){

                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://取消分享

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://拒绝分享

                break;

        }
        ToastUtils.toast(this,baseResp.errStr);
        finish();
    }


    /**
     *
     * @param title 标题
     * @param des 描述
     * @param url 网页地址
     * @param bitmap 缩略图
     * @param scene SendMessageToWX.Req.WXSceneSession:发送到聊天界面，
     *              SendMessageToWX.Req.WXSceneTimeline：分享到朋友圈,
     *              SendMessageToWX.Req.WXSceneFavorite:添加到微信收藏
     */
    public void sharePageToWeChat(String title, String des, String url, Bitmap bitmap, int scene){
        if (!judgeCanGo()){
            return;
        }
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = des;
        msg.thumbData = ImageUtils.bmp2byteArr(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WX_SHARE_PAGE;
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    /**
     * 分享文本到微信
     * @param text 文本内容
     * @param scene SendMessageToWX.Req.WXSceneSession:发送到聊天界面，
     *              SendMessageToWX.Req.WXSceneTimeline：分享到朋友圈,
     *              SendMessageToWX.Req.WXSceneFavorite:添加到微信收藏
     */
    private void shareTextToWeChat(String text, int scene){
        if (!judgeCanGo()){
            return;
        }

        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WX_SHARE_TEXT;
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    /**
     * 分享图片到微信
     * @param bitmap 分享的图片
     * @param scene SendMessageToWX.Req.WXSceneSession:发送到聊天界面，
     *              SendMessageToWX.Req.WXSceneTimeline：分享到朋友圈,
     *              SendMessageToWX.Req.WXSceneFavorite:添加到微信收藏
     */
    private void sharePictureWeChat(Bitmap bitmap, int scene){
        if (!judgeCanGo()){
            return;
        }

        WXImageObject imgObject = new WXImageObject();
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObject;


        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,50,50,true);
        bitmap.recycle();
        msg.thumbData = ImageUtils.bmp2byteArr(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WX_SHARE_PICTURE;
        req.message = msg;
        req.scene = scene;

        api.sendReq(req);


    }

    /**
     *
     * @param title 要分享的标题
     * @param summary 要分享的摘要
     * @param url 点击打开后的网页
     * @param thumbPicUrl 消息内的缩略图
     * @param appName 分享些消息的应用名称
     */
    public void sharePageToQQ(String title, String summary, String url, String thumbPicUrl, String appName){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,thumbPicUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  appName);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  0);
        mTencent.shareToQQ(this, params, new BaseUiListener());
    }

    /**
     *
     * @param activity 当前活动窗口
     * @param title 要分享的标题
     * @param summary 要分享的摘要
     * @param url 点击打开后的网页
     * @param thumbPicUrls 消息内的缩略图
     */
    public void sharePageToQzone (Activity activity, String title, String summary, String url, ArrayList<String> thumbPicUrls) {
        final Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, thumbPicUrls);
        mTencent.shareToQzone(activity, params, new BaseUiListener());
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            ToastUtils.toast(WXEntryActivity.this,"分享成功");
        }

        @Override
        public void onError(UiError e) {
            ToastUtils.toast(WXEntryActivity.this,"分享失败");
        }

        @Override
        public void onCancel() {
            ToastUtils.toast(WXEntryActivity.this,"分享取消");
        }
    }
}
