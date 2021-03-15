package com.fpt.zxing.decode;

import com.google.zxing.Result;

/**
 * <pre>
 *   @author  : fpt
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/06 15:45
 *   desc    : 解析图片的回调
 * </pre>
 */
public interface DecodeImgCallback {
    /**
     * 图片解析成功
     * @param result
     */
    void onImageDecodeSuccess(Result result);

    /**
     * 图片解析失败
     */
    void onImageDecodeFailed();
}
