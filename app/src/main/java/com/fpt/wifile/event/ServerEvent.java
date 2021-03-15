package com.fpt.wifile.event;

/**
 * <pre>
 *   @author  : lucien.feng
 *   e-mail  : fengfei0205@gmail.com
 *   time    : 2018/12/28 17:07
 *   desc    : 服务状态的event
 * </pre>
 */
public class ServerEvent {
    private String parameter;
    private int status;
    private boolean isRunning;

    public ServerEvent(int status) {
        this.status = status;
    }

    public ServerEvent(int status, String parameter) {
        this.status = status;
        this.parameter = parameter;
    }

    public ServerEvent(int status, boolean isRunning) {
        this.status = status;
        this.isRunning = isRunning;
    }

    public String getParameter() {
        return parameter == null ? "" : parameter;
    }

    public int getStatus() {
        return status;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
