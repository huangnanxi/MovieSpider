package org.hnx.movie.spider.service.dytt;

import java.util.ArrayList;
import java.util.List;

import org.hnx.movie.spider.service.common.utils.MuiteThreadExecutor;
import org.hnx.movie.spider.service.dytt.entity.DyttCallableProcesser;
import org.hnx.movie.spider.service.dytt.entity.DyttSpiderRunReqTask;

public class DyttSpiderExeService {

    public static void executeSpider() {
        // 初始化爬虫任务
        List<DyttSpiderRunReqTask> spiderRunReqTasks = initSpiderTask();

        // 初始化爬虫任务processer
        List<Runnable> DyttCallableProcesserList = initSpiderProcesser(spiderRunReqTasks);

        // 执行爬虫任务
        MuiteThreadExecutor.execute(DyttCallableProcesserList);
        
        //释放资源
        MuiteThreadExecutor.shutDown();
    }

    private static List<DyttSpiderRunReqTask> initSpiderTask() {
        List<DyttSpiderRunReqTask> spiderRunReqTasks = new ArrayList<DyttSpiderRunReqTask>();

        spiderRunReqTasks.add(new DyttSpiderRunReqTask("剧情", "http://www.dy2018.com/0/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("喜剧", "http://www.dy2018.com/1/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("动作", "http://www.dy2018.com/2/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("爱情", "http://www.dy2018.com/3/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("科幻", "http://www.dy2018.com/4/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("动画", "http://www.dy2018.com/5/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("悬疑", "http://www.dy2018.com/6/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("惊悚", "http://www.dy2018.com/7/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("恐怖", "http://www.dy2018.com/8/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("记录", "http://www.dy2018.com/9/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("同性", "http://www.dy2018.com/10/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("音乐歌舞", "http://www.dy2018.com/11/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("传记", "http://www.dy2018.com/12/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("历史", "http://www.dy2018.com/13/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("战争", "http://www.dy2018.com/14/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("犯罪", "http://www.dy2018.com/15/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("奇幻", "http://www.dy2018.com/16/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("冒险", "http://www.dy2018.com/17/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("灾难", "http://www.dy2018.com/18/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("武侠", "http://www.dy2018.com/19/"));
        spiderRunReqTasks.add(new DyttSpiderRunReqTask("古装", "http://www.dy2018.com/20/"));

        return spiderRunReqTasks;
    }

    private static List<Runnable> initSpiderProcesser(List<DyttSpiderRunReqTask> spiderRunReqTasks) {
        List<Runnable> dyttCallableProcessers = new ArrayList<Runnable>();

        for (DyttSpiderRunReqTask dyttSpiderRunReqTask : spiderRunReqTasks) {
            DyttCallableProcesser dyttCallableProcesser = new DyttCallableProcesser(dyttSpiderRunReqTask);
            dyttCallableProcessers.add(dyttCallableProcesser);
        }

        return dyttCallableProcessers;
    }

}
