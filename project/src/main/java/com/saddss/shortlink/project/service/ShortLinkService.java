package com.saddss.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saddss.shortlink.project.dao.entity.ShortLinkDO;
import com.saddss.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.saddss.shortlink.project.dto.req.*;
import com.saddss.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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

    /**
     * 修改短链接信息
     * @param requestParam 修改短链接信息请求参数
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);

    /**
     * 跳转至原始链接
     * @param shortUrl 短链接
     * @param request http请求
     * @param response http响应
     */
    void jumpToOriginUrl(String shortUrl, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 批量创建短链接
     *
     * @param requestParam 批量创建短链接请求参数
     * @return 批量创建短链接返回参数
     */
    ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam);


    /**
     * 短链接统计
     *
     * @param shortLinkStatsRecord 短链接统计实体参数
     */
    void shortLinkStats(ShortLinkStatsRecordDTO shortLinkStatsRecord);
}
