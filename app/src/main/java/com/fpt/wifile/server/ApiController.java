package com.fpt.wifile.server;

import com.blankj.utilcode.util.LogUtils;
import com.fpt.wifile.utils.FileUtils;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.framework.body.StreamBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/21 17:21
 *   desc    : 接口文档 controller
 * </pre>
 */

@RestController
class ApiController {

    @GetMapping(path = "/file")
    public void file(HttpRequest request, HttpResponse response) {
        String local_path = request.getParameter("path");
        File file = new File(local_path);
        String type = FileUtils.getMimeType(file);
        MediaType mediaType = MediaType.parseMediaType(type);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LogUtils.e(e.getMessage());
        }

        ResponseBody responseBody = new StreamBody(fileInputStream,file.length(), mediaType);
        response.setBody(responseBody);
    }

}