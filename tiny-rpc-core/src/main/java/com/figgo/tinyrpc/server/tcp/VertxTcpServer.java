package com.figgo.tinyrpc.server.tcp;

import com.figgo.tinyrpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {
    private byte[] handleRequest(byte[] requestData) {
        // 在这里编写请求处理的逻辑，根据 requestData 构造响应数据并返回
        return "Hello, client!".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(new TcpServerHandler());
//        server.connectHandler(socket -> {

//            String testMessage = "Hello, server!Hello, server!Hello, server!Hello, server!";
//            int messageLength = testMessage.getBytes().length;

            // 构造 parser
//            RecordParser recordParser = RecordParser.newFixed(8);
//            recordParser.setOutput(new Handler<Buffer>() {
//                // 初始化
//                int size = -1;
//                // 一次完整的读取（头 + 体）
//                Buffer resultBuffer = Buffer.buffer();
//
//                @Override
//                public void handle(Buffer buffer) {
//                    if (size == -1) {
//                        // 读取消息体长度
//                        size = buffer.getInt(4);
//                        recordParser.fixedSizeMode(size);
//                        // 写入头信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                    } else {
//                        // 写入体信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                        System.out.println(resultBuffer.toString());
//                        // 重置一轮
//                        recordParser.fixedSizeMode(8);
//                        size = -1;
//                        resultBuffer = Buffer.buffer();
//                    }
//                }
//            });
//            socket.handler(recordParser);
//        });

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, res -> {
            if (res.succeeded()) {
                System.out.println("TCP server started on port " + port);
            } else {
                System.out.println("Failed to start TCP server: " + res.cause().getMessage());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
