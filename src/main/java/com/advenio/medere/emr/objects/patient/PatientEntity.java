package com.advenio.medere.emr.objects.patient;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.advenio.medere.emr.objects.PersonEntity;


@Entity
@DiscriminatorValue(value="2")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PatientEntity extends PersonEntity{

	private static final long serialVersionUID = 7920479311347484717L;
	private String credentialPhotoFront;
	private String credentialPhotoBack;


	@Transient
	private BigDecimal calcPatientAge() {
		if (getBirthDate() == null) {
			return getAge();
		} else {
			return getAgeFromBirthDate();
		}
	}

	public String getCredentialPhotoFront() {
		return credentialPhotoFront;
	}

	public void setCredentialPhotoFront(String credentialPhotoFront) {
		this.credentialPhotoFront = credentialPhotoFront;
	}

	public String getCredentialPhotoBack() {
		return credentialPhotoBack;
	}

	public void setCredentialPhotoBack(String credentialPhotoBack) {
		this.credentialPhotoBack = credentialPhotoBack;
	}

	@Override
	@Transient
	public String getNameforInvoice() {
		return getNameforDisplay();
	}
}
