package versionFinal.Client.rpcClient.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import versionFinal.Client.rpcClient.RpcClient;
import versionFinal.common.message.RpcRequest;
import versionFinal.common.message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class BIOSocketRpcClient implements RpcClient {
    private String host;
    private  int port;

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try{
            Socket socket = new Socket(host, port);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
