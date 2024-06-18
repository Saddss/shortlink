package com.saddss.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.admin.dao.entity.UserDO;
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
}
