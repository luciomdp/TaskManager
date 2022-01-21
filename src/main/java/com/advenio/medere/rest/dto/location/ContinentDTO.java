package com.advenio.medere.rest.dto.location;

import java.math.BigInteger;

public class ContinentDTO {

	protected BigInteger continent;
	protected String description;

	public ContinentDTO(){

	}

	public BigInteger getContinent() {
		return continent;
	}

	public void setContinent(BigInteger continent) {
		this.continent = continent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}