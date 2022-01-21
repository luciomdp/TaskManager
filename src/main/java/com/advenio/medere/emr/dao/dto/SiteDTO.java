package com.advenio.medere.emr.dao.dto;

import java.math.BigInteger;

public class SiteDTO {
	
	protected BigInteger site;
	protected boolean active;
	protected String url; 
	
	protected String logofilename;
	protected String logofilehash;
	protected String logoreportfilename;

	protected String apptitle;
	protected String faviconpath;
	protected String medereuuid;
	
	protected String companyaddress;
	protected String companyemail;
	protected String companyname;
	protected String companytelephone;
	protected String companywebsite;
	protected boolean defaultsite;

	protected BigInteger language;
	
	// WebAppointment properties
	protected String webappointmentsurl;
	protected boolean showcoveragewarning;
	protected String webapptitle;
	protected BigInteger documenttype;
	protected BigInteger city;
	protected BigInteger regionalsettings;
	protected boolean hiderequestprescriptions;
	protected boolean hidelocationdetails;
	
	protected String totemurl;
	protected BigInteger totemuser;
	
	protected String anesthesiaappurl;
	
	protected String patientcallerurl;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogofilename() {
		return logofilename;
	}

	public void setLogofilename(String logofilename) {
		this.logofilename = logofilename;
	}

	public String getLogofilehash() {
		return logofilehash;
	}

	public void setLogofilehash(String logofilehash) {
		this.logofilehash = logofilehash;
	}

	public String getLogoreportfilename() {
		return logoreportfilename;
	}

	public void setLogoreportfilename(String logoreportfilename) {
		this.logoreportfilename = logoreportfilename;
	}

	public String getApptitle() {
		return apptitle;
	}

	public void setApptitle(String apptitle) {
		this.apptitle = apptitle;
	}

	public String getFaviconpath() {
		return faviconpath;
	}

	public void setFaviconpath(String faviconpath) {
		this.faviconpath = faviconpath;
	}

	public String getMedereuuid() {
		return medereuuid;
	}

	public void setMedereuuid(String medereuuid) {
		this.medereuuid = medereuuid;
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

	public BigInteger getLanguage() {
		return language;
	}

	public void setLanguage(BigInteger language) {
		this.language = language;
	}

	public String getWebappointmentsurl() {
		return webappointmentsurl;
	}

	public void setWebappointmentsurl(String webappointmentsurl) {
		this.webappointmentsurl = webappointmentsurl;
	}

	public boolean isShowcoveragewarning() {
		return showcoveragewarning;
	}

	public void setShowcoveragewarning(boolean showcoveragewarning) {
		this.showcoveragewarning = showcoveragewarning;
	}

	public String getWebapptitle() {
		return webapptitle;
	}

	public void setWebapptitle(String webapptitle) {
		this.webapptitle = webapptitle;
	}

	public BigInteger getDocumenttype() {
		return documenttype;
	}

	public void setDocumenttype(BigInteger documenttype) {
		this.documenttype = documenttype;
	}

	public BigInteger getCity() {
		return city;
	}

	public void setCity(BigInteger city) {
		this.city = city;
	}

	public BigInteger getRegionalsettings() {
		return regionalsettings;
	}

	public void setRegionalsettings(BigInteger regionalsettings) {
		this.regionalsettings = regionalsettings;
	}

	public boolean isHiderequestprescriptions() {
		return hiderequestprescriptions;
	}

	public void setHiderequestprescriptions(boolean hiderequestprescriptions) {
		this.hiderequestprescriptions = hiderequestprescriptions;
	}

	public boolean isHidelocationdetails() {
		return hidelocationdetails;
	}

	public void setHidelocationdetails(boolean hidelocationdetails) {
		this.hidelocationdetails = hidelocationdetails;
	}

	public String getTotemurl() {
		return totemurl;
	}

	public void setTotemurl(String totemurl) {
		this.totemurl = totemurl;
	}

	public BigInteger getTotemuser() {
		return totemuser;
	}

	public void setTotemuser(BigInteger totemuser) {
		this.totemuser = totemuser;
	}

	public String getAnesthesiaappurl() {
		return anesthesiaappurl;
	}

	public void setAnesthesiaappurl(String anesthesiaappurl) {
		this.anesthesiaappurl = anesthesiaappurl;
	}

	public String getPatientcallerurl() {
		return patientcallerurl;
	}

	public void setPatientcallerurl(String patientcallerurl) {
		this.patientcallerurl = patientcallerurl;
	}

	
}