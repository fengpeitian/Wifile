package com.fpt.wifile.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.blankj.utilcode.util.SPUtils;
import com.fpt.wifile.event.ServerEvent;
import com.fpt.wifile.utils.Constants;
import com.fpt.wifile.utils.IpUtils;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/21 17:21
 *   desc    : android 服务器
 * </pre>
 */
public class AndroidServer extends Service {
    private Server mServer;

    private boolean isListen = true;
    private static long interval = 1000;
    private static final int INTERVAL = 999;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == INTERVAL){
                EventBus.getDefault().post(new ServerEvent(Constants.RUNNING,isRunning()));
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        //开启服务
        mServer = AndServer.serverBuilder()
                .inetAddress(IpUtils.getLocalIPAddress())
                .port(Constants.PORT)
                .timeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .listener(new Server.ServerListener() {

                    @Override
                    public void onStarted() {
                        if (mServer != null) {
                            String hostAddress = mServer.getInetAddress().getHostAddress();
                            String host = "http://"+hostAddress+":"+Constants.PORT;
                            SPUtils.getInstance(Constants.CACHE).put(Constants.HOST,host);

                            EventBus.getDefault().post(new ServerEvent(Constants.STARTED, host));
                        }
                    }

                    @Override
                    public void onStopped() {
                        SPUtils.getInstance(Constants.CACHE).remove(Constants.HOST);

                        EventBus.getDefault().post(new ServerEvent(Constants.STOPPED));
                        //停止服务
                        stopSelf();
                    }

                    @Override
                    public void onException(Exception e) {
                        SPUtils.getInstance(Constants.CACHE).remove(Constants.HOST);

                        EventBus.getDefault().post(new ServerEvent(Constants.ERROR,e.getMessage()));
                        //停止服务
                        stopSelf();
                    }

                })
                .build();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        startListenServer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopServer();
        stopListenServer();
        super.onDestroy();
    }

    /**
     * 开启服务
     */
    private void startServer() {
        if (mServer != null && !mServer.isRunning()) {
            mServer.startup();
        }
    }

    /**
     * 关闭服务
     */
    private void stopServer() {
        if (mServer != null) {
            mServer.shutdown();
        }
    }

    /**
     * 监听服务状态
     */
    private void startListenServer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (isListen) {
                    try {
                        Thread.sleep(interval);
                        mHandler.sendEmptyMessage(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 停止监听服务状态
     */
    private void stopListenServer(){
        isListen = false;
    }

    /**
     * 判断服务是否在运行
     * @return
     */
    private boolean isRunning(){
        return mServer == null ? false:mServer.isRunning();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
