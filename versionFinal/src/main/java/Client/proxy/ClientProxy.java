/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Client.proxy;

import Client.rpcClient.impl.BIOSocketRpcClient;
import Client.serviceCenter.ServiceCenter;
import Client.serviceCenter.ZKServiceCenter;
import Client.circuitBreaker.CircuitBreakProvider;
import Client.circuitBreaker.CircuitBreaker;
import Client.rpcClient.RpcClient;
import Client.rpcClient.impl.NettyRpcClient;
import Client.retry.guavaRetry;
import common.message.RpcRequest;
import common.message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
public class ClientProxy implements InvocationHandler {
    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    private CircuitBreakProvider circuitBreakProvider;
    public ClientProxy(int choose) throws InterruptedException {
        serviceCenter = new ZKServiceCenter();
        circuitBreakProvider = new CircuitBreakProvider();
        switch (choose){
            case 0:
                rpcClient = new NettyRpcClient(serviceCenter);
                break;
            case 1:
                rpcClient = new BIOSocketRpcClient("127.0.0.1",9999);
        }
        return;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsType(method.getParameterTypes()).build();
        CircuitBreaker circuitBreaker=circuitBreakProvider.getCircuitBreaker(method.getName());
        if (!circuitBreaker.allowRequest()){
            return null;
        }
        RpcResponse response = null;
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            guavaRetry retry = new guavaRetry(this.rpcClient);
            response = retry.sendRequestRetry(request);
        }else {
            response = rpcClient.sendRequest(request);
        }

        if (response.getCode() ==200){
            circuitBreaker.recordSuccess();
        }
        if (response.getCode()==500){
            circuitBreaker.recordFailure();
        }

        return response.getData();
    }

    public Object getProxy(Class<?> clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz}, this);
    }
}

