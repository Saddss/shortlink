package com.saddss.shortlink.project.service.impl;

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
        Document doc = Jsoup.connect(url).get();
        return doc.title();
    }
}
