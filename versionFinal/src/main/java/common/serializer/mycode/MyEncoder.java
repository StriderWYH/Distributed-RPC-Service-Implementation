/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package common.serializer.mycode;

import common.message.RpcRequest;
import common.message.RpcResponse;
import common.serializer.myserializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import common.message.MessageType;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-12
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    // Coding: (Short)MessageType + (Short)serilizerType + (Int)DataLength + Data
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
//        System.out.println(msg.getClass());
        // 1. Add MessageType
        if(msg instanceof RpcRequest){
            out.writeShort(MessageType.REQUEST.getCode());
        }else if(msg instanceof RpcResponse){
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        // 2. Add serializer Type
        out.writeShort(serializer.getType());
        // get the serialized Data
        byte[] serializeBytes = serializer.serialize(msg);
        // 3. Add the length of the data
        out.writeInt(serializeBytes.length);
        // 4. Add Data
        out.writeBytes(serializeBytes);
    }
}
