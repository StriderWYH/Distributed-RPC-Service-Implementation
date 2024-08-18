/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Server.provider;

import lombok.Getter;
import Server.ratelimit.provider.RateLimitProvider;
import Server.serviceRegister.ServiceRegister;
import Server.serviceRegister.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
@Getter
public class ServiceProvider {
    private Map<String,Object> interfaceProvider;

    private int port;
    private String host;
    private ServiceRegister serviceRegister;
    private RateLimitProvider rateLimitProvider;

    public ServiceProvider(String host, int port){
        this.interfaceProvider=new HashMap<>();
        this.host = host;
        this.port = port;
        this.serviceRegister = new ZKServiceRegister();
        this.rateLimitProvider = new RateLimitProvider();
    }

    public void provideServiceInterface(Object service, boolean retryable){
        Class<?>[] interfaceNames=service.getClass().getInterfaces();

        for (Class<?> clazz:interfaceNames){
            interfaceProvider.put(clazz.getName(),service);
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host,port), retryable);
            rateLimitProvider.addRateLimit(clazz.getName());
        }

    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
