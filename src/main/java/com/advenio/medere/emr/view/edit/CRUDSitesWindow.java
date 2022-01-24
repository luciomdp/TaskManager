package com.advenio.medere.emr.view.edit;



import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.PersistenceException;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.dao.impl.UserDAO;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.objects.DocumentType;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.location.City;
import com.advenio.medere.objects.location.Country;
import com.advenio.medere.objects.location.Province;
import com.advenio.medere.objects.site.Site;
import com.advenio.medere.objects.user.RegionalSettings;
import com.advenio.medere.rest.MedereRest;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.advenio.medere.utils.LogoProvider;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;



@SpringComponent()
@Scope("prototype")
public class CRUDSitesWindow extends BaseCRUDWindow implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;
	
	@Value("${medere.medereaddress}")
	private String medereAddress;
	private String webmedererestcontrollerURL = "rest/webmedererestcontroller/";
	
	@Autowired protected LogoProvider logoProvider;
	@Autowired protected SiteDAO siteDAO;
	@Autowired protected EntityDAO entityDAO;
	@Autowired protected MedereDAO medereDAO;
	@Autowired protected UserDAO userDAO;
	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	protected static final Logger logger = LoggerFactory.getLogger(CRUDSitesWindow.class);
	
	protected TextField txtName;
	protected TextField txtEmail;
	protected TextField txtAddress;
	protected TextField txtWebsite;	
	protected TextField txtApptitle;
	protected TextField txtWebApptitle;
	
	protected TextField txtUrl;
	protected TextField txtWebappointmentsurl;
	protected TextField txtTotemurl;
	protected TextField txtAnesthesiaappurl;
	protected TextField txtPatientcallerurl;
	protected TextField txtFavIconPath;
	
	protected TextField txtPhone;
	
	protected Checkbox chkHideLocationDetails;
	protected Checkbox chkHideRequestPrescriptions;
	protected Checkbox chkShowCoverageWarning;
	protected Checkbox chkActive;
	
	protected ComboBox<DocumentType> cboDocumentType;
	protected ComboBox<Language> cboLanguage;
	protected ComboBox<Country> cboCountry;
	protected ComboBox<Province> cboProvince;
	protected ComboBox <City>cboCity;
	protected ComboBox <RegionalSettings>cboRegionalSettings;
	
	protected Button btnLogo;
	protected String pageTitle;
	protected boolean aceptedChanges,imageLoaded,newSite;
	protected Language language;
	protected byte[] logoImage;
	protected LogoEditPanel logoEditPanel;
	
	protected Site site;
	
	public CRUDSitesWindow(String caption) {
		super(caption);
		aceptedChanges = false;
		imageLoaded = false;
		newSite = true;
		pageTitle = caption;
	}

	@Override
	protected void createControls() {

		language = medereDAO.loadLanguage(sessionManager.getUser().getLanguageId());
		
		txtName = new TextField(sessionManager.getI18nMessage("CompanyName"));
		txtName.setSizeFull();	
		txtName.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtEmail = new TextField(sessionManager.getI18nMessage("CompanyEmail"));
		txtEmail.setSizeFull();	
		txtEmail.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});

		txtAddress = new TextField(sessionManager.getI18nMessage("CompanyAddress"));
		txtAddress.setSizeFull();	
		txtAddress.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtWebsite = new TextField(sessionManager.getI18nMessage("CompanyWebsite"));
		txtWebsite.setSizeFull();
		txtWebsite.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtApptitle = new TextField(sessionManager.getI18nMessage("CompanyApptitle"));
		txtApptitle.setSizeFull();
		txtApptitle.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtWebApptitle = new TextField(sessionManager.getI18nMessage("CompanyWebApptitle"));
		txtWebApptitle.setSizeFull();
		txtWebApptitle.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtUrl = new TextField(sessionManager.getI18nMessage("URL"));
		txtUrl.setSizeFull();
		txtUrl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtWebappointmentsurl = new TextField(sessionManager.getI18nMessage("WebAppointmentsUrl"));
		txtWebappointmentsurl.setSizeFull();
		txtWebappointmentsurl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtTotemurl = new TextField(sessionManager.getI18nMessage("TotemUrl"));
		txtTotemurl.setSizeFull();
		txtTotemurl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtAnesthesiaappurl = new TextField(sessionManager.getI18nMessage("AnesthesiaUrl"));
		txtAnesthesiaappurl.setSizeFull();
		txtAnesthesiaappurl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtPatientcallerurl = new TextField(sessionManager.getI18nMessage("PatientCallerUrl"));
		txtPatientcallerurl.setSizeFull();
		txtPatientcallerurl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		txtFavIconPath = new TextField(sessionManager.getI18nMessage("FavIconPath"));
		txtFavIconPath.setSizeFull();	
		txtFavIconPath.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtPhone = new TextField(sessionManager.getI18nMessage("CompanyPhone"));
		txtPhone.setSizeFull();	
		txtPhone.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		cboLanguage = new ComboBox<Language>(sessionManager.getI18nMessage("Language"));
		List<Language> languages = medereDAO.loadLanguages();
		cboLanguage.setItems(languages);
		if(!languages.isEmpty())
			cboLanguage.setValue(languages.get(0));
		cboLanguage.addValueChangeListener(event -> aceptedChanges = true);
				
		cboCountry = new ComboBox<Country>(sessionManager.getI18nMessage("Country"));
		cboCountry.setSizeFull();
		cboCountry.setItems(entityDAO.findAllCountries(language));
		cboCountry.setItemLabelGenerator(e -> e.getDescription(language));
		cboCountry.addValueChangeListener(event -> {
			aceptedChanges = true;	
			cboProvince.clear();
			cboProvince.getDataProvider().refreshAll();
			cboCity.clear();
			cboCity.getDataProvider().refreshAll();
		});

		cboProvince = new ComboBox<Province>(sessionManager.getI18nMessage("Province"));
		cboProvince.setSizeFull();
		cboProvince.setItemLabelGenerator(e -> e.getDescription(language));
		cboProvince.setDataProvider(
				(filter, offset,
						limit) -> entityDAO.loadProvinces(filter, offset, limit,
								language, cboCountry.getValue()).stream(),
				(filter) -> entityDAO
						.countProvinces(filter, language, cboCountry.getValue())
						.intValue());
		
		cboProvince.addValueChangeListener(event -> {
			cboCity.clear();
			cboCity.getDataProvider().refreshAll();
		});


		cboCity = new ComboBox<City>(sessionManager.getI18nMessage("City"));
		cboCity.setSizeFull();
		cboCity.setItemLabelGenerator(e -> e.getDescription(language));
		cboCity.setDataProvider(
				(filter, offset,
						limit) -> entityDAO.loadCities(filter, offset, limit,
								language, cboProvince.getValue()).stream(),
				(filter) -> entityDAO
						.countCities(filter, language, cboProvince.getValue())
						.intValue());
		cboCity.addValueChangeListener(event -> aceptedChanges = true);
		
		cboRegionalSettings = new ComboBox <RegionalSettings> (sessionManager.getI18nMessage("RegionalSettings"));
		cboRegionalSettings.setSizeFull();
		List<RegionalSettings> regionalSettings = entityDAO.loadAllRegionalSettings();
		cboRegionalSettings.setItemLabelGenerator(e -> e.displayInfo("Moneda:", "Fecha:", "Sep dec:", "Sep miles: "));
		cboRegionalSettings.setItems(regionalSettings);
		if(!regionalSettings.isEmpty())
			cboRegionalSettings.setValue(regionalSettings.get(0));
		cboRegionalSettings.addValueChangeListener(event -> aceptedChanges = true);
		
		cboDocumentType = new ComboBox<DocumentType>(sessionManager.getI18nMessage("DocumentType"));
		cboDocumentType.setSizeFull();
		List<DocumentType> documentTypes = medereDAO.loadDocumentTypes();
		cboDocumentType.setItems(documentTypes);
		cboDocumentType.setItemLabelGenerator(DocumentType::getLongName);
		
		if(!documentTypes.isEmpty()) {
			Optional<DocumentType> op = documentTypes.stream().filter(e -> e.getDocumentType().equals(DocumentType.DOCUMENTTYPEDNI)).findAny();
			if(op.isPresent())
				cboDocumentType.setValue(op.get());
			else
				cboDocumentType.setValue(documentTypes.get(0));
		}
		cboDocumentType.addValueChangeListener(event -> aceptedChanges = true);
		
		chkHideLocationDetails = new Checkbox(sessionManager.getI18nMessage("HideLocationDetails"));
		chkHideLocationDetails.setSizeFull();
		chkHideLocationDetails.setValue(false);
		chkHideLocationDetails.addValueChangeListener(event -> aceptedChanges = true);
		
		
		chkHideRequestPrescriptions = new Checkbox(sessionManager.getI18nMessage("HideRequestPrescriptions"));
		chkHideRequestPrescriptions.setSizeFull();
		chkHideRequestPrescriptions.setValue(false);
		chkHideRequestPrescriptions.addValueChangeListener(event -> aceptedChanges = true);
		
		chkShowCoverageWarning = new Checkbox(sessionManager.getI18nMessage("ShowCoverageWarning"));
		chkShowCoverageWarning.setSizeFull();
		chkShowCoverageWarning.setValue(true);
		chkShowCoverageWarning.addValueChangeListener(event -> aceptedChanges = true);
		
		
		chkActive = new Checkbox(sessionManager.getI18nMessage("Active"));
		chkActive.setSizeFull();
		chkActive.setValue(true);
		chkActive.addValueChangeListener(event -> aceptedChanges = true);

		VerticalLayout vl = new VerticalLayout();
		btnLogo = new Button(sessionManager.getI18nMessage("EditLogo"),VaadinIcon.PICTURE.create());
		btnLogo.addClickListener(event -> {
			logoEditPanel = context.getBean(LogoEditPanel.class,sessionManager.getI18nMessage("EditLogo"), site,sessionManager);
			logoEditPanel.addOpenedChangeListener(new ComponentEventListener () {
				public void onComponentEvent(ComponentEvent event) {
					imageLoaded = ((LogoEditPanel)event.getSource()).isAceptedChanges();
					if(imageLoaded)
						logoImage =  ((LogoEditPanel)event.getSource()).getLogoImage();
				}
			});
		});
		vl.setAlignItems(Alignment.END);
		vl.add(btnLogo);
		vl.setSizeFull();
		formLayout.add(vl,txtName, txtEmail, txtAddress, txtWebsite, txtApptitle, txtWebApptitle,
				txtUrl,txtWebappointmentsurl,txtTotemurl,txtAnesthesiaappurl,txtPatientcallerurl,txtFavIconPath,
				txtPhone,cboLanguage, cboCountry, cboProvince, cboCity, cboDocumentType, cboRegionalSettings,
				chkHideLocationDetails,chkHideRequestPrescriptions, chkShowCoverageWarning, chkActive);

		formLayout.setResponsiveSteps(
		        new ResponsiveStep("0", 1),
		        new ResponsiveStep("500px", 2),
		        new ResponsiveStep("750px", 3)
		);
		
		formLayout.setColspan(vl, 3);
		
	}
	

	@Override
	public String getPageTitle() {
		return pageTitle;
	}

	
	@Override
	public void editItem(Object item) {
		
		newSite = false;
		site = (Site) item;
		if(site != null) {
			txtName.setValue(site.getCompanyName()==null?"":site.getCompanyName());
			txtEmail.setValue(site.getCompanyEmail()==null?"":site.getCompanyEmail());
			txtAddress.setValue(site.getCompanyAddress()==null?"":site.getCompanyAddress());
			txtWebsite.setValue(site.getCompanyWebsite()==null?"":site.getCompanyWebsite());
			txtWebApptitle.setValue(site.getWebAppTitle()==null?"":site.getWebAppTitle());
			txtApptitle.setValue(site.getApptitle()==null?"":site.getApptitle());
			
			txtUrl.setValue(site.getUrl()==null?"":site.getUrl());
			txtWebappointmentsurl.setValue(site.getWebAppointmentsUrl()==null?"":site.getWebAppointmentsUrl());
			txtTotemurl.setValue(site.getTotemUrl()==null?"":site.getTotemUrl());
			txtAnesthesiaappurl.setValue(site.getAnesthesiaAppUrl()==null?"":site.getAnesthesiaAppUrl());
			txtPatientcallerurl.setValue(site.getPatientCallerUrl()==null?"":site.getPatientCallerUrl());
			txtFavIconPath.setValue(site.getFavIconPath()==null?"":site.getFavIconPath());
			
			txtPhone.setValue(site.getCompanyTelephone()==null?"":site.getCompanyTelephone());
			if(language != null)
				cboLanguage.setValue(language);
			if(site.getDefaultCity() != null) {
				City defCity = entityDAO.findCityComplete(site.getDefaultCity().getCity());
				cboCountry.setValue(defCity.getProvince().getCountry());
				cboProvince.setValue(defCity.getProvince());
				cboCity.setValue(defCity);
			}
			if(site.getDefaultRegionalSettings() != null)
				cboRegionalSettings.setValue(site.getDefaultRegionalSettings());
			if(site.getDefaultDocumentType() != null)
				cboDocumentType.setValue(site.getDefaultDocumentType());
			
			chkHideLocationDetails.setValue(site.isHideLocationDetails());
			chkHideRequestPrescriptions.setValue(site.isHideRequestPrescriptions());
			chkShowCoverageWarning.setValue(site.isShowcoveragewarning());
			chkActive.setValue(site.isActive());
			aceptedChanges = false;
		}
	}


	@Override
	protected void accept() {
		if (aceptedChanges || imageLoaded) { 
			if (validate()) {
				String uri;
				UriComponentsBuilder builder;
				RestTemplate restTemplate;
				UploadChanges();
				try {
					if(newSite) {
						uri = medereAddress.concat(webmedererestcontrollerURL) + "createSite";
						builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("siteId", siteDAO.saveSite(site));
						restTemplate = context.getBean(MedereRest.class).createRestTemplate();
						restTemplate.getForObject(builder.toUriString(), String.class); 
					}
					else {
						siteDAO.updateSite(site);
						uri = medereAddress.concat(webmedererestcontrollerURL) + "updateSite";
						builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("siteId", site.getSite());
						restTemplate = context.getBean(MedereRest.class).createRestTemplate();
						restTemplate.getForObject(builder.toUriString(), String.class); 
					}
					Notification.show(sessionManager.getI18nMessage("DataSuccesfullySave")).setPosition(Position.MIDDLE);
					
					if(site.getLogoFileName() == null || site.getLogoFileHash() == null) {
						Notification.show(sessionManager.getI18nMessage("YouShouldSelectLogo")).setPosition(Position.MIDDLE);
						btnLogo.focus();
					}
					messageBus.post(new EventSitesChanged());
					close();
				}catch(ResourceAccessException e) {
					Notification.show(sessionManager.getI18nMessage("ErrorUpdatingSiteInMedere")).setPosition(Position.MIDDLE);

				}catch(PersistenceException pe) {
					Notification.show(sessionManager.getI18nMessage("ErrorSavingSiteInBD")).setPosition(Position.MIDDLE);
				}
				
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

		if (StringsUtils.isNullOrEmptyTrimmed(txtEmail.getValue())) {
			txtEmail.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteMail")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtAddress.getValue())) {
			txtAddress.focus();		
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteAddress")).setPosition(Position.MIDDLE);
			return false;
		}

		if (StringsUtils.isNullOrEmptyTrimmed(txtWebsite.getValue())) {
			txtWebsite.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebSite")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtApptitle.getValue())) {
			txtApptitle.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteAppTitle")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtWebApptitle.getValue())) {
			txtWebApptitle.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebAppTitle")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtUrl.getValue())) {
			txtUrl.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteUrl")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtWebappointmentsurl.getValue())) {
			txtWebappointmentsurl.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebAppointmentsUrl")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtTotemurl.getValue())) {
			txtTotemurl.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebAppointmentsUrl")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtAnesthesiaappurl.getValue())) {
			txtAnesthesiaappurl.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteAntesthesiaUrl")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtPatientcallerurl.getValue())) {
			txtPatientcallerurl.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompletePatientCallerUrl")).setPosition(Position.MIDDLE);
			return false;
		}
		if (StringsUtils.isNullOrEmptyTrimmed(txtFavIconPath.getValue())) {
			txtFavIconPath.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteFavIconPath")).setPosition(Position.MIDDLE);
			return false;
		}
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtPhone.getValue())) {
			txtPhone.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompletePhone")).setPosition(Position.MIDDLE);
			return false;
		}		
		
		if (cboCity.isEmpty()) {
			cboCity.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseSelectCity")).setPosition(Position.MIDDLE);
			return false;			
		}
		
		return true;
	}
	
	
	protected void UploadChanges() {
	//ver logoreportfilename
		if (site == null) {
			site = new Site();
			site.setDefaultSite(false);
			site.setTotemUser(userDAO.loadCompleteUser(Long.valueOf(28013)));
		}
		
		site.setCompanyName(txtName.getValue());
		site.setCompanyEmail(txtEmail.getValue());
		site.setCompanyAddress(txtAddress.getValue());
		site.setCompanyWebsite(txtWebsite.getValue());
		site.setWebAppTitle(txtWebApptitle.getValue());
		site.setApptitle(txtApptitle.getValue());
		site.setUrl(txtUrl.getValue());
		site.setWebAppointmentsUrl(txtWebappointmentsurl.getValue());
		site.setTotemUrl(txtTotemurl.getValue());
		site.setAnesthesiaAppUrl(txtAnesthesiaappurl.getValue());
		site.setPatientCallerUrl(txtPatientcallerurl.getValue());
		site.setFavIconPath(txtFavIconPath.getValue());
		site.setCompanyTelephone(txtPhone.getValue());
		
		site.setLanguage(cboLanguage.getValue());
		site.setDefaultCity(cboCity.getValue());
		site.setDefaultDocumentType(cboDocumentType.getValue());
		site.setDefaultRegionalSettings(cboRegionalSettings.getValue());

		site.setHideLocationDetails(chkHideLocationDetails.getValue());
		site.setHideRequestPrescriptions(chkHideRequestPrescriptions.getValue());
		site.setShowcoveragewarning(chkShowCoverageWarning.getValue());
		site.setActive(chkActive.getValue());
		
		if(site.getMedereUUID() == null)
			site.setMedereUUID(UUID.randomUUID().toString());
		if(imageLoaded && logoImage != null) {
			String oldPath = site.getLogoFileName();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			LocalDateTime now = LocalDateTime.now();
			site.setLogoFileName("logoSite" + dtf.format(now) + ".png");
			site.setLogoFileHash(getLogoFileHash(logoImage));
			try {
				logoProvider.sendLogoImage(site.getLogoFileName(),logoImage,oldPath);
			} catch (IOException e) {
				logger.error(e.getMessage() + " hubo problemas al enviar la imagen a través del logo provider",e); 
			}
			
			//borrar carpeta img dentro de sitesabm
		}

	}
	
	@Override
	protected void cancel() {
		
		if(aceptedChanges || imageLoaded) {
			ConfirmDialog dialog = context.getBean(ConfirmDialog.class,sessionManager.getI18nMessage("AreYouSureToCloseHavingPendingModifications"));
			dialog.addAcceptListener(new IOnNotificationListener () {
				public void onNotification() {
					close();
				}
			});
		}else
			close();
	}
	
	private String getLogoFileHash(byte[] logo) {
			MessageDigest md;
			String myChecksum = "";
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(logo);
				byte[] digest = md.digest();
				myChecksum = DatatypeConverter
				      .printHexBinary(digest).toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return myChecksum;
	}
}