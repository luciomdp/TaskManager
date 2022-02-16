package com.advenio.medere.emr.objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Cacheable
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"description"})})
public class NutritionalItem implements Comparable<NutritionalItem> {
	
	protected Long nutritionalItem;
	protected NutritionalType nutritionalType;
	protected String description;
	boolean active;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getNutritionalItem() {
		return nutritionalItem;
	}
	public void setNutritionalItem(Long nutritionalItem) {
		this.nutritionalItem = nutritionalItem;
	}
	@ManyToOne(optional = false)
	@JoinColumn(name = "nutritionalType")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public NutritionalType getNutritionalType() {
		return nutritionalType;
	}
	public void setNutritionalType(NutritionalType nutritionalType) {
		this.nutritionalType = nutritionalType;
	}
	@Column(nullable = false)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nutritionalItem == null) ? 0 : nutritionalItem.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NutritionalItem other = (NutritionalItem) obj;
		if (nutritionalItem == null) {
			if (other.nutritionalItem != null)
				return false;
		} else if (!nutritionalItem.equals(other.nutritionalItem))
			return false;
		return true;
	}
	

	@Override
	public int compareTo(NutritionalItem other) {
		int difType =  this.getNutritionalType().getNutritionalType().compareTo(other.getNutritionalType().getNutritionalType());
        if(difType == 0) {
        	return this.getNutritionalItem().compareTo(other.getNutritionalItem());
        } else {
        	return difType;
        	
        }
	}
}
