package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		while(!input.isEmpty() && !isDone()) {
//			if(input.isEmpty())
//					System.out.println("Empty");
//			if(isDone())
//				System.out.println("done");
			processLine(input.poll());
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
		done=true;
		
	}
}
