import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;

interface Buffer{
	
	void BlockingPut(int value) throws InterruptedException;
	
	int BlockingGet() throws InterruptedException;
}

class Producer implements Runnable{
	private final SecureRandom generator ;
	private final Buffer simpleBuffer;
	
	public Producer(Buffer simpleBuffer ) {
		this.generator = new SecureRandom();
		this.simpleBuffer = simpleBuffer;
	}
	
	
	public void run() {
		int sum = 0;
		
		for(int i =0;i<=10;i++) {
			try {
				
				Thread.sleep(generator.nextInt(3000));
				simpleBuffer.BlockingPut(i);
				sum+=i;
				System.out.println("Sun value : " + sum);
			} catch (Exception e) {
			}
		}
		 System.out.printf(
	     "Producer done producing%nTerminating Producer%n");
	}
	
	
}

class Consumer implements Runnable{
	private final SecureRandom generator;
	private final Buffer simpleBuffer;
	
	Consumer(Buffer simpleBuffer){
		this.simpleBuffer = simpleBuffer;
		this.generator = new SecureRandom();
	}

	@Override
	public void run() {
		int sum = 0;
		
		for(int i =0;i<=10;i++) {
			
			try {
				Thread.sleep(generator.nextInt(3000));
				sum += simpleBuffer.BlockingGet();
				System.out.println("Sum is consumer" + sum);
			} catch (Exception e) {
				// TODO: handle exception
			}
			System.out.println("Consumer Task Done");
		}
		
	}
	
	
}

//Before applying wait and notifyAll it wass unsyc but now it is
//syncronized
class UnsynchronizedBuffer implements Buffer{
	
	private int buffer =-1;
	private boolean occupied = false;
	@Override
	public synchronized  void BlockingPut(int value) throws InterruptedException {
		
		buffer = value;
		/* Always invoke method wait in a loop that tests the condition the task is waiting on. It’s
 possible that a thread will reenter the runnable state (via a timed wait or another thread
 calling notifyAll) before the condition is satisfied. Testing the condition again ensures
 that the thread will not erroneously execute if it was notified early*/
		while(occupied) {
			System.out.println("producer is filling the details");
			display("Buffer full wait");	
			wait();
		}
		occupied = true;
		display("Producer writes " + buffer);
		notifyAll();
	}

	@Override
	public synchronized int BlockingGet() throws InterruptedException {
		
		while(!occupied) {
			System.out.println("Consumer reading");
			display("Buffer empty consumer waits");
			wait();
		}
		occupied = false;
		System.out.println("Consumer" + buffer);
		notifyAll();
		return buffer;
	}
	
	private void display(String operation) {
		System.out.println("Displaying =>" + operation);
	}
	
}
/*Class SynchronizedBuffer contains fields buffer (line 6) and occupied (line 7)—you
 must synchronize access to both fields to ensure that class SynchronizedBuffer is thread
 safe. Methods blockingPut (lines 10–31) and blockingGet (lines 34–54) are declared as
 synchronized—only one thread can call either of these methods at a time on a particular
 SynchronizedBuffer object. Field occupied is used to determine whether it’s the Produc
er’s or the Consumer’s turn to perform a task. This field is used in conditional expressions
 in both the blockingPut and blockingGet methods. If occupied is false, then buffer is
 empty, so the Consumer cannot read the value of buffer, but the Producer can place a val
ue into buffer. If occupied is true, the Consumer can read a value from buffer, but the
 Producer cannot place a value into buffer.*/
public class Main3 {
	public static void main(String[] args) {
	 
		UnsynchronizedBuffer uBuffer = new UnsynchronizedBuffer();
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute(new Producer(uBuffer));
		executor.execute(new Consumer(uBuffer));
		
		executor.shutdown();
		
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	
	}
}
