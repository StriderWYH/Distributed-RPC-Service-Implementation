package versionFinal.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RpcResponse implements Serializable {
    //状态信息
    private int code;
    private String message;
    private Class<?> dataType;
    //具体数据
    private Object data;

    public static RpcResponse sussess(Object data){
        return RpcResponse.builder().code(200).dataType(data.getClass()).data(data).build();
    }
    public static RpcResponse fail(){

        return RpcResponse.builder().code(500).message("Error in Server").build();
    }
}
