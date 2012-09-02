package com.nacnez.projects.gridgainQuery.sample1.business;

import java.util.Collection;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.typedef.G;

import com.nacnez.projects.gridgainQuery.sample1.model.Person;
import com.nacnez.projects.gridgainQuery.sample1.perfutils.FileReporter;
import com.nacnez.projects.gridgainQuery.sample1.perfutils.Reporter;
import com.nacnez.projects.gridgainQuery.sample1.perfutils.TimedTask;

public class GridTest {

	/** Grid instance. */
	private static Grid grid;

	private static final String CONFIG_PATH = "/home/narayasr/Potpourri/Projects/indigo-apps/GridgainQuerySample1/resources/spring-cache.xml";

	private static final String OUTPUT_FILENAME = "output.txt";

	private static final int QUERY_REPEATS = 10;
	
	private static final int NUMBER_OF_RECORDS = 500; 

	public static void main(String[] args) throws Exception {
		String fileName = OUTPUT_FILENAME;
		int noOfRecords = NUMBER_OF_RECORDS;
		if (args!=null && args.length>=2) {
			fileName = args[0];
			noOfRecords = Integer.parseInt(args[1]);
		} 
		new GridTest().doTest(fileName,noOfRecords);
	}

	public void doTest(String outputFileName,int noOfRecords) throws GridException, Exception {
		grid = G.start(CONFIG_PATH);
		Reporter reporter = new FileReporter(outputFileName,true);
		System.out.println("Reporter: " + reporter);
		System.out.println("Starting creation of " + noOfRecords );
		Collection<Person> data = createData(noOfRecords,reporter);
		System.out.println("Done creation and Starting Population");
		populateGrid(reporter, data);
		System.out.println("Done population and Starting Querying");
		executeQuery(reporter, QUERY_REPEATS);
		System.out.println("Done querying");

		reporter.report();
		grid.stopNodes(grid.predicate());
	}

	private void executeQuery(Reporter reporter, int queryRepeats)
			throws Exception {
		for (int i = 0; i < queryRepeats; i++) {
			TimedTask query = new TimedTask(
					"Query Grid", reporter) {
				@Override
				protected void doExecute() throws Exception {
					// new GridSearcher().getTotalCount(grid);
//					new GridSearcher().getHigherHalfSalariedPersons(grid);
//					new GridSearcher().getCountOfHigherHalfSalariedPersons(grid);
//					new GridSearcher().getCountOfPersonsFromBangalore(grid);
//					new GridSearcher().getPersonsFromBangalore(grid);
					new GridSearcher().getFirstTenHigherHalfSalariedPersons(grid);
				}
			};
			query.execute();
		}
	}

	private void populateGrid(Reporter reporter, Collection<Person> data)
			throws Exception {
		TimedTask populateGrid = new TimedTask("Fill Grid with data with " + data.size() + " records" , reporter) {
			private Collection<Person> p;

			@Override
			public void setData(Object data) {
				p = (Collection<Person>) data;
			}

			@Override
			protected void doExecute() throws Exception {
				new GridFiller().fillGridWithData(grid, p);
			}

		};

		populateGrid.setData(data);
		populateGrid.execute();
	}

	private Collection<Person> createData(int noOfRecords, Reporter reporter) throws Exception {
		final int noOfRecs = noOfRecords;
		TimedTask createData = new TimedTask("Create data", reporter) {
			private Collection<Person> p;

			@Override
			public Object getData() {
				return p;
			}

			@Override
			protected void doExecute() throws Exception {
				p = new GridFiller().createData(noOfRecs);
			}

		};
		createData.execute();
		Collection<Person> data = (Collection<Person>) createData.getData();
		return data;
	}

}
