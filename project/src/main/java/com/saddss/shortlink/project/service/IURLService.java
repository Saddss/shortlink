package com.saddss.shortlink.project.service;

public interface IURLService {
    /**
     * 根据url获取网站标题
     * @param url
     * @return
     */
    String getTitleByUrl(String url);
}
