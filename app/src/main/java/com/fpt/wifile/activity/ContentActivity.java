package com.fpt.wifile.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.fpt.wifile.R;
import com.fpt.wifile.utils.Constants;
import com.fpt.wifile.widget.Browser;
import com.fpt.wifile.widget.BrowserView;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/06 15:45
 *   desc    : 内容查看页面
 * </pre>
 */
public class ContentActivity extends AppCompatActivity {

    private ImageView iv_back;

    private Browser browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        browser = findViewById(R.id.browser);

        BrowserView mBrowserView = browser.getBrowserView();
        mBrowserView.setOnWebViewClientStatusListener(new BrowserView.OnWebViewClientStatusListener() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                LogUtils.d("onReceivedTitle");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.d("onPageStarted");
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtils.d("onProgressChanged: "+newProgress);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.d("onPageFinished");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                LogUtils.d("onReceivedError: "+error.toString());
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                LogUtils.d("onReceivedHttpError: "+errorResponse.toString());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                LogUtils.d("onReceivedSslError: "+error.toString());
            }

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogUtils.d("onDownloadStart");
            }
        });

        String url = getIntent().getStringExtra(Constants.URL);
        mBrowserView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        browser.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        browser.onPause();
    }

    @Override
    protected void onDestroy() {
        browser.onDestroy();
        super.onDestroy();
    }

}
