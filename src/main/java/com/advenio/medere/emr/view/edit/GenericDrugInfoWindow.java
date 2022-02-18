package com.advenio.medere.emr.view.edit;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.advenio.medere.emr.objects.medicine.GenericDrug;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent("GenericDrugInfoWindow")
@Scope("prototype")
public class GenericDrugInfoWindow extends Dialog {

	private static final long serialVersionUID = 7096007017814504360L;
	@Autowired
	protected ISessionManager sessionManager;
	
	protected GenericDrug genericDrug;
	private List<GenericDrug> genericDrugList = null;

	private LabelHTML lblGenericDrugName;
	private LabelHTML lblTherapeuticalGroup;
	private LabelHTML lblTherapeuticalSubgroup;
	private LabelHTML lblDosageInformation;
	private LabelHTML lblRoutes;
	private LabelHTML lblAdverseEffects;
	private LabelHTML lblTherapeuticComments;
	private LabelHTML lblAdditionalObservations;
	private LabelHTML lblCommercialNames;
	
	private VerticalLayout fl;
	
	
	public GenericDrugInfoWindow(String caption, GenericDrug genericDrug){
		this.genericDrug = genericDrug;	
	}
	
	public GenericDrugInfoWindow(String caption, List<GenericDrug> genericDrugList){
	
		this.genericDrugList = genericDrugList;
		this.genericDrug = genericDrugList.get(0);

	}
	
	@PostConstruct
	protected void createCRUDControls() {
		
		lblGenericDrugName = new LabelHTML(sessionManager.getI18nMessage("Name"), genericDrug.getName().toUpperCase());
		lblGenericDrugName.setSizeFull();
		
		lblTherapeuticalGroup = new LabelHTML(sessionManager.getI18nMessage("TherapeuticalGroup"), genericDrug.getTherapeuticalGroup() == null ? "" : genericDrug.getTherapeuticalGroup().toString());
		lblTherapeuticalGroup.setSizeFull();
		
		lblTherapeuticalSubgroup = new LabelHTML(sessionManager.getI18nMessage("TherapeuticalSubgroup"), genericDrug.getTherapeuticalSubgroup() == null ? "" : genericDrug.getTherapeuticalSubgroup());
		lblTherapeuticalSubgroup.setSizeFull();
		
		lblTherapeuticComments = new LabelHTML(sessionManager.getI18nMessage("TherapeuticComments"), genericDrug.getTherapeuticComments() == null ? "" : genericDrug.getTherapeuticComments());
		lblTherapeuticComments.setSizeFull();

		lblDosageInformation = new LabelHTML(sessionManager.getI18nMessage("DosageInformation"), genericDrug.getDosageInformation() == null ? "" : genericDrug.getDosageInformation());
		lblDosageInformation.setSizeFull();

		lblRoutes = new LabelHTML(sessionManager.getI18nMessage("Routes"), genericDrug.getRoutes() == null ? "" : genericDrug.getRoutes());
		lblRoutes.setSizeFull();

		lblAdverseEffects = new LabelHTML(sessionManager.getI18nMessage("AdverseEffects"), genericDrug.getAdverseEffects() == null ? "" : genericDrug.getAdverseEffects());
		lblAdverseEffects.setSizeFull();

		lblAdditionalObservations = new LabelHTML(sessionManager.getI18nMessage("AdditionalObservations"), genericDrug.getAdditionalObservations() == null ? "" : genericDrug.getAdditionalObservations());
		lblAdditionalObservations.setSizeFull();

		lblCommercialNames = new LabelHTML(sessionManager.getI18nMessage("CommercialNames"), genericDrug.getCommercialNames() == null ? "" : genericDrug.getCommercialNames());
		lblCommercialNames.setSizeFull();

		fl = new VerticalLayout ();
        fl.add(lblGenericDrugName, lblTherapeuticalGroup, lblTherapeuticalSubgroup, lblTherapeuticComments, lblDosageInformation, lblRoutes, lblAdverseEffects, lblAdditionalObservations, lblCommercialNames);
		fl.setSizeFull();
 
        this.setWidth("70%");
        this.setHeight("70%");
        this.setResizable(true);
        add(fl);
        
	}
	
	private class LabelHTML extends FormLayout {
		Label lblTitle, lblText;
		LabelHTML (String title, String htmlBody) {
			lblTitle = new Label ();
			lblTitle.setText(title);
			lblText = new Label ();
			lblText.setSizeFull();
			lblText.getElement().setProperty("innerHTML", htmlBody);
			add(lblTitle, lblText);
			setResponsiveSteps(
			        new ResponsiveStep("0", 1),
			        new ResponsiveStep("500px", 2),
			        new ResponsiveStep("750px", 3)
			);  
			setColspan(lblTitle, 1);
			setColspan(lblText, 2);
			
		}
	}

}
