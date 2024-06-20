package com.saddss.shortlink.admin.controller;

import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.common.convention.result.Results;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.saddss.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> saveGroup(@RequestBody ShortLinkGroupSaveReqDTO requestParam){
        groupService.saveGroup(requestParam);
        return Results.success();
    }

}
