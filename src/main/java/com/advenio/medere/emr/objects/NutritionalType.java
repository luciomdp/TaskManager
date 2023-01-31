package com.advenio.medere.emr.objects;

import java.util.Collection;

import java.util.TreeSet;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import com.advenio.medere.objects.I18n;
import com.advenio.medere.objects.Language;

@Entity
@Immutable
@Cacheable
@Cache(region = "nutritionaltype", usage = CacheConcurrencyStrategy.READ_ONLY)
public class NutritionalType extends BaseObjectI18N {
	private static final long serialVersionUID = 7408951978253789241L;
	protected Long nutritionalType;
	protected Collection<NutritionalTypeI18n> i18ncodes = new TreeSet<NutritionalTypeI18n>();	

	public enum NutritionalTypes {
		NUTRITIONAL_RESTRICTIONS(1), 				
		NUTRITIONAL_QUANTITIES(2),
		NUTRITIONAL_PREFERENCES(3);
				
		private long value;
		private NutritionalTypes(long value) {
			this.value = value;
		}
		public long getValue(){
			return value;
		}
		public static NutritionalTypes getNutritionalType(long value){
			for(NutritionalTypes aft: NutritionalTypes.values()){
				if (aft.getValue() == value){
					return aft;
				}
			}
			return null;
		}		
	}		

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getNutritionalType() {
		return nutritionalType;
	}
	public void setNutritionalType(Long nutritionalType) {
		this.nutritionalType = nutritionalType;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true)
	@JoinColumn(name="nutritionalType")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cache(region = "nutritionalTypei18n", usage = CacheConcurrencyStrategy.READ_ONLY)
	//@OrderBy(value = "description asc, language asc")
	public Collection<NutritionalTypeI18n> getI18ncodes() {
		return i18ncodes;
	}

	public void setI18ncodes(Collection<NutritionalTypeI18n> i18ncodes) {
		this.i18ncodes = i18ncodes;
	}

	@Override
	@Transient
	public String getDescription(Language lang) {
		return findDescription(i18ncodes,lang);
	}	

	public NutritionalType() {
		super();
	}

	public NutritionalType(Long nutritionalType) {
		super();
		this.nutritionalType = nutritionalType;
	}
	@Override
	@Transient
	protected Long getID() {
		return nutritionalType;
	}
	
	@Transient
	public String toString() {
		for (I18n i18n : i18ncodes) {
			if (i18n.getLanguage().getLanguage().longValue() == 1) {
				return i18n.getDescription();
			}
		}
		return "";
	}
	@Override
	public String getDescription(long lang) {
		return findDescription(i18ncodes,lang);
	}
}
