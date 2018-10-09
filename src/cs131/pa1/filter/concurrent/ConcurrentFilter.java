package cs131.pa1.filter.concurrent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected LinkedBlockingQueue<String> input;
	protected LinkedBlockingQueue<String> output;
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public Filter getNext() {
		return next;
	}
	
	public void process(){
	
		String done="";
		while(!done.equals("XXXYYYZZZPOISINPILL") && !isDone()){
			try {
				done = input.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(done.equals("XXXYYYZZZPOISINPILL")) {
				break;
			}
			
			String processedLine = processLine(done);
			if (processedLine != null){
				output.add(processedLine);
			}
		}
	}
	
	
	@Override
	//checks if the current thread has been interrupted.  Sometimes, the thread can still run after being interrupted
	public boolean isDone() {
		if(getClassThread().isInterrupted()) {
			return true;
		}
		return false;
	}
	
	public void run() {
		process();
		if(output==null)
			output=new LinkedBlockingQueue<String>();
		output.add("XXXYYYZZZPOISINPILL");
	}
	
	//gets the current running thread for that object
	public Thread getClassThread() {
		return Thread.currentThread();
	}
	protected abstract String processLine(String line);
	
}
