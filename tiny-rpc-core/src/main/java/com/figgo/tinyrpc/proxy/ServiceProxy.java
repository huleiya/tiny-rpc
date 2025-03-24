package com.figgo.tinyrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.config.RegistryConfig;
import com.figgo.tinyrpc.constant.RpcConstant;
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
import java.util.List;

/**
 * 服务代理（JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());
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
            RegistryConfig registryConfig = RpcApplication.getConfig().getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("No service provider found for " + serviceName);
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送 TCP 请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(selectedServiceMetaInfo, rpcRequest);
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
