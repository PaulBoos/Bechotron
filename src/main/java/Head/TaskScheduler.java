package Head;

import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TaskScheduler {
	
	public static BotInstance botInstance;
	Task head;
	Thread taskThread;
	
	public TaskScheduler() {
		(taskThread = new Thread(this::loop)).start();
	}
	
	public void addTask(Task task) {
		if(head == null) {
			head = task;
			if(taskThread.getState() == Thread.State.TERMINATED) (taskThread = new Thread(this::loop)).start();
		}
		else if(head.getExecutionTime() <= task.getExecutionTime()) head.addTail(task);
		else head = task.addTail(head);
	}
	
	public Task getHead() {
		return head;
	}
	
	private void loop() {
		try {
			while(true) {
				if(head == null) return;
				if(head.check(true)) {
					head = head.tail;
					System.gc();
				}
				Thread.sleep(10000);
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static class Task {
		
		long executionTime;
		Lambda code;
		Task tail;
		
		public Task(Instant instant, Lambda code) {
			this.code = code;
			executionTime = instant.getEpochSecond();
			System.out.println("New Task for " + instant.toString().replace("T"," @ ").replace("Z",""));
		}
		
		public Task(long epochSeconds, Lambda code) {
			this.code = code;
			executionTime = epochSeconds;
			System.out.println("New Task for " + Instant.ofEpochSecond(epochSeconds).toString().replace("T"," @ ").replace("Z",""));
		}
		
		boolean check(boolean execute) {
			if(execute) {
				if(Instant.now().getEpochSecond() >= executionTime) {
					code.execute();
					return true;
				} else return false;
			} else {
				return Instant.now().getEpochSecond() >= executionTime;
			}
		}
		
		Task addTail(Task newTail) {
			if(tail == null) tail = newTail;
			else if(tail.getExecutionTime() <= newTail.getExecutionTime()) tail.addTail(newTail);
			else tail = newTail.addTail(tail);
			return this;
		}
		
		public long getExecutionTime() {
			return executionTime;
		}
		
	}
	
	public interface Lambda {
		
		void execute();
		
	}
	
}
