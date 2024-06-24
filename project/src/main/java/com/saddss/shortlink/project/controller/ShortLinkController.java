package com.saddss.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saddss.shortlink.project.common.convention.result.Result;
import com.saddss.shortlink.project.common.convention.result.Results;
import com.saddss.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.saddss.shortlink.project.service.ShortLinkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 新建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 短链接分页查询
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> getPage(ShortLinkPageReqDTO requestParam){
        return Results.success(shortLinkService.getPage(requestParam));
    }

    /**
     * 查询分组内短链接数量
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> getShortLinkCountInGroup(@RequestParam("requestParam") List<String> requestParam){
        return Results.success(shortLinkService.getShortLinkCountInGroup(requestParam));
    }

}
