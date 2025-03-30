package com.figgo.tinyrpc.fault.retry;

import com.figgo.tinyrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 */
public interface RetryStrategy {
    /**
     *
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
