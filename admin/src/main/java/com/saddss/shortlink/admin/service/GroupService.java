package com.saddss.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.admin.dao.entity.GroupDO;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;

public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     * @param requestParam 入参
     */
    void saveGroup(ShortLinkGroupSaveReqDTO requestParam);
}
