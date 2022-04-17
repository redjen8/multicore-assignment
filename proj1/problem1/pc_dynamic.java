package proj1.problem1;

import java.util.concurrent.atomic.AtomicInteger;

class DynamicThread extends Thread {

    private static AtomicInteger currentNum = new AtomicInteger(1);
    public static int primeCnt = 0;
    private int limit = 200000;

    DynamicThread(int limit) {
        this.limit = limit;
    } 

    @Override
    public void run() {
        while(currentNum.get() < limit) {
            increment();
        }
    }

    static synchronized void increment() {
        if (pc_dynamic.isPrime(currentNum.get())) {
            primeCnt++;
        }
        currentNum.incrementAndGet();
    }
}

public class pc_dynamic {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 1;

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        NUM_THREADS = 4;
        int counter = 0;
        int i;
        DynamicThread[] threadList = new DynamicThread[NUM_THREADS];
        long startTime = System.currentTimeMillis();
        for (i=0; i<NUM_THREADS; i++) {
            threadList[i] = new DynamicThread(NUM_END);
            threadList[i].start();
        }
        try {
            for (i=0; i<NUM_THREADS; i++) {
                threadList[i].join();
            }
            counter = DynamicThread.primeCnt;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END -1) + " prime# counter=" + counter);
    }

    static boolean isPrime(int x) {
        int i;
        if (x <= 1) return false;
        for (i=2; i<x; i++) {
            if (x%i == 0) return false;
        }
        return true;
    }
    
}