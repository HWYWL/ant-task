package com.github.hwywl.ant.task.utils;

/**
 * 接口返回值分装类
 *
 * @author HWY
 * 2021年9月16日13:52:43
 */
public class MessageResult {
    private Integer status;
    private String msg;
    private Object obj;

    public static MessageResult ok(String msg, Object obj) {
        return new MessageResult(200, msg, obj);
    }


    public static MessageResult ok(String msg) {
        return new MessageResult(200, msg, null);
    }


    public static MessageResult error(String msg, Object obj) {
        return new MessageResult(500, msg, obj);
    }


    public static MessageResult error(String msg) {
        return new MessageResult(500, msg, null);
    }

    private MessageResult() {
    }

    private MessageResult(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
