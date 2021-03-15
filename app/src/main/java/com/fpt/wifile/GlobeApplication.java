package com.fpt.wifile;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/29 15:49
 *   desc    : 全局Application
 * </pre>
 */
public class GlobeApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        Utils.init(this);
    }

    public static Context getContext() {
        return mContext;
    }

}
