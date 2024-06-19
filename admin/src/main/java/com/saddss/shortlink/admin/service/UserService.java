package com.saddss.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.admin.dao.entity.UserDO;
import com.saddss.shortlink.admin.dto.req.UserLoginReqDTO;
import com.saddss.shortlink.admin.dto.req.UserRegisterReqDto;
import com.saddss.shortlink.admin.dto.req.UserUpdateReqDto;
import com.saddss.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.saddss.shortlink.admin.dto.resp.UserRespDto;

public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名查询用户信息
     */
    UserRespDto getUserByUsername(String username);

    /**
     * 根据用户名查询用户是否存在
     * @return 存在返回false，不存在返回true
     */
    Boolean hasUsername(String username);

    /**
     * 用户注册
     * @param requestParam 用户注册请求参数
     */
    void register(UserRegisterReqDto requestParam);

    /**
     * 修改用户信息
     * @param requestParam 修改用户请求参数
     */
    void update(UserUpdateReqDto requestParam);

    /**
     * 用户登录
     * @return token
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检测用户是否登录
     * @param username 用户名
     * @param token token值（即生成的uuid）
     */
    Boolean checkLogin(String username, String token);

    /**
     * 退出登录
     */
    void logout(String username, String token);
}
