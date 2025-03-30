package com.figgo.tinyrpc.fault.retry;

import com.figgo.tinyrpc.model.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            log.info("第 {} 次重试", attempt.getAttemptNumber());
                        }
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
