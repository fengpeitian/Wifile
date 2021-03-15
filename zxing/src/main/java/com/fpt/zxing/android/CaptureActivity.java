package com.fpt.zxing.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.fpt.zxing.camera.CameraManager;
import com.fpt.zxing.common.Constant;
import com.fpt.zxing.common.ZxingSetting;
import com.fpt.zxing.decode.DecodeImgCallback;
import com.fpt.zxing.decode.DecodeImgThread;
import com.fpt.zxing.decode.ImageUtil;
import com.fpt.zxing.view.ViewfinderView;
import com.google.zxing.Result;

/**
 * <pre>
 *   @author  : fpt
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/06 15:45
 *   desc    : 扫一扫base
 * </pre>
 */
public abstract class CaptureActivity extends AppCompatActivity {
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }
        setContentView(bindLayout());

        initView();

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(ZxingSetting.isPlayBeep);
        beepManager.setVibrate(ZxingSetting.isShake);
    }

    protected abstract void initView();

    protected abstract int bindLayout();
    
    public void handleDecode(Result rawResult) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        if (rawResult != null) {
            String result = rawResult.getText();
            onResult(result);
        }else {
            onResult("");
        }
    }

    protected abstract void onResult(String s);

    public void switchFlashImg(int flashState) {}

    @Override
    protected void onResume() {
        super.onResume();
        beepManager.updatePrefs();
        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        inactivityTimer.onPause();
        beepManager.close();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE && resultCode == RESULT_OK) {
            String path = ImageUtil.getImageAbsolutePath(this, data.getData());

            new DecodeImgThread(path, new DecodeImgCallback() {
                @Override
                public void onImageDecodeSuccess(Result result) {
                    handleDecode(result);
                }

                @Override
                public void onImageDecodeFailed() {
                    handleDecode(null);
                }
            }).run();
        }
    }

    public abstract ViewfinderView getViewfinderView();

    public abstract CameraManager getCameraManager();

    public abstract Handler getHandler();
}
