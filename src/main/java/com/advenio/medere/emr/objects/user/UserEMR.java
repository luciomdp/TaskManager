package com.advenio.medere.emr.objects.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.advenio.medere.emr.objects.MedereEntity;
import com.advenio.medere.objects.user.User;

@Entity
@SQLDelete(sql = "UPDATE _USER SET deleted=CURRENT_DATE WHERE _user=?")
@DiscriminatorValue(value = "2")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class UserEMR extends User {

	public enum Profiles {
		PROFILE_ADMINISTRATOR(1), PROFILE_NURSE(2), PROFILE_KINESIOLOGIST(3), PROFILE_DOCTOR(4), PROFILE_PHARMACIST(5),
		PROFILE_NUTRITIONIST(6), PROFILE_PATHOLOGIST(7), PROFILE_FACTURIST(8), PROFILE_SECRETARY_AMBULATORY(12), PROFILE_PATIENT(15),
		PROFILE_FACTURIST_SUPERVISION(17), PROFILE_SECRETARY_HOSPITALIZATION(18), PROFILE_RECEPTIONIST(19), PROFILE_SECRETARY_GUARD(20),
		PROFILE_EXTERNAL_SECRETARY(21), PROFILE_AMBULATORY_SUPERVISION(22), PROFILE_AUDITOR(23), PROFILE_NURSERY_NURSE(24), 
		PROFILE_SECRETARY_SURGERY(25), PROFILE_STATISTICS(26), PROFILE_ADMINISTRATIVE_SURGERY(27);

		private long value;

		private Profiles(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}

	protected String externalID;
	private String pictureRoute;
	private Integer maxInactiveInterval;
	protected MedereEntity medereEntity;

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	@Transient
	public String getFullName() {
		return String.format("%s %s", getLastName(), getFirstName());
	}

	
	public Integer getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(Integer maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public void setMedereEntity(MedereEntity medereEntity) {
		this.medereEntity = medereEntity;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medereEntity")
	public MedereEntity getMedereEntity() {
		return medereEntity;
	}	
	
	@Transient
	public boolean hasEntityTag(long tagId) {
		if (medereEntity == null) {
			return false;
		}
		
		if (medereEntity.getTags().stream().filter(e-> e.getEntityTag().longValue() == tagId).findAny().isPresent()) {
			return true;
		}
		
		return false;
	}

	public String getPictureRoute() {
		return pictureRoute;
	}

	public void setPictureRoute(String pictureRoute) {
		this.pictureRoute = pictureRoute;
	}
}
