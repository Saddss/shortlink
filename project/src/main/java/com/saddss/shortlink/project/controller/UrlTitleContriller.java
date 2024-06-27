package com.saddss.shortlink.project.controller;

import com.saddss.shortlink.project.common.convention.result.Result;
import com.saddss.shortlink.project.common.convention.result.Results;
import com.saddss.shortlink.project.service.IURLService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UrlTitleContriller {
    private final IURLService iurlService;

    @GetMapping("/api/short-link/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url){
        return Results.success(iurlService.getTitleByUrl(url));
    }
}

