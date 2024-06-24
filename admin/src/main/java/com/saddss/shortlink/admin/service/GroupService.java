package com.saddss.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.admin.dao.entity.GroupDO;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
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
     * 新增短链接分组(重载参数，用于注册后生成默认分组）
     * @param requestParam 入参
     */
    void saveGroup(String username, ShortLinkGroupSaveReqDTO requestParam);

    /**
     * 查询短链接分组
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);

    /**
     * 删除短链接分组
     */
    void deleteGroup(String gid);

    /**
     * 短链接分组排序
     */
    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
