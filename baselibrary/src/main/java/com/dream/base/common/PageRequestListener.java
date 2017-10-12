package com.dream.base.common;

import java.util.List;

/**
 * Created by wang on 2017/4/21.
 */

public interface PageRequestListener<T> {

    public static int FAILURE_TYPE_NET = 1;

    public static int FAILURE_TYPE_DATA = 2;

    /**
     * @param currentPage 当前页码
     * @param isLoadMore  true 加载更多  false 下拉刷新
     * @param result      刷新的数据
     */
    public void onSuccess(int currentPage, boolean isLoadMore, List<T> result);

    /**
     * @param currentPage 当前页码
     * @param errorMsg    错误提示
     */
    public void onFailure(int currentPage, String errorMsg);

}
