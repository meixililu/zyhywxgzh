package com.xbkj.zyhy.wx.bean;

public class TranResultRoot {

    private int code;
    private String error;
    private TranDictResult result;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }

    public void setResult(TranDictResult result) {
        this.result = result;
    }
    public TranDictResult getResult() {
        return result;
    }
}