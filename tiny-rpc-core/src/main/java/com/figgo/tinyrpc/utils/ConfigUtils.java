package com.figgo.tinyrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 配置工具类
 */
public class ConfigUtils {
    /**
     * 加载配置对象
     *
     * @param clazz
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix) {
        return loadConfig(clazz, prefix, "");
    }

    private static <T> T loadConfig(Class<T> clazz, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(clazz, prefix);
    }
}
