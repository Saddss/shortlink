package com.saddss.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.project.dao.entity.ShortLinkDO;
import com.saddss.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     * @param requestParam 短链接参数
     * @return 短链接组id、短链接uri、完整url
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 分页查询短链接
     * @param requestParam 分页查询短链接请求参数
     * @return
     */
    IPage<ShortLinkPageRespDTO> getPage(ShortLinkPageReqDTO requestParam);

    /**
     * 获取分组内短链接数量
     * @param requestParam 分组id集合
     * @return
     */
    List<ShortLinkGroupCountQueryRespDTO> getShortLinkCountInGroup(List<String> requestParam);
}
