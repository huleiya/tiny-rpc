package com.figgo.example.provider;

import com.figgo.example.common.service.UserService;
import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.config.RegistryConfig;
import com.figgo.tinyrpc.config.RpcConfig;
import com.figgo.tinyrpc.model.ServiceMetaInfo;
import com.figgo.tinyrpc.registry.LocalRegistry;
import com.figgo.tinyrpc.registry.Registry;
import com.figgo.tinyrpc.registry.RegistryFactory;
import com.figgo.tinyrpc.server.HttpServer;
import com.figgo.tinyrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {
        //Rpc 框架初始化
        RpcApplication.init();

        // 注册服务到本地
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(rpcConfig.getServerPort());
    }
}
