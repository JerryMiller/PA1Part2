package cs131.pa1.filter.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		String done ="";
		//check for the poisinPill and if the thread is interrupted but still running (which can happen)
		while(!done.equals("XXXYYYZZZPOISINPILL") && !isDone()) {
			try {
				done = input.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(done.equals("XXXYYYZZZPOISINPILL")) {
				break;
			}
				
			processLine(done);
		}
	}
	
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}

	@Override
	public void run() {
		process();
		if(output==null)
			output=new LinkedBlockingQueue<String>();
		output.add("XXXYYYZZZPOISINPILL");
		
	}
}
