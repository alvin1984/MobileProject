package com.mobile.mobileproject.ui.html;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.mobile.base.activity.BaseActivity;
import com.mobile.mobileproject.R;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * Created by pxy on 2017/11/24.
 */

public class FileReviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_review);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        TbsReaderView tbsReaderView = new TbsReaderView(this,null);
        container.addView(tbsReaderView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

//        TbsReaderView tbsReaderView = (TbsReaderView) findViewById(R.id.tbs_reader_view);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "OfficeTest" + File.separator + "日报.xlsx";
        boolean can = tbsReaderView.preOpen(getFileType(path),false);
        if (can){
            Bundle localBundle = new Bundle();
            localBundle.putString("filePath", path);
            localBundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
            tbsReaderView.openFile(localBundle);
        }
    }

    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }

        str = paramString.substring(i + 1);
        return str;
    }
}
