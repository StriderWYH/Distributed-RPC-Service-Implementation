package versionFinal.common.pojo;

import lombok.*;
import java.io.Serializable;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private Integer id;
    private String  userName;
    private Boolean sex;
}
