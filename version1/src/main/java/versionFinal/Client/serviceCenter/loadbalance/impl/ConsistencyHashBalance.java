/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Client.serviceCenter.loadbalance.impl;

import versionFinal.Client.serviceCenter.loadbalance.Loadbalance;

import java.util.*;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-14
 * @Description:
 * @Version: 1.0
 */
public class ConsistencyHashBalance implements Loadbalance {
    // Number of Virtual Nodes to each real node
    private static final int VIRTUAL_NUM = 10;

    // Hash Cycle for the assignment of virtual nodes
    private SortedMap<Integer, String> hashCycle = new TreeMap<>();

    // Real nodes
    private List<String> realNodes = new LinkedList<>();

    public void init(List<String> serverList){
        for(String server: serverList){
            realNodes.add(server);
            System.out.println("Real Node[" + server +"] is added.");
            for(int i = 0 ; i< VIRTUAL_NUM; i++){
                String virtualNode = server + "&&VN" + i;
                int hash = getHash(virtualNode);
                hashCycle.put(hash,virtualNode);
//                System.out.println("Virtual Nodes [" + virtualNode + "] is added.");
            }
        }
    }

    public String getServer(String node){
        int hash = getHash(node);
        Integer key = null;
        SortedMap<Integer, String> submap = hashCycle.tailMap(hash);
        if(submap.isEmpty()){
            key = hashCycle.lastKey();
        }else{
            key = submap.firstKey();
        }
        String virtualNode = hashCycle.get(key);
        return virtualNode.substring(0,virtualNode.indexOf("&&"));
    }


    @Override
    public String balance(List<String> addressList) {
        this.init(addressList);
        String random = UUID.randomUUID().toString();
        return getServer(random);

    }



    @Override
    public void addNode(String node) {
        if(!realNodes.contains(node)){
            realNodes.add(node);
            System.out.println("Server: Real Node[" + node +"] is added.");
            for(int i = 0 ; i< VIRTUAL_NUM; i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                hashCycle.put(hash,virtualNode);
                System.out.println("Virtual Nodes [" + virtualNode + "] is added.");
            }
        }
    }

    @Override
    public void delNode(String node) {
        if(realNodes.contains(node)){
            realNodes.remove(node);
            System.out.println("Server: Real Node[" + node +"] is deleted.");
            for(int i = 0; i < VIRTUAL_NUM; i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                hashCycle.remove(hash);
                System.out.println("Virtual Nodes [" + virtualNode + "] is deleted.");
            }
        }
    }

    /**
     * FNV1_32_HASH算法
     */
    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
