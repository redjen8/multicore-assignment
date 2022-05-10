package proj2.prob3;

import java.util.concurrent.atomic.AtomicInteger;

class IncrementModule {
    private AtomicInteger sum;

    IncrementModule() {
        sum = new AtomicInteger();
        sum.set(0);
    }

    public void addToSum(int number) {
        sum.addAndGet(number);
    }

    public void addToSum2(int number) {
        sum.getAndAdd(number);
    }

    public int getCurrentSum() {
        return sum.get();
    }
}

class IncrementWorker extends Thread {

    private IncrementModule incrementModule;
    private int threadNum;
    static int workSize;

    IncrementWorker(int threadNum, IncrementModule im) {
        this.threadNum = threadNum;
        this.incrementModule = im;
    }

    @Override
    public void run() {
        System.out.println("[ThreadNum : " + threadNum + "] Adding " + Integer.toString((threadNum - 1) * workSize) + " to " + Integer.toString(threadNum * workSize));
        for(int i = (threadNum - 1) * workSize + 1; i <= threadNum * workSize; i++) {
            if (i % 2 == 0) incrementModule.addToSum(i);
            else incrementModule.addToSum2(i);
        }
        System.out.println("[ThreadNum : " + threadNum + "] Current Sum : " + Integer.toString(incrementModule.getCurrentSum()));
    }
}

public class ex3 {
    
    private static final int THREAD_NUM = 10;
    private static final int LIMIT_SUM = 100;
    // AtomicInteger, use get(), set(), getAndAdd(), addAndGet() methods
    
    public static void main(String[] args) {
        IncrementModule incrementModule = new IncrementModule();
        IncrementWorker[] workerList = new IncrementWorker[THREAD_NUM];
        IncrementWorker.workSize = LIMIT_SUM / THREAD_NUM;
        for (int i = 1; i <= THREAD_NUM; i++) {
            workerList[i-1] = new IncrementWorker(i, incrementModule);
            workerList[i-1].start();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            try {
                workerList[i].join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Result : " + Integer.toString(incrementModule.getCurrentSum()));
    }
}
