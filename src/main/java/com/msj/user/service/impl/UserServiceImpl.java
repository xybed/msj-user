package com.msj.user.service.impl;

import com.msj.common.constant.Constants;
import com.msj.common.constant.ResultEnum;
import com.msj.common.constant.ServiceException;
import com.msj.common.entity.User;
import com.msj.common.entity.UserToken;
import com.msj.common.enums.Sex;
import com.msj.user.mapper.UserCustomMapper;
import com.msj.user.mapper.UserMapper;
import com.msj.user.mapper.UserTokenCustomMapper;
import com.msj.user.service.UserService;
import com.msj.user.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.SignatureException;


/**
 * Created by 77 on 2018/05/25.
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserCustomMapper userCustomMapper;
    @Resource
    private UserTokenCustomMapper userTokenCustomMapper;

    @Transactional
    @Override
    public Integer register(String username, String password, String verifyCode) {
        /*
        1.先检测此用户是否注册过
        2.若注册过，密码是否相同
        3.没有注册过就注册，设置默认信息
        4.设置消息主体id，3、4步骤要用到事务
         */
        Integer isRegister = userCustomMapper.verifyRegister(username);
        if(isRegister == 1){
            Integer passwordIsCorrect = userCustomMapper.verifyPassword(username, password);
            if(passwordIsCorrect == 1){
                return 1;
            }else {
                throw new ServiceException(ResultEnum.REGISTERED);
            }
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setVerifyCode(verifyCode);
        user.setRegisterDate("");
        user.setAvatar(Constants.DEFAULT_AVATAR);
        //注册时，昵称用手机号代替
        user.setNickname(username);
        user.setMobilePhone(username);
        //性别为未知
        user.setSex(Sex.UNKNOW.ordinal());

        try {
            //注册用户
            //如果sql执行不成功，这一句便会抛异常，事务回滚
            userMapper.insertSelective(user);
            //设置pid
            userCustomMapper.updatePid(username);
            return 2;
        }catch (Exception e){
            throw new ServiceException(ResultEnum.REGISTER_FAIL);
        }
    }

    @Override
    public User login(String username, String password) {
        User user = userCustomMapper.login(username, password);
        if(user == null)
            throw new ServiceException(ResultEnum.LOGIN_ERROR);

        String token = JWTUtil.createNewToken(username);
        //把token缓存到redis中
//        String key = "token" + username;
//        JedisUtil.getInstance().set(key, token);
//        JedisUtil.getInstance().expire(key, 24 * 60 * 60);

        UserToken userToken = new UserToken();
        userToken.setUsername(username);
        userToken.setToken(token);
        Integer result = userTokenCustomMapper.insertOrUpdateToken(userToken);
        //插入影响行为1，更新影响行为2
        if(result != 1 && result != 2){
            throw new ServiceException(ResultEnum.LOGIN_FAIL);
        }

        user.setAvatar(Constants.URL_PREFIX+user.getAvatar());
        user.setToken(token);
        return user;
    }

    @Override
    public void logout(String token) {
        Claims claims = null;
        try {
            claims = JWTUtil.getClaims(token);
            String username = claims.getSubject();
//            JedisUtil.getInstance().del("token"+username);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
//        userTokenCustomMapper.logout(token);
    }

    @Override
    public void updatePassword(String username, String password) {
        Integer result = userCustomMapper.updatePassword(username, password);
        if(result != 1)
            throw new ServiceException(ResultEnum.MODIFY_FAIL);
    }

    @Override
    public void updateUser(User user) {
        String avatar = user.getAvatar();
        avatar = avatar.substring((Constants.URL_PREFIX).length(), avatar.length());
        user.setAvatar(avatar);
        Integer result = userMapper.updateByPrimaryKeySelective(user);
        if(result != 1)
            throw new ServiceException(ResultEnum.MODIFY_FAIL);
    }

}
