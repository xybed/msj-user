package com.msj.user.service;


import com.msj.common.entity.User;

/**
 * Created by 77 on 2018/05/25.
 */
public interface UserService {
    Integer register(String username, String password, String verifyCode);

    User login(String username, String password);

    void logout(String token);

    void updatePassword(String username, String password);

    void updateUser(User user);
}
