package versionFinal.common.service;

import versionFinal.common.pojo.User;
/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
public interface UserService {
    User getUserByUserId(Integer ID);
    Integer insertUserId(User user);
}
