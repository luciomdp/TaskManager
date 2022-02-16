package com.advenio.medere.emr.objects;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.advenio.medere.objects.BaseObject;

@SuppressWarnings("serial")
@Entity
@Table(indexes = { @Index(name = "idx_ns_right", columnList = "ns_right"),
		@Index(name = "idx_ns_left", columnList = "ns_left")
})
@Cacheable
@Cache(region = "diseases", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Disease extends BaseObject implements Serializable{

	protected Long disease;
	protected String name;
	protected String CIE10;
	protected char gender;
	protected Long left;
	protected Long right;	
	protected Disease parentDisease;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getDisease() {
		return disease;
	}

	public void setDisease(Long disease) {
		this.disease = disease;
	}

	@Column(columnDefinition="text")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCIE10() {
		return CIE10;
	}

	public void setCIE10(String cIE10) {
		CIE10 = cIE10;
	}

	public Disease() {

	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	@Column(nullable=true, name="ns_left")	
	public Long getLeft() {
		return left;
	}

	public void setLeft(Long left) {
		this.left = left;
	}

	@Column(nullable=true,name="ns_right")	
	public Long getRight() {
		return right;
	}

	public void setRight(Long right) {
		this.right = right;
	}

	@ManyToOne(optional=true)
	@JoinColumn(name="parentDisease")
	public Disease getParentDisease() {
		return parentDisease;
	}

	public void setParentDisease(Disease parentDisease) {
		this.parentDisease = parentDisease;
	}

	@Transient
	public String getCodeAndName() {
		return String.format("%s-%s", CIE10, name);
	}

	@Transient
	@Override
	protected Long getID() {
		return disease;
	}
	
	@Transient
	public String toString() {
		return CIE10 + ": " + name;
	}
	
}
