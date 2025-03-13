package com.figgo.tinyrpc;

import com.figgo.tinyrpc.config.RpcConfig;
import com.figgo.tinyrpc.constant.RpcConstant;
import com.figgo.tinyrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", rpcConfig.toString());
    }

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig initConfig;
        try {
            initConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            initConfig = new RpcConfig();
        }
        init(initConfig);
    }
    /**
     * 获取配置
     */
    public static RpcConfig getConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
