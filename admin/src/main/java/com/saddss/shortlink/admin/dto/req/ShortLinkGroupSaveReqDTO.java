package com.saddss.shortlink.admin.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 短链接分组创建参数
 */
@Data
@AllArgsConstructor
public class ShortLinkGroupSaveReqDTO {

    /**
     * 分组名
     */
    private String name;
}
