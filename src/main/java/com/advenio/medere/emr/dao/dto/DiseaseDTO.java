package com.advenio.medere.emr.dao.dto;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DiseaseDTO {

	protected BigInteger disease;
	protected String name;
	protected String cie10;
	protected char gender;
	protected Long ns_left;
	protected Long ns_right;	
	protected BigInteger parentdisease;
	protected Integer cantchildren;
	
	public BigInteger getDisease() {
		return disease;
	}
	public void setDisease(BigInteger disease) {
		this.disease = disease;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCie10() {
		return cie10;
	}
	public void setCie10(String _cie10) {
		cie10 = _cie10;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public BigInteger getParentdisease() {
		return parentdisease;
	}
	public void setParentdisease(BigInteger parentDisease) {
		this.parentdisease = parentDisease;
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). 
            append(disease.longValue()).
            append(this.getClass().getName().hashCode()).
            toHashCode();
    }
	
	@Override
    public boolean equals(Object o) {
		DiseaseDTO mtype = (DiseaseDTO) o;
        if (o == this) return true;      
        if (o == null) return false;        
        return mtype.getDisease().longValue() == this.getDisease().longValue();
    }
	
	public Integer getCantchildren() {
		return cantchildren;
	}
	public void setCantchildren(Integer cantchildren) {
		this.cantchildren = cantchildren;
	}
	public String getCodeAndName() {
		return String.format("%s-%s", cie10, name); 
	}
	public Long getNs_left() {
		return ns_left;
	}
	public void setNs_left(Long ns_left) {
		this.ns_left = ns_left;
	}
	public Long getNs_right() {
		return ns_right;
	}
	public void setNs_right(Long ns_right) {
		this.ns_right = ns_right;
	}
	
}
