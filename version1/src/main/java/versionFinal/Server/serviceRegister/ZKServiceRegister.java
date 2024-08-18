/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Server.serviceRegister;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-09
 * @Description:
 * @Version: 1.0
 */
public class ZKServiceRegister implements ServiceRegister{
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRYABLE = "RETRYABLE";

    public ZKServiceRegister(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,4,4000);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("Zookeeper Connected");
    }
    @Override
    public void register(String servicename, InetSocketAddress serviceproviderAddress, boolean retryable) {
        try{
            // Set the serviceName as persistent, when a server shuts down, only the server address in the namespace will be deleted
            if(client.checkExists().forPath("/" + servicename) == null){
                System.out.println("created PERSISTENT Znode");
                client.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath("/"+servicename);
            }
            String path = "/" + servicename + "/" + serviceproviderAddress.getHostName() +
                    ":" + serviceproviderAddress.getPort();
            // The server address will be deleted once it shuts down
            client.create().creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
            // If this service is retryable, then also add it to the RETYRABLE namespace
            if(retryable){
                path = "/" + RETRYABLE + "/" + servicename;
                client.create().creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(path);
            }
        }catch (Exception e){
            System.out.println("The Service already exists");
        }
    }
}
