/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-16
 * @Description:
 * @Version: 1.0
 */
public class CircuitBreaker {
    private circuitBreakerState state = circuitBreakerState.CLOSED;
    private AtomicInteger failureCount = new AtomicInteger(0);
    private AtomicInteger successCount = new AtomicInteger(0);
    private AtomicInteger requestCount = new AtomicInteger(0);
    //failureThreshold
    private final int failureThreshold;
    //HalfOpen->Closed successrate required
    private final double halfOpenSuccessRate;
    //retry time priod
    private final long retryTimePeriod;
    //last time stamp of failure
    private long lastFailureTime = 0;

    public CircuitBreaker(int failureThreshold, double halfOpenSuccessRate,long retryTimePeriod) {
        this.failureThreshold = failureThreshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.retryTimePeriod = retryTimePeriod;
    }
    //check whether the current circuit breaker allows request
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        System.out.println("CircuitBreaker!!!!!!!!!failureNum=="+failureCount);
        switch (state) {
            case OPEN:
                if (currentTime - lastFailureTime > retryTimePeriod) {
                    state = circuitBreakerState.HALF_OPEN;
                    resetCounts();
                    return true;
                }
                System.out.println("CIRCUIT BREAKER IS STILL OPEN!!!!!!!!!");
                return false;
            case HALF_OPEN:
                requestCount.incrementAndGet();
                return true;
            case CLOSED:
            default:
                return true;
        }
    }
    //record success
    public synchronized void recordSuccess() {
        if (state == circuitBreakerState.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() >= halfOpenSuccessRate * requestCount.get()) {
                state = circuitBreakerState.CLOSED;
                resetCounts();
            }
        } else {
            resetCounts();
        }
    }
    //record failure
    public synchronized void recordFailure() {
        failureCount.incrementAndGet();
        System.out.println("RECORD FAILURE!!!!!!!FAILURE TIME: "+failureCount);
        lastFailureTime = System.currentTimeMillis();
        if (state == circuitBreakerState.HALF_OPEN) {
            state = circuitBreakerState.OPEN;
            lastFailureTime = System.currentTimeMillis();
        } else if (failureCount.get() >= failureThreshold) {
            state = circuitBreakerState.OPEN;
        }
    }
    //reset the counts
    private void resetCounts() {
        failureCount.set(0);
        successCount.set(0);
        requestCount.set(0);
    }

    public circuitBreakerState getState() {
        return state;
    }
}

