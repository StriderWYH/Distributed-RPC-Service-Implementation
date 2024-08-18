/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Client.retry;

import Client.rpcClient.RpcClient;
import com.github.rholder.retry.*;
import lombok.AllArgsConstructor;
import common.message.RpcRequest;
import common.message.RpcResponse;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-15
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public class guavaRetry {
    private RpcClient rpcClient;
    public RpcResponse sendRequestRetry(RpcRequest request) throws ExecutionException, RetryException {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfException()
                .retryIfResult(response-> response == null || Objects.equals(response.getCode(),500))
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(7))
                .withRetryListener(new RetryListener() { // Notice the first time retry is actually the first time call
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("RetryListener:" + attempt.getAttemptNumber() + " times");
                    }
                })
                .build();
        try{
            return retryer.call(new Callable<RpcResponse>() {
                @Override
                public RpcResponse call() throws Exception {
                   return rpcClient.sendRequest(request);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}
