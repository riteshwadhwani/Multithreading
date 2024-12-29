import java.security.SecureRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
class Mythread implements Runnable{
	final private SecureRandom generator = new SecureRandom();
	private int randomTime;
	private String taskName;
	
	Mythread(String taskName){
		this.taskName = taskName;
		this.randomTime = this.generator.nextInt(5000);
	}
	
	
	@Override
	public void run() {
		try {
			System.out.println("in run" + taskName + " " + "Sleep Time" 
					+ randomTime);
						Thread.sleep(randomTime);
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(taskName + "done sleeping");
	}
}
public  class Main {
	
	public static void main(String[] args) {
		Mythread thread1 = new Mythread("thread1");
		Mythread thread2 = new Mythread("thread2");
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute(thread1);
		executor.execute(thread2);
		executor.shutdown();
	}
}
