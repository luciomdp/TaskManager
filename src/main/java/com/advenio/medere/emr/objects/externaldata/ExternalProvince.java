package com.advenio.medere.emr.objects.externaldata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.advenio.medere.objects.location.Province;

@Entity
public class ExternalProvince implements Serializable {

	private static final long serialVersionUID = -8673123347104133763L;

	@Id
	@OneToOne
	@JoinColumn(name = "province")
	private Province province;
	@Id
	private String externalCode;
	
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	
	
}
