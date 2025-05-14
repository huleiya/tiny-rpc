package com.figgo.tinyrpc.fault.tolerant;

/**
 * 容错策略键名常量
 */
public interface TolerantStrategyKeys {
    /**
     * 故障恢复策略
     */
    String FAIL_BACK = "failBack";
    /**
     * 快速失败策略
     */
    String FAIL_FAST = "failFast";
    /**
     * 故障转移策略
     */
    String FAIL_OVER = "failOver";
    /**
     * 静默处理策略
     */
    String FAIL_SAFE = "failSafe";
}
