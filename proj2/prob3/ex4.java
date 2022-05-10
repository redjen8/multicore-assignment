package proj2.prob3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class WaitAndWorker extends Thread {

    private int threadNum;
    static CyclicBarrier cyclicBarrier;

    WaitAndWorker(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        System.out.println("[Thread " + Integer.toString(threadNum) + "] : started");
        try {
            Thread.sleep((int)(Math.random() * 10000));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[Thread " + Integer.toString(threadNum) + "] : calls await(), waiting..");
        try {
            cyclicBarrier.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("[Thread " + Integer.toString(threadNum) + "] : finished");
    }
}
public class ex4 {
    private static final int THREAD_NUM = 10;
    private static final int BARRIER_SIZE = 5;
    //CyclicBarrier, use await() methods
    public static void main(String[] args) {
        WaitAndWorker[] workerList = new WaitAndWorker[THREAD_NUM];
        WaitAndWorker.cyclicBarrier = new CyclicBarrier(BARRIER_SIZE);
        for (int i = 0; i<THREAD_NUM; i++) {
            workerList[i] = new WaitAndWorker(i+1);
            workerList[i].start();
        }
        for (int i = 0; i<THREAD_NUM; i++) {
            try {
                workerList[i].join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
