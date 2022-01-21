package com.advenio.medere.emr.objects.externaldata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.advenio.medere.objects.DocumentType;

@Entity
public class ExternalDocumentType implements Serializable{

	private static final long serialVersionUID = -7179980640212240186L;

	@Id
	@ManyToOne
	@JoinColumn(name = "documentType")
	private DocumentType documentType;
	@Id
	private String externalCode;
	
	public DocumentType getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	
}
