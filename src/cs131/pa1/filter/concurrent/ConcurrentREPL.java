package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		Thread threader= null;
		HashMap<Integer, ConcurrentFilter> listFilters = new HashMap<>();
		HashMap<Integer, String> listCommands = new HashMap<>();
		HashMap<Integer, Thread> threadsList = new HashMap<>();
		int count = 1;
		Integer threadCounter=1;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			command = command.trim();
			if(command.equals("exit")) {
				break;
			}else if(command.contains("repl_jobs")) {
				if(!command.substring(0,9).equals("repl_jobs")) {
					System.out.println(Message.CANNOT_HAVE_INPUT.with_parameter(command));
				}else if(command.length()>10) {
					System.out.println(Message.CANNOT_HAVE_INPUT.with_parameter(command));
				}else if(command.equals("repl_jobs")) {
					for(Integer I : threadsList.keySet()) {
						if(threadsList.get(I).isAlive()) {
							System.out.println("\t" + I + ". " + listCommands.get(I));
						}
					}
				}
					
//					for(Integer i : listCommands.keySet()) {
//						if(listFilters.get(i).getClassThread().isAlive()) {
//							System.out.println("\t" + i + ". " + listCommands.get(i));
//						}
//					}
			}else if (command.contains("kill") && Arrays.asList(command.split(" ")).contains("kill")){
				String[] killArray = command.split(" ");
				if(!command.substring(0,4).equals("kill")) {
					System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(command));
				}else if(killArray.length<2) {
					System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));	
				}else if (command.length() > 4 && command.substring(0, 4).equals("kill")) {
					boolean numeric=true;
					try {
			            Double num = Double.parseDouble(killArray[1]);
					} catch (NumberFormatException e) {
						numeric = false;
					}
					if(!numeric) {
						System.out.print(Message.INVALID_PARAMETER.with_parameter(command));
					}else {
						Integer kill = Integer.parseInt(command.substring(command.length()-1, command.length()));
						ConcurrentFilter filter = listFilters.get(kill);
						while(filter!=null) {
							filter.getClassThread().interrupt();
							filter = (ConcurrentFilter) filter.getNext();
						}
						listFilters.remove(kill);
						listCommands.remove(kill);
					}

				}
			}else if(!command.equals("")) {
				//building the filters list from the command
				String fullCommand = command;
				boolean hasAmp = false;
				if(command.charAt(command.length()-1) == '&'){
					fullCommand = command.substring(0, command.length()-1);
					hasAmp = true;
				}
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(fullCommand);
				Integer threadCount = 1;
				if(filterlist!=null && hasAmp) {
					listFilters.put(count, filterlist);
					listCommands.put(count, command);
					count++;
				}
				while(filterlist != null) {
					threader = new Thread (filterlist);
					threader.start();
					if(threadCount.equals(1)) {
						threadsList.put(threadCounter, threader);
						threadCounter++;
						threadCount++;
					}
					if (!hasAmp && filterlist.getNext()==(null)) {
						try {
							threader.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
