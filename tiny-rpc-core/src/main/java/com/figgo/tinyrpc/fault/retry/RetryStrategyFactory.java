package com.figgo.tinyrpc.fault.retry;

import com.figgo.tinyrpc.spi.SpiLoader;

/**
 * 重试策略工厂（用于获取重试策略对象）
 */
public class RetryStrategyFactory {
    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试策略
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = SpiLoader.getInstance(RetryStrategy.class, RetryStrategyKeys.FIXED_INTERVAL);

    /**
     * 根据键名获取重试策略对象实例
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}
