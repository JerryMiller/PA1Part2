package cs131.pa1.filter.concurrent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected LinkedBlockingQueue<String> input;
	protected LinkedBlockingQueue<String> output;
//	public String done="";
	
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
//		while (!isDone()){
		while(!done.equals("XXXYYYZZZPOISINPILL") && !isDone()){
//			System.out.println("In the loop");
			try {
				done = input.take();
//				System.out.println("In the 2nd loop");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if(!isDone()) {
			if(done.equals("XXXYYYZZZPOISINPILL")) {
				break;
			}
			
			String processedLine = processLine(done);
			if (processedLine != null){
//				System.out.println("In the 3rd if loop");
				output.add(processedLine);
			}
		}
	}
	
	
	@Override
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
	public Thread getClassThread() {
		return Thread.currentThread();
	}
	protected abstract String processLine(String line);
	
}
