package proj2.prob1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class ParkingGarage {
    private BlockingQueue<String> places;
    public ParkingGarage(int capacity) {
    	if (capacity < 0)
    		capacity = 0;
    	this.places = new ArrayBlockingQueue<>(capacity);
    }

    public void enter(String carName) { // enter parking garage
		// print();
		// System.out.println(carName + ": trying to enter");
        try {
        	places.put(carName);
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
		// System.out.println(carName + ": just entered");
		// print();
    }

    public void leave() { // leave parking garage
		// print();
		try {
			places.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// print();
    }

	private synchronized void print() {
		System.out.println("\nremaining : " + Integer.toString(places.remainingCapacity()));
	}
}
  
class Car extends Thread {
	private ParkingGarage parkingGarage;

	public Car(String name, ParkingGarage p) {
		super(name);
		this.parkingGarage = p;
		start();
	}

	private void tryingEnter()
	{
		System.out.println(getName()+": trying to enter");
	}

    private void justEntered()
    {
        System.out.println(getName()+": just entered");
    }
  
    private void aboutToLeave()
    {
        System.out.println(getName()+":                                     about to leave");
    }
  
    private void Left()
    {
        System.out.println(getName()+":                                     have been left");
    }
  
    public void run() {
    	while (true) {
        	try {
        		sleep((int)(Math.random() * 10000)); // drive before parking
        	} catch (InterruptedException e) {}
			
			tryingEnter();
			try {
				parkingGarage.enter(getName());
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			justEntered();

			try {
				sleep((int)(Math.random() * 20000)); // stay within the parking garage
			} catch (InterruptedException e) {}
			
			aboutToLeave();
			parkingGarage.leave();
			Left();
      	}
    }
  }
  
  
public class ParkingBlockingQueue {
    public static void main(String[] args){
      	ParkingGarage parkingGarage = new ParkingGarage(7);
      	for (int i=1; i<= 10; i++) {
        	Car c = new Car("Car "+i, parkingGarage);
      	}
    }
}
  