/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Server.ratelimit.provider;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import versionFinal.Server.ratelimit.Ratelimit;
import versionFinal.Server.ratelimit.impl.TokenBucket;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-16
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitProvider {
    private Map<String, Ratelimit> ratelimitMap = new HashMap<>();

    public Ratelimit getRateLimit(String interfacename){
        return ratelimitMap.get(interfacename);
    }

    public void addRateLimit(String interfacename){
        Ratelimit ratelimit = new TokenBucket(100,10);
        ratelimitMap.put(interfacename,ratelimit);
    }

}
