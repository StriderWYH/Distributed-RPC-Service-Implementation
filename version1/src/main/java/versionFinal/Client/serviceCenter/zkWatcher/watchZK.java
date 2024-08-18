/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Client.serviceCenter.zkWatcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import versionFinal.Client.cache.serviceCache;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-13
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
@Builder
@Data
public class watchZK {
    private CuratorFramework client;

    public serviceCache cache;

    public void watchToUpdate(String path) throws InterruptedException {
        // Create Curator node cache
        CuratorCache curatorCache = CuratorCache.build(client, "/");

        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forCreates(childData -> {
                    String[] pathList= parsePath(childData);
                    if(pathList.length<=2) return;
                    else {
                        String serviceName=pathList[1];
                        String address=pathList[2];
                        //将新注册的服务加入到本地缓存中
                        this.cache.addServiceToCache(serviceName,address);
                    }
                })
                .forChanges((childData, childData1) ->{
                    if (childData.getData() != null) {
                        System.out.println("Date before revision: " + new String(childData.getData()));
                    } else {
                        System.out.println("First assigned value");
                    }
                    String[] oldPathList=parsePath(childData);
                    String[] newPathList=parsePath(childData1);
                    cache.replaceServiceAddress(oldPathList[1],oldPathList[2],newPathList[2]);
                    System.out.println("Updated Data: " + new String(childData1.getData()));
                })
                .forDeletes(childData -> {
                    String[] pathList_d= parsePath(childData);
                    if(pathList_d.length<=2) return;
                    else {
                        String serviceName=pathList_d[1];
                        String address=pathList_d[2];
                        cache.deleteCache(serviceName,address);
                    }
                }).build();
        curatorCache.listenable().addListener(listener);
    }

    public String[] parsePath(ChildData childData){
        //Get the updated path
        String path=new String(childData.getPath());
        return path.split("/");
    }
}
