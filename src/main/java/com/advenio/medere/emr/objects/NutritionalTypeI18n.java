package com.advenio.medere.emr.objects;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import com.advenio.medere.objects.I18n;

@Entity
@Immutable
@Cacheable
@Cache(region = "nutritionalTypei18n", usage = CacheConcurrencyStrategy.READ_ONLY)
public class NutritionalTypeI18n extends I18n implements Comparable<NutritionalTypeI18n>{

	private static final long serialVersionUID = -5550716548401672962L;
	protected Long nutritionalTypeI18n;
	
	public NutritionalTypeI18n(){
		
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getNutritionalTypeI18n() {
		return nutritionalTypeI18n;
	}
	public void setNutritionalTypeI18n(Long nutritionalTypeI18n) {
		this.nutritionalTypeI18n = nutritionalTypeI18n;
	}

	@Override
	public int compareTo(NutritionalTypeI18n o) {
		return this.description.compareTo(o.description);
	}
	
	@Override
	@Transient
	protected Long getID() {
		return nutritionalTypeI18n;
	}
	
}

