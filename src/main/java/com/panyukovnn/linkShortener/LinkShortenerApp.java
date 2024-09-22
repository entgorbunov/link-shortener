package com.panyukovnn.linkShortener;

import com.panyukovnn.linkShortener.service.LinkInfoService;
import com.panyukovnn.linkShortener.service.LinkInfoServiceImpl;
import com.panyukovnn.linkShortener.service.LogExecuionTimeLinkInfoServiceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LinkShortenerApp {

    private final LinkInfoService service;

    public LinkShortenerApp(LinkInfoService service) {
        this.service = service;
    }

    @PostConstruct
    public void pc() {
        LogExecuionTimeLinkInfoServiceProxy proxy = new LogExecuionTimeLinkInfoServiceProxy(service);
        proxy.getByShortLink("sdf");
    }

    public static void main(String[] args) {
        SpringApplication.run(LinkShortenerApp.class);
    }

}
