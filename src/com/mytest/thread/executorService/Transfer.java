package com.mytest.thread.executorService;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class Transfer implements Callable<Boolean> {

    private static final int WAIT_SEC = 5;
    final Account accountFrom;
    final Account accountTo;
    final int amount;

    public Transfer(Account accountFrom, Account accountTo, int amount) {

        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            if (accountFrom.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                try {
                    if (accountTo.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                        try {
                            accountFrom.withdraw(amount);
                            accountTo.deposit(amount);
                            Thread.sleep(new Random().nextInt(10));
                            System.out.println("GOOD Transfer!");

                        } finally {
                            accountTo.getLock().unlock();
                            System.out.println("acc2.getLock().unlock();");
                        }
                    }

                } finally {
                    accountFrom.getLock().unlock();
                    System.out.println("acc1.getLock().unlock();");
                }
                return true;
            } else {
                accountFrom.incFailedTransferCount();
                accountTo.incFailedTransferCount();
                System.out.println("Error waiting Lock!");
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }


    }
}
