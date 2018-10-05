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
			super.process();
			output.add(linecount + " " + wordcount + " " + charcount);
		}
		
	}
	
	public String processLine(String line) {
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		}
		
		if(isDone()) {
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return ++linecount + " " + wordcount + " " + charcount;
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
		System.out.println("IN WC");
		process();
		System.out.println("out of WC");
		if(output==null)
			output=new LinkedBlockingQueue<String>();
		output.add("XXXYYYZZZPOISINPILL");
		
	}
}
