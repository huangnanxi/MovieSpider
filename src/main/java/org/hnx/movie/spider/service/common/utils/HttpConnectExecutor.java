package org.hnx.movie.spider.service.common.utils;

import org.hnx.movie.spider.service.common.entity.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnectExecutor {

    private final static Logger  httpLogger    = LoggerFactory.getLogger("httpLogger");

    private final static Integer defautTimeOut = 6000;

    private final static int     tryTime       = 3;

    private final static int     maxCallNum    = 10;

    private static int           currCallNum   = 0;

    public static Document connect(String url) {

        reqCall();

        Document doc = null;

        for (int i = 0; i < tryTime; i++) {
            doc = innerConnect(url);
            if (null != doc) {
                break;
            }
            if (i == (tryTime - 1) && null == doc) {
                HttpException httpException = new HttpException(url, "access failed", null);
                httpLogger.error(httpException.toString());
            }
        }

        reqCallEnd();

        return doc;
    }

    private static Document innerConnect(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(defautTimeOut).get();
            httpLogger.info("Success http access. Url is：{}", url);
        } catch (Exception e) {
            HttpException httpException = new HttpException(url, e.getMessage(), e);
            httpLogger.error(httpException.toString());
        }
        return doc;
    }

    private static synchronized void reqCall() {
        if (currCallNum < maxCallNum) {
            currCallNum++;
        } else {
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
            }
            reqCall();
        }
    }

    private static synchronized void reqCallEnd() {
        if (currCallNum > 0) {
            currCallNum--;
        }
    }

}
