package versionFinal.Client.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import versionFinal.Client.netty.handler.NettyClientHandler;
import versionFinal.common.serializer.mycode.MyDecoder;
import versionFinal.common.serializer.mycode.MyEncoder;
import versionFinal.common.serializer.myserializer.JsonSerializer;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());

//        pipeline.addLast(
//                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4)
//        );
//        pipeline.addLast(new LengthFieldPrepender(4));
//
//        pipeline.addLast(new ObjectEncoder());
//
//        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
//            @Override
//            public Class<?> resolve(String s) throws ClassNotFoundException {
////                System.out.println("Decoding");
//               return Class.forName(s);
//            }
//        }));
//
//        pipeline.addLast(new NettyClientHandler());

    }
}
