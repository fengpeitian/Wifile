package com.fpt.wifile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fpt.wifile.Permission;
import com.fpt.wifile.R;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/06 15:45
 *   desc    : 闪屏页面
 * </pre>
 */
public class SplashActivity extends Activity {
    private Permission.Builder mPermissionBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setPermission();
    }

    /**
     * 申请权限
     */
    private void setPermission() {
        mPermissionBuilder = new Permission.Builder();
        mPermissionBuilder.activity(this).requestPermissionFeature(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                new Permission.PermissionListener() {
                    @Override
                    public void granted() {
                        LogUtils.d("申请权限成功");
                        skip();
                    }

                    @Override
                    public void denied() {
                        ToastUtils.showShort("需要权限,请同意");
                        setPermission();
                    }

                    @Override
                    public void deniedNeverAsk() {
                        ToastUtils.showShort("我们的应用需要权限");
                        onBackPressed();
                    }
                });
    }

    private void skip() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,ScanActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionBuilder.RequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}
