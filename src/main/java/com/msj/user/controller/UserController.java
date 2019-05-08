package com.msj.user.controller;

import com.msj.common.constant.Result;
import com.msj.common.constant.ResultGenerator;
import com.msj.common.entity.User;
import com.msj.user.annotation.NeedLogin;
import com.msj.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* Created by 77 on 2018/05/25.
*/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        Integer result = userService.register(user.getUsername(), user.getPassword(), user.getVerifyCode());
        if(result == 1){
            return ResultGenerator.genSuccessResult("该用户注册过且密码相同，自动为您登录");
        }else if(result == 2){
            return ResultGenerator.genSuccessResult("注册成功");
        }else {
            return ResultGenerator.genFailResult("注册失败");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        user = userService.login(user.getUsername(), user.getPassword());
        return ResultGenerator.genSuccessResult(user);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader String token){
        userService.logout(token);
        return ResultGenerator.genSuccessResult("登出成功");
    }

    @NeedLogin
    @PostMapping("/modifyPwd")
    public Result modifyPassword(@RequestBody User user){
        userService.updatePassword(user.getUsername(), user.getPassword());
        return ResultGenerator.genSuccessResult("修改密码成功");
    }

    @NeedLogin
    @PostMapping("/modifyUserInfo")
    public Result modifyUserInfo(@RequestBody User user){
        userService.updateUser(user);
        return ResultGenerator.genSuccessResult("修改信息成功");
    }

}
