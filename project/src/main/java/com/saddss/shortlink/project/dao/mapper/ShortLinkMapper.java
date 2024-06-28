package com.saddss.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saddss.shortlink.project.dao.entity.ShortLinkDO;
import com.saddss.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;

public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    /**
     * 分页统计回收站短链接
     */
    IPage<ShortLinkDO> pageRecycleBinLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
