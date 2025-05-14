package com.figgo.tinyrpc.fault.tolerant;

import com.figgo.tinyrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 降级 - 容错策略
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 获取其他降级服务，调用
        return new RpcResponse();
    }
}
