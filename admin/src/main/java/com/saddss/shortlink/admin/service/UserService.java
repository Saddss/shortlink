package com.saddss.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.admin.dao.entity.UserDO;
import com.saddss.shortlink.admin.dto.resp.UserRespDto;

public interface UserService extends IService<UserDO> {
    UserRespDto getUserByUsername(String username);
}
