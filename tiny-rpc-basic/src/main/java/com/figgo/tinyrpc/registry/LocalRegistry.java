package com.figgo.tinyrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 */
public class LocalRegistry {
    /**
     * 注册信息存储
     */
    public static final Map<String, Class<?>> registryMap = new ConcurrentHashMap<>();
    /**
     * 注册服务
     */
    public static void register(String serviceName, Class<?> impClass) {
        registryMap.put(serviceName, impClass);
    }
    /**
     * 获取服务
     */
    public static Class<?> get(String serviceName) {
        return registryMap.get(serviceName);
    }
    /**
     * 移除服务
     */
    public static void remove(String serviceName) {
        registryMap.remove(serviceName);
    }
}
