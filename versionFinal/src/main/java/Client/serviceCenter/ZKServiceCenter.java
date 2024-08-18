/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Client.serviceCenter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import Client.cache.serviceCache;
import Client.serviceCenter.loadbalance.impl.ConsistencyHashBalance;
import Client.serviceCenter.zkWatcher.watchZK;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-09
 * @Description:
 * @Version: 1.0
 */
public class ZKServiceCenter implements ServiceCenter{
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRYABLE = "RETRYABLE";
    private serviceCache cache;

    public ZKServiceCenter() throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,4, 4000);
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .connectionTimeoutMs(5000)
                .namespace(ROOT_PATH)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        System.out.println("Connected to Zookeeper");
        // Initialize local cache
        cache = new serviceCache();
        watchZK watcher = new watchZK(client,cache);
        watcher.watchToUpdate(ROOT_PATH);
    }
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            Stat stat = client.checkExists().forPath("/" + serviceName);
            if(stat == null){
                return  null;
            }
            // First Try to find in local cache
            List<String> addressList = cache.getServiceFromCache(serviceName);
            if(addressList == null){
                addressList = client.getChildren().forPath("/" + serviceName);
            }
            if(addressList == null || addressList.isEmpty()){
                System.out.println("Cant get service IP");
                return null;
            }
//            System.out.println(addressList);
            // Use Load balance
            String address = new ConsistencyHashBalance().balance(addressList);
//            String address = addressList.get(0);
            String[] result = address.split(":");
            return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkRetry(String serviceName) {
        boolean retVal = false;
        try{
          Stat stat = client.checkExists().forPath("/" + RETRYABLE);
          if(stat == null){
              return  retVal;
          }
          List<String> addressList = client.getChildren().forPath("/" + RETRYABLE);
          if(addressList != null && addressList.contains(serviceName)){
              retVal = true;
              System.out.println(serviceName+ "is on the retryable list");
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
