package proj1.problem1;

class BlockThread extends Thread {

    private int blockSize = 50000;
    private int endNum;
    private int cnt = 0;

    BlockThread(int i, int blockSize) {
        this.endNum = i;
        this.blockSize = blockSize;
    }

    @Override
    public void run() {
        for (int i = endNum-blockSize; i<endNum; i++) {
            if(pc_static_block.isPrime(i)) cnt++;
        }
    }

    public int getCnt() {
        return this.cnt;
    }
}

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
        long startTime = System.currentTimeMillis();
        BlockThread[] threadArr = new BlockThread[NUM_THREADS];
        int BLOCK_SIZE = NUM_END / NUM_THREADS;
        for (i=1; i<(1+NUM_THREADS); i++) {
            threadArr[i-1] = new BlockThread(BLOCK_SIZE * i, BLOCK_SIZE);
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

    static boolean isPrime(int x) {
        int i;
        if (x <= 1) return false;
        for (i=2; i<x; i++) {
            if (x%i == 0) return false;
        }
        return true;
    }
    
}
