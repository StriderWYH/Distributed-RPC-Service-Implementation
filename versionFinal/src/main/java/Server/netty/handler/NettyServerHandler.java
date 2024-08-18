/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Server.netty.handler;

import Server.provider.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import Server.ratelimit.Ratelimit;
import common.message.RpcRequest;
import common.message.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-07
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        RpcResponse rpcResponse = getResponse(request);
//        System.out.println(request);
//        System.out.println(rpcResponse);
        channelHandlerContext.writeAndFlush(rpcResponse);
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private RpcResponse getResponse(RpcRequest request) {
        String interfaceName = request.getInterfaceName();
        Ratelimit ratelimit = serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
        if(!ratelimit.getToken()){
            System.out.println("Service limit");
            return RpcResponse.fail();
        }
        Object service = serviceProvider.getService(interfaceName);
        try{
            Method method =  service.getClass().getMethod(request.getMethodName(),request.getParamsType());
            Object invoke = method.invoke(service,request.getParams());
            return RpcResponse.sussess(invoke);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Error in method");
            return RpcResponse.fail();
        }
    }
}
