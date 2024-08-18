package versionFinal.Server;

import versionFinal.Server.provider.ServiceProvider;
import versionFinal.Server.rpcServer.RpcServer;
import versionFinal.Server.rpcServer.impl.NettyRpcServer;
import versionFinal.Server.serviceImplement.UserServiceImpl;
import versionFinal.common.service.UserService;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",9999);
        serviceProvider.provideServiceInterface(userService, true);

        RpcServer rpcServer=new NettyRpcServer(serviceProvider);
        rpcServer.start(9999);
    }
}
