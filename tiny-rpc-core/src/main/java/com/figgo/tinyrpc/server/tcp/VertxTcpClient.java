package com.figgo.tinyrpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.model.RpcRequest;
import com.figgo.tinyrpc.model.RpcResponse;
import com.figgo.tinyrpc.model.ServiceMetaInfo;
import com.figgo.tinyrpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Vertx TCP 请求客户端
 */
public class VertxTcpClient {
    public void start() {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = res.result();

                for (int i = 0; i < 1000; i++) {
                    // 发送数据
                    Buffer buffer = Buffer.buffer();
                    String str = "Hello, server!Hello, server!Hello, server!Hello, server!";
                    buffer.appendInt(0);
                    buffer.appendInt(str.getBytes().length);
                    buffer.appendBytes(str.getBytes());
                    socket.write(buffer);
                }

                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.out.println("Failed to connect to TCP server: " + res.cause().getMessage());
            }
        });
    }

    /**
     * 发送请求
     *
     * @param selectedServiceMetaInfo
     * @param rpcRequest
     * @return
     * @throws Exception
     */
    public static RpcResponse doRequest(ServiceMetaInfo selectedServiceMetaInfo, RpcRequest rpcRequest) throws Exception {
        // 发送 TCP 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(), res -> {
            if (!res.succeeded()) {
                System.out.println("Failed to connect to TCP server: " + res.cause().getMessage());
                //throw new RuntimeException("连接服务器失败");
                responseFuture.completeExceptionally(new RuntimeException("连接服务器失败"));
                return;
            }
            NetSocket socket = res.result();
            // 发送请求数据
            // 构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            // 生成全局请求 ID
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);

            // 编码请求，发送
            try {
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }

            // 接收响应
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RuntimeException("协议消息解码错误");
                }
            });
            socket.handler(bufferHandlerWrapper);
        });
        // 关闭连接
        netClient.close();
        return responseFuture.get();
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
