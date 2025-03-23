package com.figgo.example.consumer;

import com.figgo.example.common.model.User;
import com.figgo.example.common.service.UserService;
import com.figgo.tinyrpc.RpcApplication;
import com.figgo.tinyrpc.config.RpcConfig;
import com.figgo.tinyrpc.proxy.ServiceProxyFactory;
import com.figgo.tinyrpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
//        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//        System.out.println(rpcConfig);

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("figgo");
        for (int i = 0; i < 3; i++) {
            // 调用
            User newUser = userService.getUser(user);
            if (newUser != null) {
                System.out.println(newUser.getName());
            } else {
                System.out.println("user == null");
            }
            long number = userService.getNumber();
            System.out.println(number);
        }
    }
}
