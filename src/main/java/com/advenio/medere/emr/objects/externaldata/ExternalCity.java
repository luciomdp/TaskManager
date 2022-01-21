package com.advenio.medere.emr.objects.externaldata;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExternalCity {

	protected String id;
	protected String cityName;
	protected Long city;
	
	public ExternalCity() {
		
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCity() {
		return city;
	}

	public void setCity(Long city) {
		this.city = city;
	}
	
	
}
