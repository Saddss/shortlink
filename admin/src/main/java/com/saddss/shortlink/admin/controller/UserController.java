package com.saddss.shortlink.admin.controller;

import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.common.convention.result.Results;
import com.saddss.shortlink.admin.dto.req.UserRegisterReqDto;
import com.saddss.shortlink.admin.dto.resp.UserRespDto;
import com.saddss.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDto> getUserByUsername(@PathVariable("username") String username){
        UserRespDto result = userService.getUserByUsername(username);
        return Results.success(result);
    }

    /**
     * 根据用户名查询用户是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDto requestParam){
        userService.register(requestParam);
        return Results.success();
    }
}
