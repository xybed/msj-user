package com.msj.user.mapper;


import com.msj.common.entity.UserToken;

public interface UserTokenCustomMapper {
    String queryToken(String username);

    Integer insertOrUpdateToken(UserToken userToken);

    void logout(String token);
}
