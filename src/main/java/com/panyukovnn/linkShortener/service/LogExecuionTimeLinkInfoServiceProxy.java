package com.panyukovnn.linkShortener.service;

import com.panyukovnn.linkShortener.dto.CreateShortLinkRequest;
import com.panyukovnn.linkShortener.model.LinkInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

@Slf4j
public class LogExecuionTimeLinkInfoServiceProxy implements LinkInfoService {

    private final LinkInfoService service;

    public LogExecuionTimeLinkInfoServiceProxy(LinkInfoService service) {
        this.service = service;
    }

    @Override
    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            return service.createLinkInfo(request);
        } finally {
            watch.stop();
            log.info("Время выполнения метода createLinkInfo: {} мс", watch.getTotalTimeSeconds());
        }
    }

    @Override
    public LinkInfo getByShortLink(String shortLink) {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            return service.getByShortLink(shortLink);
        } finally {
            watch.stop();
            log.info("Время выполнения метода getByShortLink: {} мс", watch.getTotalTimeSeconds());
        }
    }
}
