package versionFinal.Client.proxy;

import versionFinal.Client.circuitBreaker.CircuitBreakProvider;
import versionFinal.Client.circuitBreaker.CircuitBreaker;
import versionFinal.Client.rpcClient.RpcClient;
import versionFinal.Client.rpcClient.impl.BIOSocketRpcClient;
import versionFinal.Client.rpcClient.impl.NettyRpcClient;
import versionFinal.Client.retry.guavaRetry;
import versionFinal.Client.serviceCenter.ServiceCenter;
import versionFinal.Client.serviceCenter.ZKServiceCenter;
import versionFinal.common.message.RpcRequest;
import versionFinal.common.message.RpcResponse;

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

