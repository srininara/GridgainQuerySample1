package com.nacnez.projects.gridgainQuery.sample1.perfutils;

public abstract class TimedTask {

	private String taskName;
	
	private Reporter reporter;
	
	private long finishedTimeInMilliSecs;

	public TimedTask(String taskName,Reporter reporter) {
		this.taskName = taskName;
		this.reporter = reporter;
	}
	
	
	public void execute() throws Exception {
        long fillStartTime = System.currentTimeMillis();
        doExecute();
		finishedTimeInMilliSecs = (System.currentTimeMillis()-fillStartTime);
		pushDataToReporter();
	}
	
	
	
	private void pushDataToReporter() {
		TimedTaskOutput output = new TimedTaskOutput(taskName,finishedTimeInMilliSecs);
		reporter.collect(output);
	}

	protected abstract void doExecute() throws Exception;
	
	public void setData(Object data) {
		
	}
	
	public Object getData() {
		return null;
	}

}
