package com.figgo.tinyrpc.fault.tolerant;

import com.figgo.tinyrpc.fault.retry.RetryStrategy;
import com.figgo.tinyrpc.fault.retry.RetryStrategyKeys;
import com.figgo.tinyrpc.spi.SpiLoader;

/**
 * 容错策略工厂（用于获取容错策略对象）
 */
public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     */
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = SpiLoader.getInstance(TolerantStrategy.class, TolerantStrategyKeys.FAIL_FAST);

    /**
     * 根据键名获取容错策略对象实例
     */
    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
