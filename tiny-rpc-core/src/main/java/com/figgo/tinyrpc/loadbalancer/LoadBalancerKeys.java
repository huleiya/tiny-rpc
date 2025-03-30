package com.figgo.tinyrpc.loadbalancer;

import com.figgo.tinyrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器键名常量
 */
public interface LoadBalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性哈希
     */
    String CONSISTENT_HASH = "consistentHash";
}
