package com.saddss.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.admin.dao.entity.GroupDO;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.saddss.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     * @param requestParam 入参
     */
    void saveGroup(ShortLinkGroupSaveReqDTO requestParam);

    /**
     * 查询短链接分组
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);
}
