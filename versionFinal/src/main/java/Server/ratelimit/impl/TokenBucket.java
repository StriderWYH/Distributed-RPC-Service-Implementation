/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Server.ratelimit.impl;

import Server.ratelimit.Ratelimit;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-16
 * @Description:
 * @Version: 1.0
 */
public class TokenBucket implements Ratelimit {

    private static int CAPACITY;
    private static int RATE;
    private volatile static int curCapacity;
    private volatile long timeStamp=System.currentTimeMillis();
    public TokenBucket(int rate, int capacity){
        CAPACITY = capacity;
        RATE = rate;
        curCapacity = capacity;
    }

    @Override
    public synchronized boolean getToken() {
        if(curCapacity> 0){
            curCapacity--;
            return true;
        }

        long current = System.currentTimeMillis();
        if(current - timeStamp >= RATE){
            curCapacity +=(int) (current-timeStamp)/RATE -1;
            if(curCapacity > CAPACITY) curCapacity = CAPACITY;
            timeStamp = current;
            return true;
        }
        return false;
    }
}
