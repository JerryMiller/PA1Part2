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
			
			String processedLine = processLine(done);
			if (processedLine != null){
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
