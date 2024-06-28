package com.saddss.shortlink.project.service.impl;

import com.saddss.shortlink.project.common.convention.exception.ClientException;
import com.saddss.shortlink.project.service.IURLService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class URLServiceImpl implements IURLService {
    @SneakyThrows
    @Override
    public String getTitleByUrl(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            throw new ClientException("无效url");
        }
        return doc == null ? null : doc.title();
    }
}
