/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-16
 * @Description:
 * @Version: 1.0
 */
public class CircuitBreakProvider {
    private Map<String,CircuitBreaker> CircuitBreakerMap=new HashMap<>();
    public synchronized CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;
        if(CircuitBreakerMap.containsKey(serviceName)){
            circuitBreaker=CircuitBreakerMap.get(serviceName);
        }else {
            System.out.println("serviceName="+serviceName+"Created a new Circuit Breaker");
            circuitBreaker=new CircuitBreaker(1,0.5,10000);
            CircuitBreakerMap.put(serviceName,circuitBreaker);
        }
        return circuitBreaker;
    }
}

