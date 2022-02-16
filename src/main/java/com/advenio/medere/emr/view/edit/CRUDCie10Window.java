package com.advenio.medere.emr.view.edit;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;

import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.CIE10DAO;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Disease;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;



@SpringComponent()
@Scope("prototype")
public class CRUDCie10Window extends BaseCRUDWindow implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;
	

	@Autowired protected CIE10DAO cie10DAO;
	@Autowired protected MedereDAO medereDAO;
	@Autowired protected UserDAO userDAO;
	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	
	protected static final Logger logger = LoggerFactory.getLogger(CRUDSitesWindow.class);
	
	protected TextField txtName;
	protected TextField txtCie10;
	protected TextField txtGender;
	
	protected ComboBox<Disease> cboParentDisease;
	
	protected Checkbox chkHasParent;

	protected boolean aceptedChanges,newDisease;
	
	protected Disease disease;

	private String pageTitle;
	
	public CRUDCie10Window(String caption) {
		super(caption);
		aceptedChanges = false;
		newDisease = true;
		pageTitle = caption;
	}

	@Override
	protected void createControls() {

		
		txtName = new TextField(sessionManager.getI18nMessage("DiseaseName"));
		txtName.setSizeFull();	
		txtName.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtCie10 = new TextField(sessionManager.getI18nMessage("Cie10Abreviation"));
		txtCie10.setSizeFull();	
		txtCie10.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});

		txtGender = new TextField(sessionManager.getI18nMessage("Gender"));
		txtGender.setSizeFull();	
		txtGender.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		
		cboParentDisease = new ComboBox<Disease>(sessionManager.getI18nMessage("ParentDisease"));
		List<Disease> diseases = cie10DAO.findAllCie10();
		cboParentDisease.setItems(diseases);
		if(!diseases.isEmpty()) 
			cboParentDisease.setValue(diseases.get(0));
		cboParentDisease.addValueChangeListener(event -> aceptedChanges = true);
		
		chkHasParent = new Checkbox(sessionManager.getI18nMessage("HasParentDisease"));
		chkHasParent.setSizeFull();

		chkHasParent.addValueChangeListener(event -> {
			cboParentDisease.setEnabled(!cboParentDisease.isEnabled());
			aceptedChanges = true;});
		
		chkHasParent.setValue(false);
		cboParentDisease.setEnabled(false);
		
		formLayout.add(txtCie10,txtName, txtGender, cboParentDisease,chkHasParent);

		formLayout.setResponsiveSteps(
		        new ResponsiveStep("0", 1),
		        new ResponsiveStep("500px", 2),
		        new ResponsiveStep("750px", 3)
		);
		formLayout.setColspan(txtCie10, 1);
		formLayout.setColspan(txtName, 1);
		formLayout.setColspan(txtGender, 1);
		formLayout.setColspan(cboParentDisease, 2);
		formLayout.setColspan(chkHasParent, 1);
		
	}
	

	@Override
	public String getPageTitle() {
		return pageTitle;
	}

	
	@Override
	public void editItem(Object item) {
		
		newDisease = false;
		disease = (Disease) item;
		if(disease != null) {
			txtName.setValue(disease.getName()==null?"":disease.getName());
			txtCie10.setValue(disease.getCIE10()==null?"":disease.getCIE10());
			txtGender.setValue(disease.getGender()+"");
			if(disease.getParentDisease() != null) {
				cboParentDisease.setValue(disease.getParentDisease());
				chkHasParent.setValue(true);
			}else {
				cboParentDisease.setValue(null);
				cboParentDisease.setEnabled(false);
				chkHasParent.setValue(false);
			}
			aceptedChanges = false;
		}
	}


	@Override
	protected void accept() {
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
	

	protected boolean validate() {
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtName.getValue())) {
			txtName.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteName")).setPosition(Position.MIDDLE);
			return false;			
		}

		if (StringsUtils.isNullOrEmptyTrimmed(txtCie10.getValue())) {
			txtCie10.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteCie10")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtGender.getValue()) || txtGender.getValue().length()>1) {
			txtGender.focus();		
			Notification.show(sessionManager.getI18nMessage("PleaseCheckGender")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (cboParentDisease.isEmpty() && chkHasParent.getValue()) {
			cboParentDisease.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseSelectParent")).setPosition(Position.MIDDLE);
			return false;			
		}
		
		return true;
	}
	
	
	protected void UploadChanges() {

		if (disease == null) {
			disease = new Disease();
		}
		disease.setCIE10(txtCie10.getValue());
		disease.setGender(txtGender.getValue().charAt(0));
		disease.setName(txtName.getValue());
		disease.setParentDisease(chkHasParent.getValue()?cboParentDisease.getValue():null);
		
		if(newDisease)
			cie10DAO.saveDisease(disease);
		else
			cie10DAO.updateDisease(disease);
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
