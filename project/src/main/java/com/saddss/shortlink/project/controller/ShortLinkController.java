package com.saddss.shortlink.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saddss.shortlink.project.common.convention.result.Result;
import com.saddss.shortlink.project.common.convention.result.Results;
import com.saddss.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.saddss.shortlink.project.handler.CustomBlockHandler;
import com.saddss.shortlink.project.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 新建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createShortLinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
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

    /**
     * 修改短链接信息（包括分组）
     */
    @PostMapping("/api/short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 短链接跳转
     */
    @GetMapping("/{short-url}")
    public void jumpToOriginUrl(@PathVariable("short-url") String shortUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        shortLinkService.jumpToOriginUrl(shortUrl, request, response);
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    public Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam) {
        return Results.success(shortLinkService.batchCreateShortLink(requestParam));
    }

}
