package project1;

import java.util.List;
import java.util.ArrayList;

class CyclicThread extends Thread {

    private int primeCnt = 0;
    private List<Integer> workArray;

    CyclicThread(int residual, int limit) {
        workArray = new ArrayList<>();
        for (int k = 0; 4*k + residual < limit; k++) {
            workArray.add(4*k + residual);
        }
    }

    @Override
    public void run() {
        for (int target : workArray) {
            if (pc_static_cyclic.isPrime(target)) primeCnt++;
        }
    }

    public int getCnt () {
        return this.primeCnt;
    }
}

public class pc_static_cyclic {
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
        CyclicThread[] threadList = new CyclicThread[NUM_THREADS];
        long startTime = System.currentTimeMillis();
        for (i=0; i<NUM_THREADS; i++) {
            threadList[i] = new CyclicThread(i, NUM_END);
            threadList[i].start();
        }
        try {
            for (i=0; i<NUM_THREADS; i++) {
                threadList[i].join();
                counter += threadList[i].getCnt();
            }
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