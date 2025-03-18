package com.figgo.tinyrpc.registry;

import com.figgo.tinyrpc.spi.SpiLoader;

public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = SpiLoader.getInstance(Registry.class, RegistryKeys.ETCD);

    /**
     * 获取实例
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
