package proj2.prob3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class LimitedResource {
    private BlockingQueue<String> resourceQueue;

    LimitedResource(int capacity) {
        this.resourceQueue = new ArrayBlockingQueue<>(capacity);
    }

    public void pushQueue(String item){
        try {
            resourceQueue.put(item);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String popQueue(){
        String result = "";
        try {
            result = resourceQueue.take();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}

class SampleQueueing extends Thread {

    private LimitedResource resource;
    private String threadNumber;

    SampleQueueing(LimitedResource lr, int num) {
        this.resource = lr;
        this.threadNumber = "Thread Number " + num; 
    }

    @Override
    public void run() {
        try {
            System.out.println(threadNumber + " is waiting to be pushed to the resource!");
            Thread.sleep((long)(Math.random() * 15000));
            resource.pushQueue(threadNumber);
            System.out.println(threadNumber + " has been pushed to the resource!");
            Thread.sleep((long)(Math.random() * 10000));
            System.out.println(threadNumber + " is waiting for popped out!");
            String result = resource.popQueue();
            System.out.println(result + " popped from resource!");
            
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ex1 {
    //ArrayBlockingQueue and BlockingQueue, use put(), take() methods

    private static final int THREAD_NUM = 10;
    private static final int QUEUE_LEN = 5;
    public static void main(String[] args) {
        
        SampleQueueing[] queueList = new SampleQueueing[THREAD_NUM];
        LimitedResource limitedResource = new LimitedResource(QUEUE_LEN);
        for (int i = 0; i<THREAD_NUM; i++) {
            queueList[i] = new SampleQueueing(limitedResource, i+1);
            queueList[i].start();
        }
        for (int i = 0; i<THREAD_NUM; i++) {
            try {
                queueList[i].join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
