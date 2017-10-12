package com.dream.base.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 *
 * @ClassName: ThreadPool
 * @Description: TODO
 * @author: wang
 * @date: 2016-3-3 下午4:27:32
 */
public class ThreadPool {

    /**
     * 核心线程数（最小线程数）
     */
    private static int defaultCorePoolSize = 2;

    /**
     * 最大线程数
     */
    private static int defaultMaxPoolSize = 5;

    /**
     * 线程池维护线程所允许的空闲时间，单位S
     */
    private static int defaultKeepAliveTime = 2;

    /**
     * 默认线程池等待队列长度
     */
    private static int defaultCacheListSize = 4;

    private static ThreadPool instance;

    /**
     * 线程池
     */
    private ThreadPoolExecutor mThreadPoolExecutor;

    /**
     * 缓冲线程队列
     */
    private ArrayBlockingQueue<Runnable> mCacheThreadList;

    private ThreadPool() {

        this(defaultCorePoolSize, defaultMaxPoolSize, defaultKeepAliveTime,
                defaultCacheListSize,
                new ThreadPoolExecutor.DiscardOldestPolicy());

    }

    /**
     * @param corePoolSize    核心线程数
     * @param maxPoolSize     最大线程数
     * @param keepAliveTime   线程空闲活跃时间 单位s
     * @param cacheThreadSize 缓冲队列大小
     * @param handler         线程池满的时候处理方式
     */
    private ThreadPool(int corePoolSize, int maxPoolSize, int keepAliveTime,
                       int cacheThreadSize, RejectedExecutionHandler handler) {

        mCacheThreadList = new ArrayBlockingQueue<Runnable>(cacheThreadSize);
        mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.SECONDS, mCacheThreadList, handler);

    }

    public static ThreadPool getDefaultInstance() {

        if (instance == null) {
            instance = new ThreadPool();
        }
        return instance;
    }

    public static ThreadPool getInstance(int corePoolSize, int maxPoolSize,
                                         int keepAliveTime, int cacheThreadSize,
                                         RejectedExecutionHandler handler) {
        if (instance == null) {
            instance = new ThreadPool();
        }
        return instance;
    }

    /**
     * 添加线程
     *
     * @param runnable
     */
    public void addThread(Runnable runnable) {

        mThreadPoolExecutor.execute(runnable);

    }

    /**
     * 获取线程池大小
     */
    public int getCurrentThreadNum() {
        return mThreadPoolExecutor.getPoolSize();
    }

}
