package com.nacnez.projects.gridgainQuery.sample1.perfutils;

public class TimedTaskOutput {
	private String taskName;
	private long executionTime;
	
	public TimedTaskOutput() {
		
	}
	
	public TimedTaskOutput(String taskName, long executionTime) {
		super();
		this.taskName = taskName;
		this.executionTime = executionTime;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
}
