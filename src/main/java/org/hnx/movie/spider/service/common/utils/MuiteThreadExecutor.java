package org.hnx.movie.spider.service.common.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MuiteThreadExecutor {

    private final static int       taskSize = 10;

    private static ExecutorService pool     = Executors.newFixedThreadPool(taskSize);

    public synchronized static void execute(List<Runnable> runnablesList) {
        for (Runnable runnable : runnablesList) {
            pool.execute(runnable);
        }
    }

    public synchronized static void shutDown() {
        pool.shutdown();
    }

}
