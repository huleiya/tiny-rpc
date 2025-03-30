package com.figgo.tinyrpc.core;

import com.figgo.tinyrpc.loadbalancer.LoadBalancer;
import com.figgo.tinyrpc.loadbalancer.RoundRobinLoadBalancer;
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
public class LoadBalancerTest {
    final LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    @Test
    public void select() {
        // 请求参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", "apple");
        // 服务列表
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(1234);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("yupi.icu");
        serviceMetaInfo2.setServicePort(80);
        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2);
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(selectedServiceMetaInfo);
        Assertions.assertNotNull(selectedServiceMetaInfo);

        selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(selectedServiceMetaInfo);
        Assertions.assertNotNull(selectedServiceMetaInfo);

        selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(selectedServiceMetaInfo);
        Assertions.assertNotNull(selectedServiceMetaInfo);
    }
}
