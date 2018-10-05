package cs131.pa1.filter.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		String done ="";
//		while(!input.isEmpty()) {
		while(!done.equals("XXXYYYZZZPOISINPILL")) {
			try {
				done = input.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if(input.isEmpty())
//					System.out.println("Empty");
//			if(isDone())
//				System.out.println("done");
			
			if(done.equals("XXXYYYZZZPOISINPILL")) {
//			if(isDone()) {
				break;
			}
				
			processLine(done);
		}
//		if(isDone())
//			System.out.println("DONER");
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
