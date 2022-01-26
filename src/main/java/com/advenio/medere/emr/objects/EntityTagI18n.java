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
@Cache(region = "entitytagi18ns", usage = CacheConcurrencyStrategy.READ_ONLY)
public class EntityTagI18n extends I18n{

	private static final long serialVersionUID = -6900041420638576350L;

	protected Long entityTagI18n;

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getEntityTagI18n() {
		return entityTagI18n;
	}

	public void setEntityTagI18n(Long entityTagI18n) {
		this.entityTagI18n = entityTagI18n;
	}

	@Override
	@Transient
	protected Long getID() {
		return entityTagI18n;
	}

}
