package com.saddss.shortlink.admin.remote;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saddss.shortlink.admin.common.convention.result.Result;
import com.saddss.shortlink.admin.dto.req.ShortLinkStatsReqDTO;
import com.saddss.shortlink.admin.dto.resp.ShortLinkStatsRespDTO;
import com.saddss.shortlink.admin.remote.dto.req.*;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.saddss.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短链接中台远程调用服务
 */
public interface ShortLinkRemoteService {

    default Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam){
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<Result<ShortLinkCreateRespDTO>>(){});

    }
    default Result<IPage<ShortLinkPageRespDTO>> getPage(ShortLinkPageReqDTO requestParam){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/page", requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<Result<IPage<ShortLinkPageRespDTO>>>(){});
    }

    default Result<List<ShortLinkGroupCountQueryRespDTO>> getShortLinkCountInGroup(List<String> requestParam){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/count", requestMap);
        return JSON.parseObject(resultStr, new TypeReference<Result<List<ShortLinkGroupCountQueryRespDTO>>>(){});
    }

    default Result<Void> updateShortLink(ShortLinkUpdateReqDTO requestParam){
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/update", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<Result<Void>>(){});
    }

    default Result<String> getTitleByUrl(String url){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("url", url);
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title", requestMap);
        return JSON.parseObject(resultStr, new TypeReference<Result<String>>(){});
    }

    default Result<Void> saveRecycleBin(RecycleBinSaveReqDTO requestParam){
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/save", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<Result<Void>>(){});
    }


    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 分页短链接请求参数
     * @return 查询短链接响应
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gidList", requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/page", requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }

    /**
     * 恢复短链接
     *
     * @param requestParam 短链接恢复请求参数
     */
    default Result<Void> recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/recover", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


    /**
     * 移除短链接
     *
     * @param requestParam 短链接移除请求参数
     */
    default Result<Void> removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/remove", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param requestParam 访问短链接监控请求参数
     * @return 短链接监控信息
     */
    default Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats", BeanUtil.beanToMap(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }
}
