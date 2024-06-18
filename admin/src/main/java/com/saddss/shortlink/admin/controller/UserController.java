package com.saddss.shortlink.admin.controller;

import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.common.convention.result.Results;
import com.saddss.shortlink.admin.dto.resp.UserRespDto;
import com.saddss.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        if (result == null){
            return new Result<UserRespDto>().setCode("-1").setMessage("用户查询为空");
        }else{
            return Results.success(result);
        }
    }
}
