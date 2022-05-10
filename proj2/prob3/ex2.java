package proj2.prob3;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class IncrementModule {
    private ReentrantReadWriteLock moduleLock;
    static int sum;

    IncrementModule() {
        this.sum = 0;
        moduleLock = new ReentrantReadWriteLock();
    }

    public void addToSum(int number) {
        moduleLock.writeLock().lock();
        sum += number;
        moduleLock.writeLock().unlock();
    }

    public int getCurrentSum() {
        moduleLock.readLock().lock();
        int result = sum;
        moduleLock.readLock().unlock();
        return result;
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
            incrementModule.addToSum(i);
        }
        System.out.println("[ThreadNum : " + threadNum + "] Current Sum : " + Integer.toString(incrementModule.getCurrentSum()));
    }
}

public class ex2 {

    private static final int THREAD_NUM = 10;
    private static final int LIMIT_SUM = 100;
    //ReadWriteLock, use lock(), unlock(), readLock(), writeLock() methods
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
