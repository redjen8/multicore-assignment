package proj1.problem1;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class pc_dynamic {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 1;

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        int counter = 0;
        int i;

        DynamicThread.t = NUM_THREADS * 2 - 1;
        DynamicThread.endNum = NUM_END;
        DynamicThread.primeNumList.add(2);

        DynamicThread[] threadList = new DynamicThread[NUM_THREADS];
        long startTime = System.currentTimeMillis();
        for (i=0; i<NUM_THREADS; i++) {
            threadList[i] = new DynamicThread(i);
            threadList[i].start();
        }
        try {
            for (i=0; i<NUM_THREADS; i++) {
                threadList[i].join();
            }
            counter = DynamicThread.primeNumList.size();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END -1) + " prime# counter=" + counter);
    }

    private static boolean isPrime(int x) {
        int i;
        if (x <= 1) return false;
        for (i=2; i<x; i++) {
            if (x%i == 0) return false;
        }
        return true;
    }
    
    static class DynamicThread extends Thread {

        private int startNum, threadNum;
        static int endNum, t;
        long startTime, timeDiff;
        static List<Integer> primeNumList = Collections.synchronizedList(new ArrayList<Integer>()); 
    
        DynamicThread(int threadNum) {
            this.startNum = threadNum * 2 + 1;
            this.threadNum = threadNum;
        } 
    
        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            while(t < endNum) {
                if (isPrime(startNum)) {
                    primeNumList.add(startNum);
                };
                startNum = increment();
            }
            timeDiff = System.currentTimeMillis() - startTime;
            System.out.println("[Thread " + threadNum + "] execution time : " + timeDiff + " ms");
        }
    
        static synchronized int increment() {
            t++;
            return t;
        }
    }
}