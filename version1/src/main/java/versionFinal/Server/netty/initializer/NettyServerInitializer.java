/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Server.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import versionFinal.Server.netty.handler.NettyServerHandler;
import versionFinal.Server.provider.ServiceProvider;
import versionFinal.common.serializer.mycode.MyDecoder;
import versionFinal.common.serializer.mycode.MyEncoder;
import versionFinal.common.serializer.myserializer.JsonSerializer;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-07
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        channelPipeline.addLast(new MyEncoder(new JsonSerializer()));
        channelPipeline.addLast(new MyDecoder());
        channelPipeline.addLast(new NettyServerHandler(serviceProvider));

//        // in station
//        channelPipeline.addLast(
//                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0,4)
//        );
//        // out station
//        channelPipeline.addLast(new LengthFieldPrepender(4));
//        // out station
//        channelPipeline.addLast(new ObjectEncoder());
//        // in station
//        channelPipeline.addLast(new ObjectDecoder(new ClassResolver() {
//            @Override
//            public Class<?> resolve(String s) throws ClassNotFoundException {
//                return Class.forName(s);
//            }
//        }));
//        // in station
//        channelPipeline.addLast(new NettyServerHandler(serviceProvider));
    }
}
