/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package common.serializer.myserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.message.RpcRequest;
import common.message.RpcResponse;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-12
 * @Description:
 * @Version: 1.0
 */
public class JsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSON.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType){
            case 0:
                RpcRequest rpcRequest = JSON.parseObject(bytes, RpcRequest.class);
                Object[] objs = new Object[rpcRequest.getParams().length];
                for(int i=0; i < objs.length; i++){
                    Class<?> paramType = rpcRequest.getParamsType()[i];
                    // JSON cant automatically transfer basic types like int, double, string but not Object, thus need second check
                    if(!paramType.isAssignableFrom(rpcRequest.getParams()[i].getClass())){
                        objs[i] = JSON.toJavaObject((JSONObject) rpcRequest.getParams()[i], rpcRequest.getParamsType()[i]);
                    }else{
                        objs[i] = rpcRequest.getParams()[i];
                    }
                }
                rpcRequest.setParams(objs);
                obj = rpcRequest;
                break;
            case 1:
                RpcResponse rpcResponse = JSON.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = rpcResponse.getDataType();
                int code = rpcResponse.getCode();
                // JSON cant automatically transfer basic types like int, double, string but not Object
                if(code != 500 && !dataType.isAssignableFrom(rpcResponse.getData().getClass())){
                    rpcResponse.setData(JSON.toJavaObject((JSONObject) rpcResponse.getData(), rpcResponse.getDataType()));
                }
                obj = rpcResponse;
                break;
            default:
                System.out.println("The messageType is not supported");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}
