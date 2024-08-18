/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-13
 * @Description:
 * @Version: 1.0
 */
public class serviceCache {
    private static Map<String,List<String>> cache = new HashMap<>();

    public void addServiceToCache(String servicename, String address){
        if(cache.containsKey(servicename)){
            List<String> addressList = cache.get(servicename);
            addressList.add(address);
            System.out.println("Added service:" + servicename + "to the cache with address:" + address);
        }else{
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(servicename,addressList);
        }
    }

    public void replaceServiceAddress(String servicename, String oldAddress, String newAddress){
        if(cache.containsKey(servicename)){
            List<String> addressList = cache.get(servicename);
            if(!addressList.remove(oldAddress)){
                System.out.println("No such old Address to be replaced");
            }
            addressList.add(newAddress);
        }else{
            System.out.println("Service:"+servicename+"does not exist in cache");
        }
    }

    public List<String> getServiceFromCache(String servicename){
        if(!cache.containsKey(servicename)){
            return null;
        }
        return cache.get(servicename);
    }

    public void deleteCache(String servicename, String address){
        List<String> addressList = cache.get(servicename);
        addressList.remove(address);
        System.out.println("Service:"+servicename+"with address:"+address+"is deleted from the cache now");
    }
}
