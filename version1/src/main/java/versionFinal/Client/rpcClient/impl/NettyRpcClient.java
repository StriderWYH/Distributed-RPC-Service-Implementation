/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import versionFinal.Client.netty.initializer.NettyClientInitializer;
import versionFinal.Client.rpcClient.RpcClient;
import versionFinal.Client.serviceCenter.ServiceCenter;
import versionFinal.common.message.RpcRequest;
import versionFinal.common.message.RpcResponse;

import java.net.InetSocketAddress;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-07
 * @Description:
 * @Version: 1.0
 */
public class NettyRpcClient implements RpcClient {
    private ServiceCenter serviceCenter;
    public NettyRpcClient(ServiceCenter serviceCenter) throws InterruptedException {
        this.serviceCenter=serviceCenter;
    }

    private static final Bootstrap bootstrap;
    private static final NioEventLoopGroup nioeventloopgroup;

    static {
        nioeventloopgroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(nioeventloopgroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }


    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        InetSocketAddress inetSocketAddress= serviceCenter.serviceDiscovery(request.getInterfaceName());
        if(inetSocketAddress == null) return RpcResponse.fail();
        String host = inetSocketAddress.getHostName();
        int port = inetSocketAddress.getPort();
        try{
            ChannelFuture chf = bootstrap.connect(host,port).sync();
            Channel ch = chf.channel();
            ch.writeAndFlush(request);
            ch.closeFuture().sync();

            AttributeKey<RpcResponse> responseKey = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = ch.attr(responseKey).get();
//            System.out.println("Client got response");
//            System.out.println(response);
            return response;
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
