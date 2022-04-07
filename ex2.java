import java.util.concurrent.atomic.AtomicInteger;

class Ex2 extends Thread {
    private AtomicInteger sum = new AtomicInteger();
    int i;

    Ex2(int i) {
        this.i= i;
    }

    @Override
    public void run() {
        for (int k = 0; k <= 1000; k++) {
            sum.addAndGet(i+k);
        }
    }
    public int getSum() {
        return sum.intValue();
    }
} 

public class ex2 {

    private static final int THREAD_NUM = 10;

    public static void main(String[] args) {
        int[] int_arr = new int [10000];
        Ex2[] threadPool = new Ex2[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            threadPool[i] = new Ex2(1000 * i);
            threadPool[i].start();
        }
        int result = 0;
        for (int i = 0; i < THREAD_NUM; i++) {
            result += threadPool[i].getSum();
        }
        System.out.println("sum=" + result +"\n");
    }
}
  