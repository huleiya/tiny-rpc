package com.figgo.tinyrpc.config;

import com.figgo.tinyrpc.fault.retry.RetryStrategyKeys;
import com.figgo.tinyrpc.loadbalancer.LoadBalancerKeys;
import com.figgo.tinyrpc.serializer.SerializerKeys;
import lombok.Data;
import lombok.ToString;

/**
 * RPC 框架配置
 */
@ToString
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "tiny-rpc";
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";
    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;
    /**
     * 是否启用模拟调用
     */
    private boolean mock = false;
    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JSON;
    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;
}
