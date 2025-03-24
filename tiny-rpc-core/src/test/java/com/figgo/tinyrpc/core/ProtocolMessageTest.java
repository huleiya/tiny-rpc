package com.figgo.tinyrpc.core;

import cn.hutool.core.util.IdUtil;
import com.figgo.tinyrpc.constant.RpcConstant;
import com.figgo.tinyrpc.model.RpcRequest;
import com.figgo.tinyrpc.protocol.*;
import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ProtocolMessageTest {
    @Test
    public void testEncodeAndDecode() throws IOException {
        ProtocolMessage<RpcRequest> message = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa", "bbb"});
        message.setHeader(header);
        message.setBody(rpcRequest);

        Buffer buffer = ProtocolMessageEncoder.encode(message);
        ProtocolMessage<?> decodedMessage = ProtocolMessageDecoder.decode(buffer);
        Assertions.assertNotNull(decodedMessage);
    }
}
