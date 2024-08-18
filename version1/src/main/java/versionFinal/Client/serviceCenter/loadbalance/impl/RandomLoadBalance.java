/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Client.serviceCenter.loadbalance.impl;

import versionFinal.Client.serviceCenter.loadbalance.Loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-14
 * @Description:
 * @Version: 1.0
 */
public class RandomLoadBalance implements Loadbalance {
    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("RandomLoadBalance choose the service at:"+ choose );
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
