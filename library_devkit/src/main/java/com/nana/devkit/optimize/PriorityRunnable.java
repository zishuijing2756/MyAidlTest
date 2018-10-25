package com.nana.devkit.optimize;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wujl on 2016/8/16.
 * email:zjwujlei@sina.com
 */
public class PriorityRunnable implements Runnable {
    public TaskPriority priority;
    private static final AtomicInteger AtomicId = new AtomicInteger(0);
    public  int id;
    private Runnable runnable;
    public PriorityRunnable(TaskPriority priority, Runnable runnable){
        this.priority=priority;
        this.runnable=runnable;
        id = AtomicId.getAndIncrement();
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        runnable.run();
    }
}
