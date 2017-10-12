package com.dream.base.common;

import java.util.List;

/**
 * Description: TODO
 * author: WangKunHui
 * date: 16/11/4 下午5:21
 * <p>
 * Copyright©2016 by wang. All rights reserved.
 */
public class ResponseResult<T> {

    /**
     * 状态码
     */
    private int code = -1;

    /**
     * 失败原因
     */
    private String msg;

    /**
     * 数据体
     */
    private T result;

    private List<T> results;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
