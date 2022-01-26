package com.advenio.medere.emr.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.advenio.medere.objects.BaseObject;
import com.advenio.medere.objects.DocumentType;
import com.advenio.medere.objects.location.City;
import com.advenio.medere.objects.site.Site;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "medereEntityType", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "1")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public abstract class MedereEntity extends BaseObject {

	private static final long serialVersionUID = -8940859771485723593L;

	protected Long medereEntity;
	protected String address;
	protected String phoneNumber;
	protected String externalID;
	protected DocumentType documentType;
	protected String document;
	protected City city;
	protected String email;
	protected boolean active;
	protected String entityPhoto;
	protected String mobilePhone;
	
	protected Set<EntityTag> tags = new HashSet<>();

	protected Site site;
	
	public MedereEntity() {

	}

	@Override
	@Transient
	protected Long getID() {
		return medereEntity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getMedereEntity() {
		return medereEntity;
	}

	public void setMedereEntity(Long medereEntity) {
		this.medereEntity = medereEntity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "documentType")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "city")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(joinColumns = { @JoinColumn(name = "medereEntity") }, inverseJoinColumns = {
			@JoinColumn(name = "entityTag") })
	public Set<EntityTag> getTags() {
		return tags;
	}

	public void setTags(Set<EntityTag> tags) {
		this.tags = tags;
	}

	public String getEntityPhoto() {
		return entityPhoto;
	}

	public void setEntityPhoto(String entityPhoto) {
		this.entityPhoto = entityPhoto;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	@Transient
	public abstract String getNameforDisplay();
	
	@Transient
	public abstract String getNameforInvoice();

	@ManyToOne(optional = true,fetch = FetchType.LAZY)
	@JoinColumn(name = "site")
	@NotAudited
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
	
}
