package cn.atool.distributor.retry.service;

import cn.atool.distributor.retry.exception.RetryException;
import cn.atool.distributor.retry.model.RetryBody;

import java.util.List;

/**
 * 异步重试持久化方法
 *
 * @author darui.wu
 * @create 2020/1/8 4:23 下午
 */
public interface RetryPersistence {
    /**
     * 查找需要重试的消息列表
     *
     * @param retryCategory 重试事件类型
     * @param begin         起始记录
     * @return 最多返回100条状态为status的重试事件
     * @throws RetryException
     */
    List<RetryBody> findRetry(String retryCategory, long begin) throws RetryException;

    /**
     * 查找指定的重试事件
     *
     * @param retryCategory
     * @param retryKey
     * @return
     * @throws RetryException
     */
    RetryBody findRetry(String retryCategory, String retryKey) throws RetryException;

    /**
     * 统计
     *
     * @param retryStatus 重试状态
     * @return
     */
    List<RetryBody> summaryRetry(String retryStatus);

    /**
     * 记录重试消息
     *
     * @param body     重试消息体
     * @param maxRetry 最大重试次数
     * @param e        异常
     * @throws RetryException
     */
    void save(RetryBody body, int maxRetry, Throwable e) throws RetryException;

    /**
     * 关闭重试消息
     *
     * @param body
     */
    void closeRetry(RetryBody body);
}
