package com.askitis.consulting.jdg.loader;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

@Stateless
public class LoadDataExecutorBean implements Executor {
 
    @Asynchronous
    @Override
    public void execute(Runnable command) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.execute(command);
    }
}
