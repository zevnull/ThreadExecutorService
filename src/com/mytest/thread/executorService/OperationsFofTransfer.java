package com.mytest.thread.executorService;


import java.util.Random;
import java.util.concurrent.*;

public class OperationsFofTransfer {

    public static void main(String[] args) {

        final Account accountFrom = new Account(1000);
        Account accountTo = new Account(5000);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("accountFrom.getFailCounter() : " + accountFrom.getFailCounter());
            }
        }, 0, 1, TimeUnit.SECONDS);

        ExecutorService service = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            service.submit(new Transfer(accountFrom, accountTo, new Random().nextInt(400)));
        }

        service.shutdown();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduledExecutorService.shutdown();
    }
}
