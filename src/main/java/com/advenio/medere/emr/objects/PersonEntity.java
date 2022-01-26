package com.advenio.medere.emr.objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public abstract class PersonEntity extends MedereEntity{

	private static final long serialVersionUID = -7025784014269578167L;

	protected String firstName;
	protected String lastName;
	protected LocalDate birthDate;
	protected Character gender;
	protected String clinicHistoryID;
	protected BigDecimal age;
	protected String documentPhotoFront;
	protected String documentPhotoBack;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
		
	@Transient
	public BigDecimal getAgeFromBirthDate() {
		if (getBirthDate() != null) {
			Period p = Period.between(getBirthDate(), LocalDate.now());
			return new BigDecimal(p.getYears());
		}
		else {
			return null;
		}
	}
	
	@Transient
	public BigDecimal[] getAgeInYearsAndMonthsFromBirthDate() {
		BigDecimal[] ageInParts = new BigDecimal[2];
		if (getBirthDate() != null) {
			Period p = Period.between(getBirthDate(), LocalDate.now());
			ageInParts[0] = new BigDecimal(p.getYears());
			ageInParts[1] = new BigDecimal(p.getMonths());
		}
		return ageInParts;
	}
		
	@Transient
	public BigDecimal calcAge() {
		if (getBirthDate() == null) {
			return getAge();
		} else {
			return getAgeFromBirthDate();
		}
	}
	
	public Character getGender() {
		return gender;
	}
	public void setGender(Character gender) {
		this.gender = gender;
	}
	public String getClinicHistoryID() {
		return clinicHistoryID;
	}
	public void setClinicHistoryID(String clinicHistoryID) {
		this.clinicHistoryID = clinicHistoryID;
	}
	public BigDecimal getAge() {
		return age;
	}
	public void setAge(BigDecimal age) {
		this.age = age;
	}
	
	@Transient
	public String getNameAndLastName() {
		return String.format("%s %s", firstName, lastName);
	}

	@Transient
	public String getLastNameAndName() {
		return String.format("%s %s", lastName, firstName);
	}
	
	@Override
	@Transient
	public String getNameforDisplay() {
		return getNameAndLastName();
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s (%s)", lastName, firstName, medereEntity);
	}
	public String getDocumentPhotoFront() {
		return documentPhotoFront;
	}
	public void setDocumentPhotoFront(String documentPhotoFront) {
		this.documentPhotoFront = documentPhotoFront;
	}
	public String getDocumentPhotoBack() {
		return documentPhotoBack;
	}
	public void setDocumentPhotoBack(String documentPhotoBack) {
		this.documentPhotoBack = documentPhotoBack;
	}
	
}
