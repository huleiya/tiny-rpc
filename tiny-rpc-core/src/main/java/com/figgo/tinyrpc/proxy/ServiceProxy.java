package com.figgo.tinyrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.model.RpcRequest;
import com.figgo.tinyrpc.model.RpcResponse;
import com.figgo.tinyrpc.serializer.JdkSerializer;
import com.figgo.tinyrpc.serializer.Serializer;
import com.figgo.tinyrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理（JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        //Serializer serializer = new JdkSerializer();
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());
        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化请求
            byte[] requestBytes = serializer.serialize(rpcRequest);
            // 发送请求
            // todo 注意这里地址被硬编码了（需要使用注册中心和服务发现机制解决） http://localhost:8080
            try (HttpResponse httpResponse = HttpRequest.post("http://"
                            + RpcApplication.getConfig().getServerHost()
                            + ":" + RpcApplication.getConfig().getServerPort())
                    .body(requestBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化响应
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
