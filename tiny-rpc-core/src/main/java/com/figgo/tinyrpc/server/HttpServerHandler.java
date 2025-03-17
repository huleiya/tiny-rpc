package com.figgo.tinyrpc.server;

import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.model.RpcRequest;
import com.figgo.tinyrpc.model.RpcResponse;
import com.figgo.tinyrpc.registry.LocalRegistry;
import com.figgo.tinyrpc.serializer.Serializer;
import com.figgo.tinyrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * http 请求处理
 *
 * 业务流程如下：
 * 1.反序列化请求为对象，并从请求对象中获取参数，
 * 2.根据服务名称从本地注册器中获取到对应的服务实现类。
 * 3.通过反射机制调用方法，得到返回结果。
 * 4.对返回结果进行封装和序列化，并写入到响应中。
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());

        // 记录日志
        System.out.println("Received request: " + request.method() + " " + request.uri());

        // 异步处理 http 请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为 null，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }
            try {
                // 获取要调用的服务实现类，通过反射调用
                Class<?> impClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = impClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(impClass.newInstance(), rpcRequest.getArgs());
                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);

            }
            // 响应
            doResponse(request, rpcResponse, serializer);
        });


    }

    /**
     * 响应
     * @Param request
     * @Param rpcResponse
     * 1. 设置响应头编码格式
     * 2. 序列化响应结果
     * 3. 写入响应结果
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("Content-Type", "application/json");
        try {
            // 序列化
            byte[] bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
