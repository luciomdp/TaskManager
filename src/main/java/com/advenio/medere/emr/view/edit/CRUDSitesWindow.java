package com.advenio.medere.emr.view.edit;



import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.patient.PatientEntity;
import com.advenio.medere.emr.objects.user.User;
import com.advenio.medere.objects.DocumentType;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.location.City;
import com.advenio.medere.objects.location.Country;
import com.advenio.medere.objects.location.Province;
import com.advenio.medere.objects.site.Site;
import com.advenio.medere.objects.user.RegionalSettings;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
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

	@Value("${messagesender.url}")
    private String urlMessageSender;
	@Value("${messagesender.username}")
    private String usernameMessageSender;
	@Value("${messagesender.password}")
    private String passwordMessageSender;

	private final String COMPANY_ADMIN_USERNAME = "administrador";
	private final String COMPANY_ADMIN_PASSWORD = "administrador";

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
	protected TextField txtWebappointmentsuserurl;
	protected TextField txtTotemurl;
	protected TextField txtAnesthesiaappurl;
	protected TextField txtPatientcallerurl;
	protected TextField txtFavIconPath;
	protected TextField txtPrescriptionAppMaxScheduleHour;
	protected TextField txtPrescriptionAppDeadlineToScheduleForCurrentDate;
	
	protected TextField txtPhone;
	
	protected Checkbox chkHideLocationDetails;
	protected Checkbox chkHideRequestPrescriptions;
	protected Checkbox chkShowCoverageWarning;
	protected Checkbox chkActive;
	protected Checkbox chkManualHospitalizationEgressEnabled;
	protected Checkbox chkManualHospitalizationEnabled;
	protected Checkbox chkWebappointmentsEnabled;
	protected Checkbox chkStockPreparationControl;
	
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
		
		txtUrl = new TextField(sessionManager.getI18nMessage("MedereURL"));
		txtUrl.setSizeFull();
		txtUrl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtWebappointmentsurl = new TextField(sessionManager.getI18nMessage("WebAppointmentsUrl"));
		txtWebappointmentsurl.setHelperText("No poner protocolo https");
		txtWebappointmentsurl.setSizeFull();
		txtWebappointmentsurl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtWebappointmentsuserurl = new TextField(sessionManager.getI18nMessage("WebAppointmentsUserUrl"));
		txtWebappointmentsuserurl.setSizeFull();
		txtWebappointmentsuserurl.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
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
		
		txtPrescriptionAppMaxScheduleHour = new TextField(sessionManager.getI18nMessage("PrescriptionAppMaxScheduleHour"));
		txtPrescriptionAppMaxScheduleHour.setSizeFull();	
		txtPrescriptionAppMaxScheduleHour.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
			public void onComponentEvent(BlurEvent<TextField> event) {
				aceptedChanges = true;	
			}	
		});
		
		txtPrescriptionAppDeadlineToScheduleForCurrentDate = new TextField(sessionManager.getI18nMessage("PrescriptionAppDeadlineToScheduleForCurrentDate"));
		txtPrescriptionAppDeadlineToScheduleForCurrentDate.setSizeFull();	
		txtPrescriptionAppDeadlineToScheduleForCurrentDate.addBlurListener(new ComponentEventListener<BlurEvent<TextField>>() {
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

		chkManualHospitalizationEnabled = new Checkbox(sessionManager.getI18nMessage("ManualHospitalizationEnabled"));
		chkManualHospitalizationEnabled.setSizeFull();
		chkManualHospitalizationEnabled.setValue(true);
		chkManualHospitalizationEnabled.addValueChangeListener(event -> aceptedChanges = true);

		chkManualHospitalizationEgressEnabled = new Checkbox(sessionManager.getI18nMessage("ManualHospitalizationEgressEnabled"));
		chkManualHospitalizationEgressEnabled.setSizeFull();
		chkManualHospitalizationEgressEnabled.setValue(true);
		chkManualHospitalizationEgressEnabled.addValueChangeListener(event -> aceptedChanges = true);
		
		chkWebappointmentsEnabled = new Checkbox(sessionManager.getI18nMessage("WebAppointmentsEnabled"));
		chkWebappointmentsEnabled.setSizeFull();
		chkWebappointmentsEnabled.setValue(true);
		chkWebappointmentsEnabled.addValueChangeListener(event -> {
			txtWebApptitle.setEnabled(event.getValue());
			txtWebappointmentsurl.setEnabled(event.getValue());
			txtWebappointmentsuserurl.setEnabled(event.getValue());
			chkHideLocationDetails.setEnabled(event.getValue());
			chkHideRequestPrescriptions.setEnabled(event.getValue());
			chkShowCoverageWarning.setEnabled(event.getValue());	
			aceptedChanges = true;
		});
		
		chkStockPreparationControl = new Checkbox(sessionManager.getI18nMessage("StockPreparationControl"));
		chkStockPreparationControl.setSizeFull();
		chkStockPreparationControl.setValue(true);
		chkStockPreparationControl.addValueChangeListener(event -> {	
			aceptedChanges = true;
		});

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
		formLayout.add(vl,txtName, txtEmail, txtAddress, txtWebsite, txtApptitle, txtUrl,
				txtWebApptitle,txtWebappointmentsurl,txtWebappointmentsuserurl,txtTotemurl,txtAnesthesiaappurl,txtPatientcallerurl,txtFavIconPath,
				txtPhone,txtPrescriptionAppMaxScheduleHour,txtPrescriptionAppDeadlineToScheduleForCurrentDate,cboLanguage, cboCountry, cboProvince, cboCity, cboDocumentType, cboRegionalSettings,chkWebappointmentsEnabled,
				chkHideLocationDetails,chkHideRequestPrescriptions, chkShowCoverageWarning, chkManualHospitalizationEnabled,
				chkManualHospitalizationEgressEnabled,chkStockPreparationControl,chkActive);

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
		
		if(item != null) {
			site = (Site) item;
			txtName.setValue(site.getCompanyName()==null?"":site.getCompanyName());
			txtEmail.setValue(site.getCompanyEmail()==null?"":site.getCompanyEmail());
			txtAddress.setValue(site.getCompanyAddress()==null?"":site.getCompanyAddress());
			txtWebsite.setValue(site.getCompanyWebsite()==null?"":site.getCompanyWebsite());
			txtWebApptitle.setValue(site.getWebAppTitle()==null?"":site.getWebAppTitle());
			txtApptitle.setValue(site.getApptitle()==null?"":site.getApptitle());
			if(site.getSite() != null)
				txtUrl.setValue(site.getUrl()==null?"":site.getUrl());
			else
				txtUrl.setPlaceholder("http://www.xxxxxxx.com.ar");
			if(site.getSite() != null)
				txtWebappointmentsurl.setValue(site.getWebAppointmentsUrl()==null?"":site.getWebAppointmentsUrl());
			else
				txtWebappointmentsurl.setPlaceholder("http://www.xxxxxxx.com.ar");
			txtWebappointmentsuserurl.setValue(site.getWebAppointmentsUserUrl()==null?"":site.getWebAppointmentsUserUrl());
			txtTotemurl.setValue(site.getTotemUrl()==null?"":site.getTotemUrl());
			txtAnesthesiaappurl.setValue(site.getAnesthesiaAppUrl()==null?"":site.getAnesthesiaAppUrl());
			txtPatientcallerurl.setValue(site.getPatientCallerUrl()==null?"":site.getPatientCallerUrl());
			txtFavIconPath.setValue(site.getFavIconPath()==null?"":site.getFavIconPath());
			txtPrescriptionAppDeadlineToScheduleForCurrentDate.setValue(site.getPrescriptionAppDeadlineToScheduleForCurrentDate()==null?"":site.getPrescriptionAppDeadlineToScheduleForCurrentDate());
			txtPrescriptionAppMaxScheduleHour.setValue(site.getPrescriptionAppMaxScheduleHour()==null?"":site.getPrescriptionAppMaxScheduleHour());
			
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
			chkManualHospitalizationEnabled.setValue(site.getManualHospitalizationEnabled());
			chkManualHospitalizationEgressEnabled.setValue(site.getManualHospitalizationEgressEnabled());
			chkActive.setValue(site.isActive());
			chkStockPreparationControl.setValue(site.isStockPreparationControl());
			aceptedChanges = false;
			if(StringsUtils.isNullOrEmptyTrimmed(site.getWebAppointmentsUrl())) {
				chkWebappointmentsEnabled.setValue(false);
			}
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
					
					Notification.show(sessionManager.getI18nMessage("DataSuccesfullySave")).setPosition(Position.MIDDLE);
					
					close();
				}catch(DataIntegrityViolationException e) {
					Notification.show(sessionManager.getI18nMessage("URLMustBeUniqueForEachSite")).setPosition(Position.MIDDLE);
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
		
		if (StringsUtils.isNullOrEmptyTrimmed(txtUrl.getValue())) {
			txtUrl.focus();
			Notification.show(sessionManager.getI18nMessage("PleaseCompleteUrl")).setPosition(Position.MIDDLE);
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
		
		//Parametros de webappointments
		if(chkWebappointmentsEnabled.getValue()) {
		
			if (StringsUtils.isNullOrEmptyTrimmed(txtWebApptitle.getValue())) {
				txtWebApptitle.focus();
				Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebAppTitle")).setPosition(Position.MIDDLE);
				return false;
			}
			
			if (StringsUtils.isNullOrEmptyTrimmed(txtWebappointmentsurl.getValue())) {
				txtWebappointmentsurl.focus();
				Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebAppointmentsUrl")).setPosition(Position.MIDDLE);
				return false;
			}
			
			if (StringsUtils.isNullOrEmptyTrimmed(txtWebappointmentsuserurl.getValue())) {
				txtWebappointmentsuserurl.focus();
				Notification.show(sessionManager.getI18nMessage("PleaseCompleteWebAppointmentsUserUrl")).setPosition(Position.MIDDLE);
				return false;
			}
		}
		
		return true;
	}
	
	
	protected void UploadChanges() {

		if (site == null) {
			site = new Site();
			site.setDefaultSite(false);
			site.setTotemUser(userDAO.loadCompleteUser(Long.valueOf(28013)));
		}
		
		site.setCompanyName(txtName.getValue());
		site.setCompanyEmail(txtEmail.getValue());
		site.setCompanyAddress(txtAddress.getValue());
		site.setCompanyWebsite(txtWebsite.getValue());
		
		site.setApptitle(txtApptitle.getValue());
		site.setUrl(txtUrl.getValue());
		
		site.setTotemUrl(txtTotemurl.getValue());
		site.setAnesthesiaAppUrl(txtAnesthesiaappurl.getValue());
		site.setPatientCallerUrl(txtPatientcallerurl.getValue());
		site.setFavIconPath(txtFavIconPath.getValue());
		site.setCompanyTelephone(txtPhone.getValue());
		site.setPrescriptionAppDeadlineToScheduleForCurrentDate(txtPrescriptionAppDeadlineToScheduleForCurrentDate.getValue());
		site.setPrescriptionAppMaxScheduleHour(txtPrescriptionAppMaxScheduleHour.getValue());
		
		site.setLanguage(cboLanguage.getValue());
		site.setDefaultCity(cboCity.getValue());
		site.setDefaultDocumentType(cboDocumentType.getValue());
		site.setDefaultRegionalSettings(cboRegionalSettings.getValue());

		site.setHideLocationDetails(chkHideLocationDetails.getValue());
		site.setHideRequestPrescriptions(chkHideRequestPrescriptions.getValue());
		site.setShowcoveragewarning(chkShowCoverageWarning.getValue());
		site.setManualHospitalizationEnabled(chkManualHospitalizationEnabled.getValue());
		site.setManualHospitalizationEgressEnabled(chkManualHospitalizationEgressEnabled.getValue());

		site.setActive(chkActive.getValue());
		site.setStockPreparationControl(chkStockPreparationControl.getValue());
		
		if(imageLoaded && logoImage != null) {
			String oldPath = site.getLogoFileName();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			LocalDateTime now = LocalDateTime.now();
			site.setLogoFileName("logoSite" + dtf.format(now) + ".png");
			site.setLogoFileHash(getLogoFileHash(logoImage));
			
			
		}
		//Parametros de webappointments
		if(chkWebappointmentsEnabled.getValue()) {
			site.setWebAppTitle(txtWebApptitle.getValue());
			site.setWebAppointmentsUrl(txtWebappointmentsurl.getValue());
			site.setWebAppointmentsUserUrl(txtWebappointmentsuserurl.getValue());
		}else {
			site.setWebAppTitle(null);
			site.setWebAppointmentsUrl(null);
			site.setWebAppointmentsUserUrl(null);
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
	
	private User generateAdmin(String userName, String password) {
		User admin = new User();
		admin.setBlocked(false);
		admin.setEmail("completar@completar.com.ar");
		admin.setFirstName("Administrador");
		admin.setLanguage(site.getLanguage());
		admin.setLastName(site.getCompanyName());
		admin.setMaxInactiveInterval(3600);
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		admin.setPassword(passEncoder.encode(password));
		admin.setPasswordChange(false);
		admin.setProfile(userDAO.findProfile(1, site.getLanguage()));
		admin.setRegionalSettings(site.getDefaultRegionalSettings());
		admin.setSite(site);
		admin.setUsername(userName);
		
		PatientEntity person = new PatientEntity();
		person.setActive(true);
		person.setAddress("");
		person.setAge(BigDecimal.valueOf(99));
		person.setCity(site.getDefaultCity());
		person.setClinicHistoryID("");
		person.setCredentialPhotoBack("");
		person.setCredentialPhotoFront("");
		person.setDocumentType(site.getDefaultDocumentType());
		person.setEmail(admin.getEmail());	
		person.setEntityPhoto("");
		person.setExternalID("");
		person.setFirstName(admin.getFirstName());
		person.setGender('M');
		person.setLastName(admin.getLastName());
		person.setMobilePhone(site.getCompanyTelephone());
		person.setPhoneNumber(site.getCompanyTelephone());
		person.setSite(site);
		
		admin.setMedereEntity(person);
		
		return admin;
	}

	public boolean isNewSite() {
		return newSite;
	}

	public void setNewSite(boolean newSite) {
		this.newSite = newSite;
	}
	
	
}
