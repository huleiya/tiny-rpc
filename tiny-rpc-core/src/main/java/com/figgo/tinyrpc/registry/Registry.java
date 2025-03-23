package com.figgo.tinyrpc.registry;

import com.figgo.tinyrpc.config.RegistryConfig;
import com.figgo.tinyrpc.model.ServiceMetaInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Registry {
    /**
     * 初始化
     *
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);
    /**
     * 注册服务（服务端）
     *
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务（服务端）
     *
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现（获取某服务的所有节点）
     *
     * @param serviceKey 服务键名
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测（服务端）
     */
    void heartBeat();

    /**
     * 监听（消费端）
     */
    void watch(String serviceNodeKey);
}
