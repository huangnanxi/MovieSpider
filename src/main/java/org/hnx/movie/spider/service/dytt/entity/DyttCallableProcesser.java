package org.hnx.movie.spider.service.dytt.entity;

import java.util.List;

import org.hnx.movie.spider.dmo.MovieDmo;
import org.hnx.movie.spider.service.dytt.impl.DyttSpiderAnaylsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DyttCallableProcesser implements Runnable {

    private final static Logger  bizzLogger = LoggerFactory.getLogger("bizzLogger");

    private DyttSpiderRunReqTask movieSpiderRunReqTask;

    public DyttCallableProcesser(DyttSpiderRunReqTask movieSpiderRunReqTask) {
        this.movieSpiderRunReqTask = movieSpiderRunReqTask;
    }

    @Override
    public void run() {

        bizzLogger.info("Dytt：taskName：{}，start do", movieSpiderRunReqTask.getTypeName());

        try {
            // 一级解析
            DyttSpiderAnaylsService.firstSpideAnayLs(movieSpiderRunReqTask);

            bizzLogger.info("Dytt：taskName：{}，finish 1-level parse", movieSpiderRunReqTask.getTypeName());

            // 二级解析
            List<String> thirdSpideUrlList = DyttSpiderAnaylsService.secondSpideAnayls(movieSpiderRunReqTask);

            bizzLogger.info("Dytt：taskName：{}，finish 2-level parse", movieSpiderRunReqTask.getTypeName());

            // 三级解析
            List<MovieDmo> movieDmos = DyttSpiderAnaylsService.thirdSpideAnayls(movieSpiderRunReqTask.getTypeName(),
                    thirdSpideUrlList);

            bizzLogger.info("Dytt：taskName：{}，finish 3-level parse", movieSpiderRunReqTask.getTypeName());

            // 落地入库
            DyttSpiderAnaylsService.landMovieItems(movieDmos);

            bizzLogger.info("Dytt：taskName：{}，finish dbInsert", movieSpiderRunReqTask.getTypeName());
        } catch (Exception e) {
            throw e;
        } finally {
            bizzLogger.info("Dytt：taskName：{}，end do", movieSpiderRunReqTask.getTypeName());
        }
    }

}
