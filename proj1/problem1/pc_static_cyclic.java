package proj1.problem1;

public class pc_static_cyclic {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 1;

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
    
        int counter = 0;
        int i;
        CyclicThread.threadSize = NUM_THREADS;
        CyclicThread.limit = NUM_END;
        CyclicThread[] threadList = new CyclicThread[NUM_THREADS];
        long startTime = System.currentTimeMillis();
        for (i=0; i<NUM_THREADS; i++) {
            threadList[i] = new CyclicThread(i);
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

    private static boolean isPrime(int x) {
        int i;
        if (x <= 1) return false;
        for (i=2; i<x; i++) {
            if (x%i == 0) return false;
        }
        return true;
    }

    static class CyclicThread extends Thread {

        private int primeCnt = 0;
        private int threadNum = 0;
        static int limit = 0;
        static int threadSize = 0;

    
        CyclicThread(int residual) {
            this.threadNum = residual;
        }
    
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = threadNum; i<limit; i+=threadSize) {
                if (pc_static_cyclic.isPrime(i)) primeCnt++;
            }
            long timeDiff = System.currentTimeMillis() - startTime;
            System.out.println("[Thread " + threadNum + "] execution time : " + timeDiff + " ms");
        }
    
        public int getCnt () {
            return this.primeCnt;
        }
    }
}