package com.nacnez.projects.gridgainQuery.sample1.perfutils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReporter implements Reporter {

	private Map<String,List<TimedTaskOutput>> mappedOutputs = new HashMap<String,List<TimedTaskOutput>>();
	private String fileName;
	private boolean reportProgress;
	PrintWriter writer;
	
	
	public FileReporter(String fileName, boolean reportProgress) throws Exception {
		this.fileName = fileName;
		this.reportProgress = reportProgress;
		this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
	}
	
	public void collect(TimedTaskOutput output) {
		List<TimedTaskOutput> outputs = mappedOutputs.get(output.getTaskName()); 
		if (outputs == null) {
			mappedOutputs.put(output.getTaskName(), new ArrayList<TimedTaskOutput>());
			outputs = mappedOutputs.get(output.getTaskName());
		}
		outputs.add(output);
		if (reportProgress) {
			printTaskOutput(output);
		}
	}

	public void report() throws Exception {
		
		for(String taskName : mappedOutputs.keySet()) {
			int count = 0;
			long sum = 0;
			TimedTaskOutput currOutput = null;
			for (TimedTaskOutput output : mappedOutputs.get(taskName)) {
				count++;
				sum += output.getExecutionTime();
				currOutput = output;
				if(!reportProgress) {
					printTaskOutput(output);
				}
			}
			if (count>1) {
				printAverage(currOutput,sum,count);
			}
		}
		
		writer.close();
	}
	
	private void printAverage(TimedTaskOutput output, long sum, int count) {
		long average = sum/count;
		StringBuilder msgBuilder = new StringBuilder();
		msgBuilder.append(output.getTaskName());
		msgBuilder.append(" - Average time taken is ");
		msgBuilder.append(average);
		msgBuilder.append(" milli seconds for ");
		msgBuilder.append(count);
		msgBuilder.append(" number of runs");
		writer.println(msgBuilder.toString());
		writer.flush();
	}

	private void printTaskOutput(TimedTaskOutput output) {
		StringBuilder msgBuilder = new StringBuilder();
		msgBuilder.append(output.getTaskName());
		msgBuilder.append(" completed in ");
		msgBuilder.append(output.getExecutionTime());
		msgBuilder.append(" milli seconds.");
		writer.println(msgBuilder.toString());
		writer.flush();
	}


}
