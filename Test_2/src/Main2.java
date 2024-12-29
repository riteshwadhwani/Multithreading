import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;

class SimpleArray{
	private SecureRandom generator = new SecureRandom();
	private int[] arr;
	private int size;
	private int writeIndex;
	
	SimpleArray(int size){
		this.size = size;
		this.arr = new int[size];
	}
	
	public synchronized void  add(int value) {
		int position = this.writeIndex;
		if(position > size-1) {
			return;
		}
		
		try {
			Thread.sleep(generator.nextInt(500));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		arr[position] = value;
		++writeIndex;
		
		System.out.println("Thread -> " + Thread.currentThread().getName()+ " " + value + " " + position);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.arr.toString();
	}
	
}

class ArrayWriter implements Runnable{
	private final SimpleArray array ;
	private final int startValue;
	
	public ArrayWriter(SimpleArray array, int startValue) {
		this.array = array;
		this.startValue = startValue;
	}
	
	@Override
	public void run() {
		for(int i =0;i<startValue+3 ;i++) {
			this.array.add(i);
		}
	}
	
}


public class Main2 {
	public static void main(String[] args) {
		SimpleArray array = new SimpleArray(6);
		
		ArrayWriter writer1 = new ArrayWriter(array, 1);
		ArrayWriter writer2 = new ArrayWriter(array, 1);
		
		ExecutorService executor = Executors.newCachedThreadPool();
		//Will face the Array Index Out Of Bounds Exception
		executor.execute(writer1);
		executor.execute(writer2);
		
		executor.shutdown();
		
		
		try {
			boolean taskEnded = 
					executor.awaitTermination(1, TimeUnit.MINUTES);
			
			if(taskEnded) {
				System.out.println("SimpleArray");
				System.out.println(array);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
}
