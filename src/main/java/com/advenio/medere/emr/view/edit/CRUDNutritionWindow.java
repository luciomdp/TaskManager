package com.advenio.medere.emr.view.edit;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.NutritionalItemDAO;
import com.advenio.medere.emr.objects.NutritionalItem;
import com.advenio.medere.emr.objects.NutritionalType;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;



@SpringComponent()
@Scope("prototype")
public class CRUDNutritionWindow extends BaseCRUDWindow implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;
	

	@Autowired protected NutritionalItemDAO nutritionalItemDAO;
	@Autowired protected MedereDAO medereDAO;
	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	
	protected static final Logger logger = LoggerFactory.getLogger(CRUDSitesWindow.class);
	
	protected TextField txtDescription;
	
	protected ComboBox<NutritionalType> cboNutritionalType;
	
	protected Checkbox chkActive;

	protected boolean aceptedChanges,newNutritionalItem;
	
	protected NutritionalItem nutritionalItem;

	private String pageTitle;
	
	public CRUDNutritionWindow(String caption) {
		super(caption);
		aceptedChanges = false;
		newNutritionalItem = true;
		pageTitle = caption;
	}

	@Override
	protected void createControls() {

		
		txtDescription = new TextField(sessionManager.getI18nMessage("Description"));
		txtDescription.setSizeFull();	
		txtDescription.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		cboNutritionalType = new ComboBox<NutritionalType>(sessionManager.getI18nMessage("Category"));
		List<NutritionalType> diseases = nutritionalItemDAO.loadNutritionalTypes(medereDAO.loadLanguage(sessionManager.getUser().getLanguageId()));
		cboNutritionalType.setItems(diseases);
		if(!diseases.isEmpty()) 
			cboNutritionalType.setValue(diseases.get(0));
		cboNutritionalType.addValueChangeListener(event -> aceptedChanges = true);
		
		chkActive = new Checkbox(sessionManager.getI18nMessage("Active"));
		chkActive.setSizeFull();
		chkActive.addValueChangeListener(event -> aceptedChanges = true);	
		chkActive.setValue(true);
		
		formLayout.add(txtDescription,cboNutritionalType, chkActive);

		formLayout.setResponsiveSteps(
		        new ResponsiveStep("0", 1),
		        new ResponsiveStep("500px", 2),
		        new ResponsiveStep("750px", 3)
		);
		formLayout.setColspan(txtDescription, 3);
		formLayout.setColspan(cboNutritionalType, 2);
		formLayout.setColspan(chkActive, 1);
		
		aceptedChanges = false;
	}
	

	@Override
	public String getPageTitle() {
		return pageTitle;
	}

	
	@Override
	public void editItem(Object item) {
		
		newNutritionalItem = false;
		nutritionalItem = (NutritionalItem) item;
		if(nutritionalItem != null) {
			txtDescription.setValue(nutritionalItem.getDescription()==null?"":nutritionalItem.getDescription());
			chkActive.setValue(nutritionalItem.isActive());
			if(nutritionalItem.getNutritionalType() != null) 
				cboNutritionalType.setValue(nutritionalItem.getNutritionalType());
		}
		aceptedChanges = false;
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
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtDescription.getValue())) {
			txtDescription.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteDescription")).setPosition(Position.MIDDLE);
			return false;			
		}
		
		if (cboNutritionalType.isEmpty()) {
			cboNutritionalType.focus();
			Notification.show(sessionManager.getI18nMessage("SelectAnItem")).setPosition(Position.MIDDLE);
			return false;			
		}
		
		return true;
	}
	
	
	protected void UploadChanges() {

		if (nutritionalItem == null) {
			nutritionalItem = new NutritionalItem();
		}
		nutritionalItem.setActive(chkActive.getValue());
		nutritionalItem.setDescription(txtDescription.getValue());
		nutritionalItem.setNutritionalType(cboNutritionalType.getValue());
		
		if(newNutritionalItem)
			nutritionalItemDAO.saveNewNutritionalItem(nutritionalItem);
		else
			nutritionalItemDAO.updateNutritionalItem(nutritionalItem);
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
