package com.figgo.tinyrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理（JDK 动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的默认值对象
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(returnType);
    }
    private Object getDefaultObject(Class<?> typeClass) {
        // 基本类型
        if (typeClass.isPrimitive()) {
            if (typeClass == int.class) {
                return 0;
            } else if (typeClass == long.class) {
                return 0L;
            } else if (typeClass == short.class) {
                return (short) 0;
            } else if ( typeClass == boolean.class) {
                return false;
            }
        }
        // 对象类型
        return null;
    }
}
