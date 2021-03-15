package com.fpt.wifile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fpt.wifile.Permission;
import com.fpt.wifile.R;
import com.fpt.wifile.event.ServerEvent;
import com.fpt.wifile.server.AndroidServer;
import com.fpt.wifile.utils.Constants;
import com.fpt.wifile.utils.FileUtils;
import com.fpt.zxing.encode.CodeCreator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/06 15:45
 *   desc    : 外部分享页面
 * </pre>
 */
public class ShareActivity extends Activity {
    private ImageView iv_qr;
    private Button bt_finish;
    private ProgressBar pb_loading;
    private String mPath;
    private boolean isOpen = false;

    private Permission.Builder mPermissionBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share);
        iv_qr = findViewById(R.id.iv_qr);
        bt_finish = findViewById(R.id.bt_finish);
        pb_loading = findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);

        bt_finish.setText(R.string.close);
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //注册eventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent service = new Intent(this, AndroidServer.class);
        stopService(service);
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionBuilder.RequestPermissionsResult(requestCode,permissions,grantResults);
    }

    /**
     * 申请权限
     */
    private void setPermission() {
        mPermissionBuilder = new Permission.Builder();
        mPermissionBuilder.activity(this).requestPermissionFeature(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new Permission.PermissionListener() {
                    @Override
                    public void granted() {
                        LogUtils.d("申请权限成功");
                        initIntentData();
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

    private void initIntentData(){
        Intent intent = getIntent();
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        // 判断Intent是否是“分享”功能
        if (Intent.ACTION_SEND.equals(action)){
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                // 获取资源路径Uri
                Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
                String local_path = FileUtils.getPathFromUri(this,uri);
                mPath = "/file?path="+local_path;

                Intent server = new Intent(this,AndroidServer.class);
                server.putExtra(Constants.SERVER,true);
                startService(server);
            }else if (extras.containsKey(Intent.EXTRA_TEXT)){
                //分享的是文本字符串
                final String data = extras.getString(Intent.EXTRA_TEXT);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQrCode(iv_qr,data);
                    }
                },32);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ServerEvent event) {
        switch (event.getStatus()){
            case Constants.STARTED:
                isOpen = true;
                LogUtils.d("start: "+event.getParameter());
                break;
            case Constants.STOPPED:
                isOpen = false;
                LogUtils.d("stop");
                break;
            case Constants.ERROR:
                isOpen = false;
                LogUtils.d("error: "+event.getParameter());
                break;
            case Constants.RUNNING:
                if (event.isRunning() && !TextUtils.isEmpty(mPath) && isOpen){
                    String host = SPUtils.getInstance(Constants.CACHE).getString(Constants.HOST);
                    if (!TextUtils.isEmpty(host)){
                        String url = host + mPath;

                        LogUtils.d(url);
                        showQrCode(iv_qr,url);
                        mPath = null;
                    }
                }
                break;
            default:
        }
    }

    /**
     * 展示二维码
     * @param ivQrCode
     * @param text
     */
    private void showQrCode(ImageView ivQrCode,String text) {
        if (!TextUtils.isEmpty(text)) {
            pb_loading.setVisibility(View.GONE);
            Bitmap bitmap = CodeCreator.createQRCode(text, ivQrCode.getWidth(), ivQrCode.getHeight(), null);
            if (bitmap != null) {
                ivQrCode.setImageBitmap(bitmap);
            }
        }
    }

}
