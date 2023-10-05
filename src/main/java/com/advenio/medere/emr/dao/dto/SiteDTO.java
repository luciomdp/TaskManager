package com.advenio.medere.emr.dao.dto;

import java.math.BigInteger;

public class SiteDTO {
	
	private BigInteger site;
	private boolean active;
	private boolean defaultsite;
	private String apptitle;
	private String companyaddress;
	private String companyemail;
	private String companyname;
	private String companytelephone;
	private String companywebsite;
	private String medereuuid;
	

	public String getMedereuuid() {
		return medereuuid;
	}

	public void setMedereuuid(String medereuuid) {
		this.medereuuid = medereuuid;
	}

	public BigInteger getSite() {
		return site;
	}

	public void setSite(BigInteger site) {
		this.site = site;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getApptitle() {
		return apptitle;
	}

	public void setApptitle(String apptitle) {
		this.apptitle = apptitle;
	}

	public String getCompanyaddress() {
		return companyaddress;
	}

	public void setCompanyaddress(String companyaddress) {
		this.companyaddress = companyaddress;
	}

	public String getCompanyemail() {
		return companyemail;
	}

	public void setCompanyemail(String companyemail) {
		this.companyemail = companyemail;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCompanytelephone() {
		return companytelephone;
	}

	public void setCompanytelephone(String companytelephone) {
		this.companytelephone = companytelephone;
	}

	public String getCompanywebsite() {
		return companywebsite;
	}

	public void setCompanywebsite(String companywebsite) {
		this.companywebsite = companywebsite;
	}

	public boolean isDefaultsite() {
		return defaultsite;
	}

	public void setDefaultsite(boolean defaultsite) {
		this.defaultsite = defaultsite;
	}
}