package cn.atool.distributor.retry.service;

import cn.atool.distributor.retry.model.RetryBody;

import java.util.List;
import java.util.function.Consumer;

/**
 * 外部重试实现
 *
 * @author darui.wu
 * @create 2020/1/10 3:30 下午
 */
public interface RetryHandler {
    /**
     * 执行单个重试任务
     *
     * @param category     重试事件类型
     * @param retryKey     重试事件键值
     * @param retryHandler 执行重试函数，Object[]是具体重试参数
     */
    default void doRetry(String category, String retryKey, Consumer<Object[]> retryHandler) {
        RetryBody body = this.getRetryPersistence().findRetry(category, retryKey);
        this.doRetry(body, retryHandler);
    }

    /**
     * 批量执行重试任务, 一次执行100条事件
     *
     * @param category     重试事件类型
     * @param startId      重试事件起始id
     * @param retryHandler 执行重试函数，Object[]是具体重试参数
     * @return 返回100条重试事件的最大id + 1, 如果一批不足100条，返回0
     */
    default long doRetry(String category, long startId, Consumer<Object[]> retryHandler) {
        int maxSize = 100;
        List<RetryBody> list = this.getRetryPersistence().findRetry(category, startId, maxSize);
        long maxId = startId;
        for (RetryBody body : list) {
            maxId = Math.max(maxId, body.getId());
            this.doRetry(body, retryHandler);
        }
        return list.size() < maxSize ? 0L : maxId + 1;
    }

    /**
     * 单个执行重试任务
     *
     * @param body         重试消息体
     * @param retryHandler 执行重试函数，Object[]是具体重试参数
     */
    void doRetry(RetryBody body, Consumer<Object[]> retryHandler);

    /**
     * 返回持久化处理器
     *
     * @return
     */
    RetryPersistence getRetryPersistence();
}
