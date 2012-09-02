package com.nacnez.projects.gridgainQuery.sample1.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.gridgain.grid.cache.affinity.GridCacheAffinityKey;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

public class Person {

    @GridCacheQuerySqlField(unique = true)
	private String uniqueId;

    @GridCacheQuerySqlField(index = false)
    private String firstName;

    @GridCacheQuerySqlField(index = false)
    private String lastName;
	
    @GridCacheQuerySqlField
    private Date dateOfBirth;
    
    @GridCacheQuerySqlField
	private String gender;
    
    @GridCacheQuerySqlField
	private BigDecimal income;
    
	@GridCacheQuerySqlField(name="city")
	private String city;

    private transient GridCacheAffinityKey<String> key;

    private Address address;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	
    public GridCacheAffinityKey<String> key() {
        if (key == null)
            key = new GridCacheAffinityKey<String>(uniqueId);

        return key;
    }
	
}
