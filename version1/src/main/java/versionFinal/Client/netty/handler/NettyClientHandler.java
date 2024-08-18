package versionFinal.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import versionFinal.common.message.RpcResponse;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
//        System.out.println("channelRead0:");
//        System.out.println(rpcResponse);
        channelHandlerContext.channel().attr(key).set(rpcResponse);
        channelHandlerContext.channel().close();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //Exception handler
        cause.printStackTrace();
        ctx.close();
    }
}
