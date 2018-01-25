package com.mobile.mobileproject.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobile.base.activity.BaseActivity;
import com.mobile.base.util.PermissionHelper;
import com.mobile.base.widget.dialog.LoadingDialog;
import com.mobile.imageloader.glide.AppGlide;
import com.mobile.mobileproject.R;
import com.mobile.mobileproject.ui.TabsActivity;

/**
 * 登陆界面
 * Created by alvinzhang on 2017/7/20.
 */

public class LoginActivity extends BaseActivity {

    private EditText userNameET;
    private EditText passwordET;
    private String loginUrl = "http://10.0.55.23:80/lms-service/user/login";
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showTitleBarLeftBtn(false);
        userNameET = (EditText) findViewById(R.id.et_user_name);
        passwordET = (EditText) findViewById(R.id.et_user_password);
        loadingDialog = new LoadingDialog(this, "加载中......");
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, TabsActivity.class);
                startActivity(intent);

//                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                        + "vanke11221122" + File.separator;
//
//                FileUtils.copyAssets(LoginActivity.this, "vanke11221122",path);
//
//                AppCan.getInstance().startCustomWidget(LoginActivity.this, null,path);

//                Intent intent = new Intent(LoginActivity.this,TabsActivity.class);
//                startActivity(intent);

//                String userName = userNameET.getText().toString();
//                String password = passwordET.getText().toString();
//                HashMap<String, String> params = new HashMap<>();
//                params.put("account", userName);
//                params.put("password", password);
//                JSONObject jsonObject = new JSONObject(params);

//                NetworkManager.doPost(loginUrl, jsonObject.toString(), new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            LoginResult loginResult = new Gson().fromJson(response.body().string(), LoginResult.class);
//                            if (loginResult.getState().getErrCode() == 10000) {
//                                new CustomerDialog(LoginActivity.this)
//                                        .setCustomerContent(R.layout.dialog_login_toast)
//                                        .setBtnPanelView(R.layout.dialog_login_btn_panel, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialogInterface.dismiss();
//                                                if (i == R.id.btn_login) {
//                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                    startActivity(intent);
//                                                    finish();
//                                                }
//                                            }
//                                        })
//                                        .showCloseBtn(true)
//                                        .show();
//                            } else {
//                                Toast.makeText(LoginActivity.this, loginResult.getState().getErrMsg(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });

//                NetworkManager.doPost(loginUrl, jsonObject.toString())
//                        .doOnSubscribe(new Consumer<Disposable>() {
//                            @Override
//                            public void accept(@NonNull Disposable disposable) throws Exception {
//                                if (!disposable.isDisposed()) {
//                                    loadingDialog.show();
//                                }
//                            }
//                        })
//                        .subscribe(new Consumer<Response>() {
//                            @Override
//                            public void accept(@NonNull Response response) throws Exception {
//                                loadingDialog.dismiss();
//                                if (response.isSuccessful()) {
//                                    LoginResult loginResult = new Gson().fromJson(response.body().string(), LoginResult.class);
//                                    if (loginResult.getState().getErrCode() == 10000) {
//                                        new CustomerDialog(LoginActivity.this)
//                                                .setCustomerContent(R.layout.dialog_login_toast)
//                                                .setBtnPanelView(R.layout.dialog_login_btn_panel, new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        dialogInterface.dismiss();
//                                                        if (i == R.id.btn_login) {
//                                                            Intent intent = new Intent(LoginActivity.this, TabsActivity.class);
//                                                            startActivity(intent);
//                                                            finish();
//                                                        }
//                                                    }
//                                                })
//                                                .showCloseBtn(true)
//                                                .show();
//                                    } else {
//                                        Toast.makeText(LoginActivity.this, loginResult.getState().getErrMsg(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(@NonNull Throwable throwable) throws Exception {
//                                loadingDialog.dismiss();
//                                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });


            }
        });


//        ImageView picIV = (ImageView) findViewById(R.id.iv_pic);
//        GlideApp.with(this)
//                .asGif()
//                .load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2259989867,1935385594&fm=27&gp=0.jpg")
//                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
//                .into(picIV);


//        NetworkManager.doGet("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg",false)
//                      .subscribeOn(Schedulers.io())
//                      .observeOn(AndroidSchedulers.mainThread())
//                      .subscribe(new Consumer<Response>() {
//                          @Override
//                          public void accept(Response response) throws Exception {
//                              ImageView picIV2 = (ImageView) findViewById(R.id.iv_pic2);
//                              Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
//                              RenderScriptGaussianBlur renderScriptGaussianBlur = new RenderScriptGaussianBlur(LoginActivity.this);
//                              picIV2.setImageBitmap(RenderScriptGaussianBlur.blurBitmap(LoginActivity.this,bitmap,25));
//                          }
//                      });

        final ImageView picIV2 = (ImageView) findViewById(R.id.iv_pic2);
//        GlideApp.with(this)
//                .load("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg")
//                .placeholder(R.mipmap.ic_launcher)
////                .blur(this)
////                .circleCrop()
////                .roundedCorners(RoundedCornersTransformation.CornerType.ALL)
//                .transforms(new BlurTransformation(this),new RoundedCornersTransformation(10,10))
////                .roundedCorners(RoundedCornersTransformation.CornerType.ALL)
////                .blur(this)
//                .into(picIV2);

//        AppGlide.with(this)
//                .load("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg")
//                .placeholder(R.mipmap.ic_launcher_round)
//                .blur(this)
//                .roundedCorners(20)
//                .circleCrop()
//                .into(picIV2);

        ImageView picIV = (ImageView) findViewById(R.id.iv_pic);
        AppGlide.with(this)
                .asBitmap()
                .load("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg")
//                .blur(this)
//                .circleCrop()
                .into(picIV2);
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition transition) {
//                        picIV2.setImageBitmap(resource);
//                    }
//                });

//
//        GlideApp.with(this)
//                .asGif()
////                .load("http://pic38.nipic.com/20140215/2844191_214643588144_2.jpg")
//                .load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2259989867,1935385594&fm=27&gp=0.jpg")
////                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
//                .circleCrop()
//                .blur(this)
//                .into(picIV);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionHelper.requestPermissions("", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {

            }

            @Override
            public void doAfterDenied(String... permission) {

            }
        }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_WIFI_STATE);
    }
}
