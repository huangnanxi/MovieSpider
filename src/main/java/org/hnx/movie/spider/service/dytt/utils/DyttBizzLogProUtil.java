package org.hnx.movie.spider.service.dytt.utils;

import org.hnx.movie.spider.dmo.MovieDmo;
import org.hnx.movie.spider.service.common.entity.BizzException;
import org.hnx.movie.spider.service.dytt.entity.DyttSpiderRunReqTask;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DyttBizzLogProUtil {

    private final static Logger bizzLogger = LoggerFactory.getLogger("bizzLogger");

    public static void docNullDeal(Document doc, DyttSpiderRunReqTask dyttSpiderRunReqTask, int spideStep,
            String forThirdStepUrl) {
        if (null == doc) {
            String errorMsg = "bizzErrorType[httpAccessError]: " + spideStep + " 级解析发生异常。";

            if (3 == spideStep) {
                errorMsg = errorMsg + " Url is：" + forThirdStepUrl;
            } else {
                errorMsg = errorMsg + "任务名：" + dyttSpiderRunReqTask.getTypeName();

                if (1 == spideStep) {
                    errorMsg = errorMsg + "。Url is：" + dyttSpiderRunReqTask.getSourceUrl();
                } else if (2 == spideStep) {
                    errorMsg = errorMsg + "。Url is：" + dyttSpiderRunReqTask.constructSecondSpiderUrl();
                }
            }

            bizzLogger.error(errorMsg);

            throw new BizzException(errorMsg);
        }
    }

    public static void processParseExeception(DyttSpiderRunReqTask dyttSpiderRunReqTask, int spideStep, Exception e,
            String forThirdStepUrl) {
        boolean isNeedException = false;
        String errorMsg = "bizzErrorType[parseError]: " + spideStep + " 级解析发生异常。";

        if (3 == spideStep) {
            errorMsg = errorMsg + " Url is：" + forThirdStepUrl;
        } else {
            errorMsg = errorMsg + "任务名：" + dyttSpiderRunReqTask.getTypeName();

            if (1 == spideStep) {
                isNeedException = true;
                errorMsg = errorMsg + "。Url is：" + dyttSpiderRunReqTask.getSourceUrl();
            } else if (2 == spideStep) {
                errorMsg = errorMsg + "。Url is：" + dyttSpiderRunReqTask.constructSecondSpiderUrl();
            }
        }

        bizzLogger.error(errorMsg);

        if (isNeedException) {
            throw new BizzException(errorMsg);
        }
    }

    public static void logMovieItemLand(MovieDmo movieDmo, boolean isSucc) {
        if (isSucc) {
            bizzLogger.info("Success insert one movieInfo. Name: {}", movieDmo.getTitle());
        } else {
            bizzLogger.error("Failed insert one movieInfo. sourceUrl: {}", movieDmo.getOriginalSourceUrl());
        }
    }

}
