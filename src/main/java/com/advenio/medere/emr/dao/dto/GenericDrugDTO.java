package com.advenio.medere.emr.dao.dto;

import java.math.BigInteger;

public class GenericDrugDTO {

	private BigInteger genericdrug;
    private String name;
	private String observations;
	private Integer maxdaystreatmentduration;
	private boolean preventoutofleveldosage;
	private boolean composed;
	private String commercialnames;
	private String therapeuticalgroup;
	private String therapeuticalsubgroup;
	private String dosageinformation;
	private String routes;
	private String adverseeffects;
	private String therapeuticcomments;
	private String additionalobservations;

	
	public GenericDrugDTO() {
	}
	
	public BigInteger getGenericDrug() {
		return genericdrug;
	}

	public void setGenericDrug(BigInteger genericdrug) {
		this.genericdrug = genericdrug;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Integer getMaxdaystreatmentduration() {
		return maxdaystreatmentduration;
	}

	public void setMaxdaystreatmentduration(Integer maxdaystreatmentduration) {
		this.maxdaystreatmentduration = maxdaystreatmentduration;
	}

	public BigInteger getGenericdrug() {
		return genericdrug;
	}

	public void setGenericdrug(BigInteger genericdrug) {
		this.genericdrug = genericdrug;
	}

	public boolean getPreventoutofleveldosage() {
		return preventoutofleveldosage;
	}

	public void setPreventoutofleveldosage(boolean preventoutofleveldosage) {
		this.preventoutofleveldosage = preventoutofleveldosage;
	}

	public String getCommercialnames() {
		return commercialnames;
	}

	public void setCommercialnames(String commercialnames) {
		this.commercialnames = commercialnames;
	}

	public String getTherapeuticalgroup() {
		return therapeuticalgroup;
	}

	public void setTherapeuticalgroup(String therapeuticalgroup) {
		this.therapeuticalgroup = therapeuticalgroup;
	}

	public String getTherapeuticalsubgroup() {
		return therapeuticalsubgroup;
	}

	public void setTherapeuticalsubgroup(String therapeuticalsubgroup) {
		this.therapeuticalsubgroup = therapeuticalsubgroup;
	}

	public String getDosageinformation() {
		return dosageinformation;
	}

	public void setDosageinformation(String dosageinformation) {
		this.dosageinformation = dosageinformation;
	}

	public String getRoutes() {
		return routes;
	}

	public void setRoutes(String routes) {
		this.routes = routes;
	}

	public String getAdverseeffects() {
		return adverseeffects;
	}

	public void setAdverseeffects(String adverseeffects) {
		this.adverseeffects = adverseeffects;
	}

	public String getTherapeuticcomments() {
		return therapeuticcomments;
	}

	public void setTherapeuticcomments(String therapeuticcomments) {
		this.therapeuticcomments = therapeuticcomments;
	}

	public String getAdditionalobservations() {
		return additionalobservations;
	}

	public void setAdditionalobservations(String additionalobservations) {
		this.additionalobservations = additionalobservations;
	}

	public boolean isComposed() {
		return composed;
	}

	public void setComposed(boolean composed) {
		this.composed = composed;
	}

}
