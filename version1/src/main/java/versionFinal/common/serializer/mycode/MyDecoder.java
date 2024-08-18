/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.common.serializer.mycode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import versionFinal.common.message.MessageType;
import versionFinal.common.serializer.myserializer.Serializer;

import java.util.List;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-12
 * @Description:
 * @Version: 1.0
 */
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {

        short messageType = in.readShort();

        if(messageType != MessageType.REQUEST.getCode() &&
        messageType != MessageType.RESPONSE.getCode()){
            System.out.println("We do not support this type of Message!");
            return;
        }

        short serializerType = in.readShort();
        Serializer serializer = Serializer.getSerializerBycode(serializerType);
        if(serializer == null)
            throw new RuntimeException("None Existence of this type of Serializer");
        int length = in.readInt();

        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object deserialize = serializer.deserialize(bytes,messageType);
        out.add(deserialize);
    }
}
