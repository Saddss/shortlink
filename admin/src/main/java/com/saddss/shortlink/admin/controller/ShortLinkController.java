package com.saddss.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.remote.ShortLinkRemoteService;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.saddss.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * 新建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.getPage(requestParam);
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){
        return shortLinkRemoteService.updateShortLink(requestParam);
    }
}
