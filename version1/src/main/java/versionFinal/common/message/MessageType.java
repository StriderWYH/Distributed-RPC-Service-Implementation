/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.common.message;

import lombok.AllArgsConstructor;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-12
 * @Description:
 * @Version: 1.0
 */
@AllArgsConstructor
public enum MessageType {
    REQUEST(0),
    RESPONSE(1);

    private int code;
    public int getCode(){
        return code;
    }

}
