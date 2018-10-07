package cs131.pa1.filter.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Message;

public class GrepFilter extends ConcurrentFilter {
	private String toFind;
	
	public GrepFilter(String line) throws Exception {
		super();
		String[] param = line.split(" ");
		if(param.length > 1) {
			toFind = param[1];
		} else {
			System.out.printf(Message.REQUIRES_PARAMETER.toString(), line);
			throw new Exception();
		}
	}
	
	public String processLine(String line) {
		if(line.contains(toFind)) {
			return line;
		} else {
			return null;
		}
	}
	
	public void process(){
		String done = "";
//		System.out.println("in process");
//		while (!isDone()){
		while(!done.equals("XXXYYYZZZPOISINPILL")) {
//			System.out.println("In the loop");
			try {
				done = input.take();
//				System.out.println("In the 2nd loop");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if(isDone()) {
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
	public void run() {
		process();
		if(output==null)
			output=new LinkedBlockingQueue<String>();
		output.add("XXXYYYZZZPOISINPILL");
		
	}

	
}
