package com.advenio.medere.rest.dto.location;

import java.math.BigInteger;

public class CountryDTO {

	protected BigInteger country;
	protected BigInteger continent;
	protected Integer isocode;
	protected String description;

	public CountryDTO(){

	}

	public BigInteger getCountry() {
		return country;
	}

	public void setCountry(BigInteger country) {
		this.country = country;
	}

	public BigInteger getContinent() {
		return continent;
	}

	public void setContinent(BigInteger continent) {
		this.continent = continent;
	}

	public Integer getIsocode() {
		return isocode;
	}

	public void setIsocode(Integer isocode) {
		this.isocode = isocode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
