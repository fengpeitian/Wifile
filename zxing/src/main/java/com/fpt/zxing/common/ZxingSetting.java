package com.fpt.zxing.common;

import android.support.annotation.ColorRes;

import com.fpt.zxing.R;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2019/01/28 13:35
 *   desc    : 设置
 * </pre>
 */
public class ZxingSetting {
    public static boolean isShake = true;
    public static boolean isPlayBeep = true;
    /**
     * 四个角的颜色
     */
    @ColorRes
    public static int reactColor = R.color.react;
    /**
     * 扫描框颜色
     */
    @ColorRes
    public static int frameLineColor = R.color.react;
    /**
     * 扫描线颜色
     */
    @ColorRes
    public static int scanLineColor = R.color.scanLineColor;

}
