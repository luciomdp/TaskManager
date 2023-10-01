package com.advenio.medere.emr.view.edit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;


import com.advenio.medere.MessageBusContainer;

import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.GenericDrugDAO;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.dao.UserDAO;

import com.advenio.medere.emr.objects.medicine.GenericDrug;

import com.advenio.medere.objects.Language;
import com.advenio.medere.server.session.ISessionManager;

import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ComponentEventListener;

import com.vaadin.flow.component.checkbox.Checkbox;

import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent()
@Scope("prototype")
public class CRUDGenericDrugWindow extends BaseCRUDWindow implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;

	
	@Autowired protected SiteDAO siteDAO;
	@Autowired protected EntityDAO entityDAO;
	@Autowired protected MedereDAO medereDAO;
	@Autowired protected UserDAO userDAO;

	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	@Autowired protected GenericDrugDAO genericDrugDAO;
	
	protected static final Logger logger = LoggerFactory.getLogger(CRUDGenericDrugWindow.class);
	

	protected GenericDrug genericDrug = null;
	
	protected TextField txtName;
	protected TextField txtMaxTreatmentDuration;

	
	protected TextArea txtObservations;
	
	protected Checkbox chkComposed;
	protected Checkbox chkPreventOutOfLevelDosage;
	
	protected String pageTitle;
	protected boolean aceptedChanges, newGenericDrug;
	protected Language language;
	
	
	public CRUDGenericDrugWindow(String caption) {
		super(caption);
		aceptedChanges = false;
		pageTitle = caption;
	}

	@Override
	protected void createControls() {
		
	
		language = medereDAO.loadLanguage(sessionManager.getUser().getLanguageId());
		
		txtName = new TextField(sessionManager.getI18nMessage("Name"));
		txtName.setSizeFull();	
		txtName.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtMaxTreatmentDuration = new TextField(sessionManager.getI18nMessage("MaxDaysTreatmentDuration"));
		txtMaxTreatmentDuration.setSizeFull();	
		txtMaxTreatmentDuration.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtObservations = new TextArea(sessionManager.getI18nMessage("Observations"));
		txtObservations.setSizeFull();
		txtObservations.addBlurListener(new ComponentEventListener<BlurEvent<TextArea>>() {
			public void onComponentEvent(BlurEvent<TextArea> event) {
				aceptedChanges = true;	
			}	
		});

		chkComposed = new Checkbox(sessionManager.getI18nMessage("Composed"));
		chkComposed.setSizeFull();
		chkComposed.setValue(false);
		chkComposed.addValueChangeListener(event -> aceptedChanges = true);
		
		chkPreventOutOfLevelDosage = new Checkbox(sessionManager.getI18nMessage("PreventOutOfLevelDosage"));
		chkPreventOutOfLevelDosage.setSizeFull();
		chkPreventOutOfLevelDosage.setValue(false);
		chkPreventOutOfLevelDosage.addValueChangeListener(event -> aceptedChanges = true);
	
		formLayout.setResponsiveSteps(
		        new ResponsiveStep("0", 1),
		        new ResponsiveStep("500px", 2),
		        new ResponsiveStep("750px", 3)
		);
		
		formLayout.add(txtName, txtMaxTreatmentDuration, txtObservations, chkComposed, chkPreventOutOfLevelDosage);
		formLayout.setColspan(txtName, 1);
		formLayout.setColspan(txtMaxTreatmentDuration, 1);
		formLayout.setColspan(txtObservations, 1);
		
		
	}

	@Override
	public String getPageTitle() {
		// TODO Auto-generated method stub
		return pageTitle;
	}

	@Override
	protected void accept() {
		// TODO Auto-generated method stub
		if (aceptedChanges) { 
			if (validate()) {
				UploadChanges();
				Notification.show(sessionManager.getI18nMessage("DataSuccesfullySave")).setPosition(Position.MIDDLE);
				messageBus.post(new EventStateChanged());
				close();				
			}
		}else {
			close();
		}	
		
		
	}

	@Override
	public void editItem(Object item) {
		// TODO Auto-generated method stub
		newGenericDrug = false;
		genericDrug = (GenericDrug)item;
		if (genericDrug != null) {
			txtName.setValue(genericDrug.getName()==null?"":genericDrug.getName());
			txtMaxTreatmentDuration.setValue(genericDrug.getMaxDaysTreatmentDuration()==null?"":""+genericDrug.getMaxDaysTreatmentDuration());	
			txtObservations.setValue(genericDrug.getObservations() == null ? "" : genericDrug.getObservations());
			chkComposed.setValue(genericDrug.isComposed());
			chkPreventOutOfLevelDosage.setValue(genericDrug.isPreventOutOfLevelDosage());
		}
	
	}
	

	protected boolean validate() {
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtName.getValue())) {
			txtName.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteName")).setPosition(Position.MIDDLE);
			return false;			
		}
	
		return true;
	}
	
	
	protected void UploadChanges() {

		if (genericDrug == null) {
			genericDrug = new GenericDrug();
		}
		genericDrug.setName(txtName.getValue());
		genericDrug.setMaxDaysTreatmentDuration(txtMaxTreatmentDuration.isEmpty() ||txtMaxTreatmentDuration.getValue() == null ?null: Integer.valueOf(txtMaxTreatmentDuration.getValue()));
		genericDrug.setObservations(txtObservations.getValue());
		genericDrug.setComposed(chkComposed.getValue());
		genericDrug.setPreventOutOfLevelDosage(chkPreventOutOfLevelDosage.getValue());
	
		if(newGenericDrug)
			genericDrugDAO.saveNewGenericDrug(genericDrug);
		else
			genericDrugDAO.updateGenericDrug(genericDrug);
	}
	
	@Override
	protected void cancel() {
		
		if(aceptedChanges) {
			ConfirmDialog dialog = context.getBean(ConfirmDialog.class,sessionManager.getI18nMessage("AreYouSureToCloseHavingPendingModifications"));
			dialog.addAcceptListener(new IOnNotificationListener () {
				public void onNotification() {
					close();
				}
			});
		}else
			close();
	}
	
}
	