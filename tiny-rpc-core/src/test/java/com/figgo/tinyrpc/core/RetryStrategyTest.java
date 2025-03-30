package com.figgo.tinyrpc.core;

import com.figgo.tinyrpc.fault.retry.FixedIntervalRetryStrategy;
import com.figgo.tinyrpc.fault.retry.NoRetryStrategy;
import com.figgo.tinyrpc.fault.retry.RetryStrategy;
import com.figgo.tinyrpc.loadbalancer.LoadBalancer;
import com.figgo.tinyrpc.loadbalancer.RoundRobinLoadBalancer;
import com.figgo.tinyrpc.model.RpcResponse;
import com.figgo.tinyrpc.model.ServiceMetaInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡器测试
 */
public class RetryStrategyTest {
    final RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
