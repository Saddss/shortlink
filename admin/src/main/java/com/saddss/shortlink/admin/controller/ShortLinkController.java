package com.saddss.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkBaseInfoRespDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.saddss.shortlink.admin.util.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

//    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
//    };

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    /**
     * 新建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam);
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){
        return shortLinkActualRemoteService.updateShortLink(requestParam);
    }

    /**
     * 根据url获取网站标
     */
    @GetMapping("/api/short-link/admin/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url){
        return shortLinkActualRemoteService.getTitleByUrl(url);
    }

    /**
     * 批量创建短链接
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }
}
