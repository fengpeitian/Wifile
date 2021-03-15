package com.fpt.wifile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/28 09:27
 *   desc    : BrowserView包装
 * </pre>
 */
public class Browser extends RelativeLayout {
    private Context mContext;
    private BrowserView mBrowserView;

    public Browser(Context context) {
        super(context);
        initView(context);
    }

    public Browser(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Browser(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        mBrowserView = new BrowserView(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                ,LayoutParams.MATCH_PARENT);

        addView(mBrowserView,layoutParams);
    }

    public BrowserView getBrowserView() {
        return mBrowserView;
    }

    public void onPause(){
        if (mBrowserView != null){
            mBrowserView.pause();
        }
    }

    public void onResume(){
        if (mBrowserView != null){
            mBrowserView.resume();
        }
    }

    public void onDestroy(){
        if (mBrowserView != null){
            mBrowserView.destroy();
            mBrowserView = null;
        }
    }

}
