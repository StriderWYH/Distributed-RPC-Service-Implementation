package versionFinal.Client.rpcClient;

import versionFinal.common.message.RpcRequest;
import versionFinal.common.message.RpcResponse;


public interface   RpcClient {
    //定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
