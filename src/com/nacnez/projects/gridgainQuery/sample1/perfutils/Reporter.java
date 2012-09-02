package com.nacnez.projects.gridgainQuery.sample1.perfutils;

public interface Reporter {

	void collect(TimedTaskOutput output);
	
	void report() throws Exception;
}
