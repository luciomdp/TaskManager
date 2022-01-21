package com.advenio.medere.emr.view.edit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.ui.components.UploadImageContent;
import com.advenio.medere.objects.site.Site;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.advenio.medere.utils.LogoProvider;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent()
@Scope("prototype")
public class  LogoEditPanel extends BaseCRUDWindow{
	
	@Autowired protected ApplicationContext context;
	@Autowired protected SiteDAO siteDAO;
	@Autowired protected LogoProvider logoProvider;
	
	protected static final String IMG_PATH = "imgs/";
	private final int IMG_WIDTH = 128;
	private final int IMG_HEIGHT = 128;	

	protected ISessionManager sessionManager;
	private byte [] logoImageBytes;
	private Image logoImage;
	private Label lblNoImage;
	private H4 lblCurrentLogo;
	private UploadImageContent imgUploader;
	private Site site;
	private HorizontalLayout hl;
	private VerticalLayout vlCurrentLogo;
	
	private boolean aceptedChanges;
	


	public LogoEditPanel(String caption,Site site,ISessionManager sessionManager) {
		super(caption);
		this.site = site;
		this.sessionManager = sessionManager;
	}
	

	@Override
	protected void createControls() {
		aceptedChanges = false;
		imgUploader = new UploadImageContent(sessionManager.getI18nMessage("UploadNewLogo"),logoImageBytes, sessionManager, IMG_WIDTH + "px",IMG_HEIGHT + "px");
		hl = new HorizontalLayout();
		vlCurrentLogo = new VerticalLayout();
		lblCurrentLogo = new H4(sessionManager.getI18nMessage("CurrentLogo"));
		lblCurrentLogo.getStyle().set("margin-top", "0.5em");
		vlCurrentLogo.add(lblCurrentLogo);
		if (site != null && site.getLogoFileName() != null && site.getLogoFileHash() != null) 
			logoImage = logoProvider.getLogoImage(site.getLogoFileName(), site.getLogoFileHash());
		if(logoImage != null) {
			logoImage.setWidth(IMG_WIDTH, Unit.PIXELS);
			logoImage.setHeight(IMG_HEIGHT, Unit.PIXELS);
			logoImage.setTitle(sessionManager.getI18nMessage("CurrentLogo"));
			vlCurrentLogo.add(logoImage);
		}
		else {
			lblNoImage = new Label(sessionManager.getI18nMessage("NoCompanyLogo"));
			vlCurrentLogo.add(lblNoImage);
		}
		vlCurrentLogo.setWidth("50%");
		vlCurrentLogo.setHeight("50%");
		imgUploader.setWidth("50%");
		imgUploader.setHeight("50%");
		hl.add(vlCurrentLogo);
		hl.add(imgUploader);
		hl.setSizeFull();
        headerLayout.getStyle().set("border-bottom", "groove");
        lblWindowTitle.setSizeFull();
        lblWindowTitle.getStyle().set("font-size", "larger");
        headerLayout.setSizeFull();
		formLayout.add(hl);
		formLayout.setColspan(hl, 2);
	}


	@Override
	protected void accept() {
		if (imgUploader.getBytes() != null) {
			aceptedChanges = true;
		}
		close();		
	}


	@Override
	public void editItem(Object item) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void cancel() {
		if (imgUploader.getBytes() != null) {
			ConfirmDialog dialog = context.getBean(ConfirmDialog.class,sessionManager.getI18nMessage("AreYouSureToCloseHavingPendingModifications"));
			dialog.addAcceptListener(new IOnNotificationListener () {
				public void onNotification() {
					close();
				}
			});	
		}else 
			close();
	}


	public boolean isAceptedChanges() {
		return aceptedChanges;
	}


	public void setAceptedChanges(boolean aceptedChanges) {
		this.aceptedChanges = aceptedChanges;
	}


	public byte[] getLogoImage() {
		return imgUploader.getBytes();
	}

}
