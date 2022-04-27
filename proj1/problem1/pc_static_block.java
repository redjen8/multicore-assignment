package proj1.problem1;

public class pc_static_block {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 1;

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        int counter = 0;
        int i;
        BlockThread.blockSize = NUM_END / NUM_THREADS;
        if (BlockThread.blockSize * NUM_THREADS != NUM_END) counter = 1;
        long startTime = System.currentTimeMillis();
        BlockThread[] threadArr = new BlockThread[NUM_THREADS];
        for (i=1; i<(1+NUM_THREADS); i++) {
            threadArr[i-1] = new BlockThread(i);
            threadArr[i-1].start();
        }
        try {
            for (i=0; i<NUM_THREADS; i++) {
                threadArr[i].join();
                counter += threadArr[i].getCnt();
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

    static class BlockThread extends Thread {

        static int blockSize;
        private int endNum;
        private int cnt = 0;
    
        BlockThread(int i) {
            this.endNum = i * blockSize;
        }
    
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = endNum-blockSize; i<endNum; i++) {
                if(pc_static_block.isPrime(i)) cnt++;
            }
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println("[Thread " + endNum/blockSize + "] execution time : " + timeDiff + " ms");
        }
    
        public int getCnt() {
            return this.cnt;
        }
    }
}
