package com.advenio.medere.emr.dao.dto;

import java.math.BigInteger;

public class NutritionalItemDTO {

	private BigInteger nutritionalitem;
    private String description;
    private String typedescription;
	private boolean active;
	
	public NutritionalItemDTO() {
	}

	public BigInteger getNutritionalitem() {
		return nutritionalitem;
	}

	public void setNutritionalitem(BigInteger nutritionalitem) {
		this.nutritionalitem = nutritionalitem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypedescription() {
		return typedescription;
	}

	public void setTypedescription(String typedescription) {
		this.typedescription = typedescription;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
