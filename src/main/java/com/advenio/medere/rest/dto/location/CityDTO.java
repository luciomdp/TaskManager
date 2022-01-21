package com.advenio.medere.rest.dto.location;

import java.math.BigInteger;

public class CityDTO {

	protected BigInteger city;
	protected BigInteger province;
	protected String postalcode;
	protected String description;

	public CityDTO(){

	}

	public BigInteger getCity() {
		return city;
	}

	public void setCity(BigInteger city) {
		this.city = city;
	}

	public BigInteger getProvince() {
		return province;
	}

	public void setProvince(BigInteger province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalcode;
	}

	public void setPostalCode(String postalCode) {
		this.postalcode = postalCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}