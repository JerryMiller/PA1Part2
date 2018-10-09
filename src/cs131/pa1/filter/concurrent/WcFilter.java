package cs131.pa1.filter.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() {
		
		if(isDone()) {
			output.add(processLine(null));
		} else {
			nextprocess();

		}
		
	}
	public void nextprocess(){
		String done="";
		//check for the poisinPill and if the thread is interrupted but still running (which can happen)
		while(!done.equals("XXXYYYZZZPOISINPILL") && !isDone() ){
			
			try {
				done = input.take();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String processedLine = processLine(done);
			if (processedLine != null){
			
				output.add(processedLine);
			}
		}
	}
	
	public String processLine(String line) {
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		}
		
		if(line.equals("XXXYYYZZZPOISINPILL")) {
			return linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
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
