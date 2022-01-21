package com.advenio.medere.rest.dto.location;

import java.math.BigInteger;

public class ProvinceDTO {

	protected BigInteger province;
	protected BigInteger country;
	protected String description;

	public ProvinceDTO(){

	}

	public BigInteger getProvince() {
		return province;
	}

	public void setProvince(BigInteger province) {
		this.province = province;
	}

	public BigInteger getCountry() {
		return country;
	}

	public void setCountry(BigInteger country) {
		this.country = country;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}