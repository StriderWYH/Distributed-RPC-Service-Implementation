package versionFinal.Server.rpcServer.impl;

import lombok.AllArgsConstructor;
import versionFinal.Server.provider.ServiceProvider;
import versionFinal.Server.rpcServer.RpcServer;
import versionFinal.Server.rpcServer.worker.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public class BIOSocketRpcServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        try{
            ServerSocket serversocket = new ServerSocket(port);
            System.out.println("Server is running");
            while(true){
                Socket socket = serversocket.accept();
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {

    }
}
