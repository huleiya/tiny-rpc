package com.figgo.tinyrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.config.RegistryConfig;
import com.figgo.tinyrpc.config.RpcConfig;
import com.figgo.tinyrpc.constant.RpcConstant;
import com.figgo.tinyrpc.loadbalancer.LoadBalancer;
import com.figgo.tinyrpc.loadbalancer.LoadBalancerFactory;
import com.figgo.tinyrpc.model.RpcRequest;
import com.figgo.tinyrpc.model.RpcResponse;
import com.figgo.tinyrpc.model.ServiceMetaInfo;
import com.figgo.tinyrpc.registry.Registry;
import com.figgo.tinyrpc.registry.RegistryFactory;
import com.figgo.tinyrpc.serializer.Serializer;
import com.figgo.tinyrpc.serializer.SerializerFactory;
import com.figgo.tinyrpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理（JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        RpcConfig rpcConfig = RpcApplication.getConfig();
        final Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 从注册中心获取服务提供者请求地址
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("No service provider found for " + serviceName);
            }

            // 负载均衡选择服务提供者
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            System.out.println(selectedServiceMetaInfo);

            // 发送 TCP 请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(selectedServiceMetaInfo, rpcRequest);
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
