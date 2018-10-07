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
//			super.process();
			nextprocess();
//			String line = linecount + " " + wordcount + " " + charcount;
//			output.add(line);

		}
		
	}
	public void nextprocess(){
		String done="";
//		while (!isDone()){
		while(!done.equals("XXXYYYZZZPOISINPILL") && !isDone() ){
			
			try {
				done = input.take();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if(!isDone()) {
			
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
//			String[] wct = line.split(" ");
//			wordcount += wct.length;
//			String[] cct = line.split("|");
//			charcount += cct.length;
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
