package com.saddss.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.common.convention.result.Results;
import com.saddss.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.saddss.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.saddss.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站管理控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
//    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
//    };
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    private final RecycleBinService recycleBinService;
    /**
     * 保存回收站
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkActualRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageRecycleBinShortLink(requestParam);
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkActualRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }


    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        shortLinkActualRemoteService.removeRecycleBin(requestParam);
        return Results.success();
    }



}
