package com.nana.devkit.optimize;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:线程池
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class LaunchTaskPool {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;


    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    private static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    public LaunchTaskPool() {
    }

    private static void runTask(TaskPriority priority, Runnable runnable) {
        switch (priority) {
            case BLOCK:
                runnable.run();
                break;
            case IMMEDIATELY:
                THREAD_POOL_EXECUTOR.execute(runnable);
                break;
            case PRIORITY:
                THREAD_POOL_EXECUTOR.execute(runnable);
                break;
            default:
                break;
        }
    }



    public void postBlock(Runnable runnable) {
        runTask(TaskPriority.BLOCK, runnable);
    }

    public static void postImmediately(Runnable runnable) {
        runTask(TaskPriority.IMMEDIATELY, runnable);
    }

    public static void postDelay(Runnable runnable) {
        runTask(TaskPriority.PRIORITY, runnable);
    }

}
