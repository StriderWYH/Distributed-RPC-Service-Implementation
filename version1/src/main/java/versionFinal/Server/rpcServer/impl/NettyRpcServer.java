/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Server.rpcServer.impl;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import versionFinal.Server.netty.initializer.NettyServerInitializer;
import versionFinal.Server.provider.ServiceProvider;
import versionFinal.Server.rpcServer.RpcServer;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-07
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public class NettyRpcServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        NioEventLoopGroup bossgroup = new NioEventLoopGroup();
        NioEventLoopGroup workgroup = new NioEventLoopGroup();
        System.out.println("Server starts running");
        try {
            ServerBootstrap serverbootstrap = new ServerBootstrap();
            serverbootstrap.group(bossgroup, workgroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceProvider));

            ChannelFuture chf = serverbootstrap.bind(port).sync();
            chf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossgroup.shutdownGracefully();
            workgroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
