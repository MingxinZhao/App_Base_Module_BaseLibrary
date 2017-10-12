package com.dream.base.common;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wang on 2017/4/21.
 */

public abstract class PageRequest<T> {

    protected PageRequestListener<T> requestListener;

    /**
     * 从第一页开始加载
     */
    protected int currentPage = 0;

    public PageRequest() {
        initPage();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setRequestListener(PageRequestListener<T> requestListener) {
        this.requestListener = requestListener;
    }

    public abstract void request(boolean isLoadMore);

    protected void increasePage() {
        currentPage++;
    }

    protected void decreasePage() {
        currentPage--;
        if (currentPage < 0) {
            currentPage = 0;
        }
    }

    protected void returnError(String errorMessage) {
        if (requestListener != null) {
            requestListener.onFailure(currentPage, errorMessage);
        }
    }

    protected void returnData(boolean isLoadMore, List<T> data) {
        if (requestListener != null) {
            requestListener.onSuccess(currentPage, isLoadMore, data);
        }
    }

    protected void returnData(boolean isLoadMore, T data) {
        if (requestListener != null) {
            requestListener.onSuccess(currentPage, isLoadMore, Arrays.asList(data));
        }
    }

    /**
     * 初始化page
     */
    protected abstract void initPage();
}
