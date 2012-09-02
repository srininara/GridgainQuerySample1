package com.nacnez.projects.gridgainQuery.sample1.model;

import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

public class Address {

	private String firstLine;
	private String secondLine;

	private String country;
	private int postCode;
	public String getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	public String getSecondLine() {
		return secondLine;
	}

	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}

 	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public int getPostCode() {
		return postCode;
	}

	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}
}
