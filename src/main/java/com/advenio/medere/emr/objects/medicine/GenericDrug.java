package com.advenio.medere.emr.objects.medicine;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Cacheable
@Table( uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}		)
public class GenericDrug {

	private Long genericDrug;
	private String name;
	private String observations;
	private Integer maxDaysTreatmentDuration;
	private boolean preventOutOfLevelDosage;
	private boolean composed;
	private String commercialNames;
	private String therapeuticalGroup;
	private String therapeuticalSubgroup;
	private String dosageInformation;
	private String routes;
	private String adverseEffects;
	private String therapeuticComments;
	private String additionalObservations;

	public GenericDrug() {		
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getGenericDrug() {
		return genericDrug;
	}

	public void setGenericDrug(Long genericDrug) {
		this.genericDrug = genericDrug;
	}
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	@Column(nullable = true)	
	public Integer getMaxDaysTreatmentDuration() {
		return maxDaysTreatmentDuration;
	}

	public void setMaxDaysTreatmentDuration(Integer maxDaysTreatmentDuration) {
		this.maxDaysTreatmentDuration = maxDaysTreatmentDuration;
	}
	
	public boolean isPreventOutOfLevelDosage() {
		return preventOutOfLevelDosage;
	}

	public void setPreventOutOfLevelDosage(boolean preventOutOfLevelDosage) {
		this.preventOutOfLevelDosage = preventOutOfLevelDosage;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getCommercialNames() {
		return commercialNames;
	}

	public void setCommercialNames(String commercialNames) {
		this.commercialNames = commercialNames;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getRoutes() {
		return routes;
	}

	public void setRoutes(String routes) {
		this.routes = routes;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getAdverseEffects() {
		return adverseEffects;
	}

	public void setAdverseEffects(String adverseEffects) {
		this.adverseEffects = adverseEffects;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getTherapeuticComments() {
		return therapeuticComments;
	}

	public void setTherapeuticComments(String therapeuticComments) {
		this.therapeuticComments = therapeuticComments;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getAdditionalObservations() {
		return additionalObservations;
	}

	public void setAdditionalObservations(String additionalObservations) {
		this.additionalObservations = additionalObservations;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getDosageInformation() {
		return dosageInformation;
	}

	public void setDosageInformation(String dosageInformation) {
		this.dosageInformation = dosageInformation;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getTherapeuticalGroup() {
		return therapeuticalGroup;
	}

	public void setTherapeuticalGroup(String therapeuticalGroup) {
		this.therapeuticalGroup = therapeuticalGroup;
	}

	@Column(columnDefinition="VARCHAR") 
	public String getTherapeuticalSubgroup() {
		return therapeuticalSubgroup;
	}

	public void setTherapeuticalSubgroup(String therapeuticalSubgroup) {
		this.therapeuticalSubgroup = therapeuticalSubgroup;
	}

	@Transient
	public boolean hasAdditionalInfo() {
		boolean withoutInformation = ((commercialNames == null) && (routes == null) && (therapeuticalGroup == null) &&
									  (therapeuticalSubgroup == null) && (dosageInformation == null) && (adverseEffects == null) &&
									  (therapeuticComments == null) && (additionalObservations == null));
		return !withoutInformation;
	}

	public boolean isComposed() {
		return composed;
	}

	public void setComposed(boolean composed) {
		this.composed = composed;
	}	
	
/*
	private Collection<Interaction> interactions = new ArrayList<>();	

	
	public boolean validate(Disease disease, float dose, int numberOfTimes, float height) {
		boolean pediatricSearchOK = false;
		boolean adultSearchOK = false;
		
		if ((disease == null) || (height <= 0))  //Error in parameters
			 return false;
		
		if (pediatricDosages.isEmpty() && adultDosages.isEmpty())  //There aren't min/max dosages for this generic drug  
		     return true;
		
		if (!pediatricDosages.isEmpty()) {
			PediatricDosage dosage;
			Iterator<PediatricDosage> itr = pediatricDosages.iterator();
			do {
				dosage = itr.next();
				pediatricSearchOK = (height >= dosage.getFromHeight()) && (height <= dosage.getToHeight()) && (disease.getName().equalsIgnoreCase(dosage.getDisease().getName()));
			}
			while (itr.hasNext() && !pediatricSearchOK);
			if (pediatricSearchOK) 
				return dosage.validate(dose, numberOfTimes);		
		}
		
		if (!adultDosages.isEmpty()) {
			DailyDrugDosage dosage;
			Iterator<DailyDrugDosage> itr = adultDosages.iterator();
			do {
				dosage = itr.next();
				adultSearchOK = (disease.getName().equalsIgnoreCase(dosage.getDisease().getName()));
			}
			while (itr.hasNext() && !adultSearchOK);
			if (adultSearchOK) 
				return dosage.validate(dose, numberOfTimes);  		
		}
		return true;  //There aren't min/max dosages for this disease and height
	}

	public boolean validate(float dose, int numberOfTimes, float height) {
		boolean pediatricSearchOK = false;
		boolean adultSearchOK = false;
		
		if (height <= 0)  //Error in parameters
			 return false;
		
		if (pediatricDosages.isEmpty() && adultDosages.isEmpty())  //There aren't min/max dosages for this generic drug  
		     return true;
		
		if (!pediatricDosages.isEmpty()) {
			PediatricDosage dosage;
			Iterator<PediatricDosage> itr = pediatricDosages.iterator();
			do {
				dosage = itr.next();
				pediatricSearchOK = (height >= dosage.getFromHeight()) && (height <= dosage.getToHeight()) && (dosage.getDisease() == null);
			}
			while (itr.hasNext() && !pediatricSearchOK);
			if (pediatricSearchOK) 
				return dosage.validate(dose, numberOfTimes);		
		}
		
		if (!adultDosages.isEmpty()) {
			DailyDrugDosage dosage;
			Iterator<DailyDrugDosage> itr = adultDosages.iterator();
			do {
				dosage = itr.next();
				adultSearchOK = (dosage.getDisease() == null);
			}
			while (itr.hasNext() && !adultSearchOK);
			if (adultSearchOK) 
				return dosage.validate(dose, numberOfTimes);  		
		}
		return true;  //There aren't min/max dosages for this height
	}

	public boolean validate(Disease disease, float dose, int numberOfTimes) {
		boolean adultSearchOK = false;
		
		if (disease == null)  //Error in parameters
			 return false;
				
		if (!adultDosages.isEmpty()) {
			DailyDrugDosage dosage;
			Iterator<DailyDrugDosage> itr = adultDosages.iterator();
			do {
				dosage = itr.next();
				adultSearchOK = (disease.getName().equalsIgnoreCase(dosage.getDisease().getName()));
			}
			while (itr.hasNext() && !adultSearchOK);
			if (adultSearchOK) 
				return dosage.validate(dose, numberOfTimes);  		
		}
		return true;  //There aren't min/max dosages for this disease
	}
	
	public boolean validate(float dose, int numberOfTimes) {
		boolean adultSearchOK = false;
					
		if (!adultDosages.isEmpty()) {
			DailyDrugDosage dosage;
			Iterator<DailyDrugDosage> itr = adultDosages.iterator();
			do {
				dosage = itr.next();
				adultSearchOK = (dosage.getDisease() == null);
			}
			while (itr.hasNext() && !adultSearchOK);
			if (adultSearchOK) 
				return dosage.validate(dose, numberOfTimes);  		
		}
		return true;  //There aren't min/max dosages for this disease
	}
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="interactions")
	@LazyCollection(LazyCollectionOption.TRUE)		
	public Collection<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(Collection<Interaction> interactions) {
		this.interactions = interactions;
	}
**/

}
