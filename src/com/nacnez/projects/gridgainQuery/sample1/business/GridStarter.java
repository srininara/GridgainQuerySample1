package com.nacnez.projects.gridgainQuery.sample1.business;

import org.gridgain.grid.Grid;
import org.gridgain.grid.typedef.G;

public class GridStarter {


	/** Grid instance. */
	public static Grid grid;
	
	private static final String CONFIG_PATH = "/home/narayasr/Potpourri/Projects/indigo-apps/GridgainQuerySample1/resources/spring-cache.xml";
	
	public static void main(String[] args) throws Exception {
		grid = G.start(CONFIG_PATH);
	}
	
}
