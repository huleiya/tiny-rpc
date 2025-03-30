package com.figgo.tinyrpc.loadbalancer;

import com.figgo.tinyrpc.spi.SpiLoader;

/**
 * 一致性哈希负载均衡器
 */
public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    public static final LoadBalancer DEFAULT_LOAD_BALANCER = SpiLoader.getInstance(LoadBalancer.class, LoadBalancerKeys.ROUND_ROBIN);

    /**
     * 获取实例
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
