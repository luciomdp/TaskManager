package com.advenio.medere.emr.objects;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.advenio.medere.objects.BaseObjectI18N;
import com.advenio.medere.objects.Language;

@Entity
@Immutable
@Cacheable
@Cache(region = "entitytags", usage = CacheConcurrencyStrategy.READ_ONLY)
public class EntityTag extends BaseObjectI18N {

	private static final long serialVersionUID = -6576972544693417816L;

	public enum EEntityTag {

		PHYSICIAN(1), NURSE(2), PATIENT(3), PARAMEDIC(4), THIS(6), PAYER(7), SUPPLIER(8), PHARMACY(9), ADMINISTRATIVE(10),AUDITOR(11);

		private long tag;

		private EEntityTag(long value) {
			this.tag = value;
		}

		public long getTag() {
			return tag;
		}
	}

	protected Long entityTag;
	protected String name;
	protected Set<EntityTagI18n> i18ncodes;

	public EntityTag() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getEntityTag() {
		return entityTag;
	}

	public void setEntityTag(Long entityTag) {
		this.entityTag = entityTag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Transient
	protected Long getID() {
		return entityTag;
	}

	@Override
	@Transient
	public String getDescription(Language lang) {
		return findDescription(i18ncodes, lang);
	}

	@OneToMany
	@Immutable
	@JoinColumn(name = "entityTag",updatable=false)
	@Cache(region = "entitytagi18ns", usage = CacheConcurrencyStrategy.READ_ONLY)
	@LazyCollection(LazyCollectionOption.FALSE)
	public Set<EntityTagI18n> getI18ncodes() {
		return i18ncodes;
	}

	public void setI18ncodes(Set<EntityTagI18n> i18ncodes) {
		this.i18ncodes = i18ncodes;
	}

}
