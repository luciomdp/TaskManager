package com.advenio.medere.emr.ui;

import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.advenio.medere.IAppController;
import com.advenio.medere.objects.dto.users.UserDTO;
import com.advenio.medere.security.LoginResponse;
import com.advenio.medere.security.SecurityConfig;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.services.users.IUserService;
import com.advenio.medere.ui.components.FlexBoxLayout;
import com.advenio.medere.ui.components.recaptcha.ReCaptcha;
import com.advenio.medere.ui.util.UIUtils;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.textfield.Autocapitalize;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route(value = "login")
//@PWA(iconPath = "/img/logo.png", name = "", shortName = "")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@CssImport("./styles/views/login.css")
@PreserveOnRefresh
@Push(transport = Transport.WEBSOCKET_XHR,value = PushMode.AUTOMATIC)
public class LoginView extends FlexLayout implements HasDynamicTitle,AfterNavigationObserver,BootstrapListener, HasUrlParameter<String>{

	private static final long serialVersionUID = -2099613514951356768L;

	protected static final Logger logger = LoggerFactory.getLogger(LoginView.class);

	@Value("${recaptcha.privatekey}")
	protected String privatekey;

	@Value("${recaptcha.sitekey}")
	protected String sitekey;
	
	@Value("${rememberMeEnabled:true}")
	protected boolean rememberMeEnabled;
	
	@Value("${forgotPasswordEnabled:true}")
	protected boolean forgotPasswordEnabled;

	@Value("${showbrandlogo:true}")
	protected boolean showbrandlogo;
	
	@Autowired protected ISessionManager vaadinSessionManager;
	@Autowired protected IAppController appController;
	@Autowired protected ApplicationContext appContext;
	@Autowired protected IUserService userService;

	protected String currentStyle = "";
	protected TextField username;
	protected PasswordField password;
	protected Checkbox rememberMe;
	protected Button login;
	protected VerticalLayout loginLayout;
	protected VerticalLayout vlLogin;
	protected ReCaptcha reCaptcha;
	protected Image imageLogo;
	protected Image imageLogoBrand;
	protected FlexBoxLayout centeringLayout ;

	protected Map<String, List<String>> parametersMap;
	protected VerticalLayout vlContainer;
	protected HorizontalLayout welcomeLayout;

	protected ComboBox<UserDTO> cboUsers;
	protected List<UserDTO> users;
	protected Button btnOk;
	protected Button cancelButton;

	public void changeBackground() {
		if (!currentStyle.isEmpty()) {
			removeClassName(currentStyle);
		}
		int number = new Random().nextInt(12);
		currentStyle = "loginbg" + Integer.toString(number);
		addClassName(currentStyle);
	}

	@PostConstruct
	protected void createComponents() {
		VaadinSession.getCurrent().setErrorHandler((ErrorHandler) errorEvent -> {
			logger.error("Uncaught UI exception", errorEvent.getThrowable());
			//Notification.show("We are sorry, but an internal error occurred");
		});

		changeBackground();
		setSizeFull();

		UI.getCurrent().getPage().setTitle(appController.getApptitle());
		setId("LoginView");
		welcomeLayout= createWelcomeLayout();

		loginLayout = new VerticalLayout();
		loginLayout.addClassName("login-panel");
		loginLayout.setWidthFull();
		buildLoginForm();
		
		vlContainer = new VerticalLayout();
		vlContainer.setSizeUndefined();

		vlContainer.add(welcomeLayout,loginLayout);
		vlContainer.addClassName("content-form");

		// layout to center login form when there is sufficient screen space
		centeringLayout = new FlexBoxLayout();
		centeringLayout.setSizeFull();
		centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		centeringLayout.setAlignItems(Alignment.CENTER);

		centeringLayout.add(vlContainer);
		add(centeringLayout);

		if (vaadinSessionManager.isSessionNeedsCaptcha()) {
			if (reCaptcha == null) {
				reCaptcha = new ReCaptcha(sitekey, privatekey);
				loginLayout.add(reCaptcha);
			}
		}
		 
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		if (imageLogo != null) { //as the logo is dynamic, it is need to reload again
			UIUtils.getLogoImage(imageLogo);
		}
		
		if (imageLogoBrand != null) { //as the logo is dynamic, it is need to reload again
			UIUtils.getLogoBrandImage(imageLogoBrand);
		}
		UI.getCurrent().getPage().setTitle(appController.getApptitle());
	}

	private void buildLoginForm() {
		/*********************** CONTROLS ********************************/
		// Create a button
		Button forgotPassword = new Button(vaadinSessionManager.getI18nMessage("ForgotPassword"));
		forgotPassword.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		forgotPassword.addThemeVariants(ButtonVariant.LUMO_SMALL);
		forgotPassword.getStyle().set("color", "grey");
		forgotPassword.addClickListener(event -> recoverPassword());

		username = new TextField(vaadinSessionManager.getI18nMessage("Username"));
		username.setWidthFull();
		username.setAutocorrect(false);
		username.setAutocapitalize(Autocapitalize.NONE);
		username.setAutocomplete(Autocomplete.USERNAME);

		password = new PasswordField(vaadinSessionManager.getI18nMessage("Password"));
		password.setWidthFull();
		password.setAutocomplete(Autocomplete.CURRENT_PASSWORD);
		password.setAutocapitalize(Autocapitalize.NONE);
		password.setAutocorrect(false);

		login = new Button("Login");
		login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		login.setDisableOnClick(true);
		login.addClickListener(event -> login());
		login.setWidthFull();

		/******************************************************************/

		/**************************** LAYOUT ********************************/
		rememberMe = new Checkbox(vaadinSessionManager.getI18nMessage("RememberMe"), false);
		rememberMe.setWidthFull();
		rememberMe.getElement().getStyle().set("font-size", "small");
		rememberMe.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<Boolean>>() {
			@Override
			public void valueChanged(ValueChangeEvent<Boolean> event) {
				vaadinSessionManager.setRememberMe(event.getValue().booleanValue());
			}
		});

		vlLogin = new VerticalLayout();
		vlLogin.setPadding(false);
		vlLogin.setMargin(false);
		vlLogin.setSizeFull();
		vlLogin.add(username);
		vlLogin.add(password);
		vlLogin.add(login);
		
		
		
		if (showbrandlogo) {
			imageLogoBrand = new Image();
			imageLogoBrand.addClassName("logobrand");
			
			HorizontalLayout hlLogoBrand = new HorizontalLayout();
			hlLogoBrand.setSizeFull();
			
			if (rememberMeEnabled  || forgotPasswordEnabled) {
				VerticalLayout vlControls = new VerticalLayout();
				vlControls.setSizeFull();
				vlControls.setMargin(false);
				vlControls.setPadding(false);
				if (rememberMeEnabled) {
					vlControls.add(rememberMe);	
				}
				
				if (forgotPasswordEnabled) {
					vlControls.add(forgotPassword);	
				}	
				hlLogoBrand.add(vlControls);				
			}
								
			hlLogoBrand.add(imageLogoBrand);
			hlLogoBrand.setDefaultVerticalComponentAlignment(Alignment.CENTER);
			hlLogoBrand.setJustifyContentMode(JustifyContentMode.END);
						
			vlLogin.add(hlLogoBrand);
		}else {
			if (rememberMeEnabled) {
				vlLogin.add(rememberMe);	
			}
			
			if (forgotPasswordEnabled) {
				vlLogin.add(forgotPassword);	
			}
		}
		
		loginLayout.removeAll();
		loginLayout.add(vlLogin);
		loginLayout.setFlexGrow(1, vlLogin);

		vlLogin.getElement().addEventListener("keypress", event -> login()).setFilter("event.key == 'Enter'");
	}

	private void login() {
		
		if (StringsUtils.isNullOrEmptyTrimmed(username.getValue())) {
			UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("EnterTheUsername"),2000,Position.MIDDLE);
			enableLoginButton();
			return;
		}

		if (StringsUtils.isNullOrEmptyTrimmed(password.getValue())) {
			UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("EnterUserPassword"),2000,Position.MIDDLE);
			enableLoginButton();
			return;
		}

		if (vaadinSessionManager.isSessionNeedsCaptcha()) {
			if (reCaptcha!=null && !reCaptcha.validate().isValid()) {
				UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("PleaseMarkTheCheckbox"),2000,Position.MIDDLE);
				enableLoginButton();
				return;
			}
		}

		ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
		/// login
		try {
			LoginResponse lr = vaadinSessionManager.doLogin(username.getValue(), password.getValue(),
					webBrowser.getAddress(), servletContext,rememberMe.getValue());
			if (lr.isLoginOK()) {
				UI.getCurrent().navigate(appController.getAppPath());				
			} else {
				setLoginFail();
				enableLoginButton();
			}
		} catch (ResourceAccessException rce) {
			UI.getCurrent().access(() -> {
				password.setValue("");
				UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("CommunicationErrorCaptionMessage"),4000, null );
			});
			enableLoginButton();
		} catch (Exception e) {
			UI.getCurrent().access(() -> {
				password.setValue("");
				UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("Error"),4000, null );
			});
			enableLoginButton();
		}
	}

	private void recoverPassword() {
		/*********************** CONTROLS ********************************/
		Label lblRecoverPassword = UIUtils.createH4Label(vaadinSessionManager.getI18nMessage("ResetPassword"));
		lblRecoverPassword.setSizeFull();
		Label enterEmail =  new Label(vaadinSessionManager.getI18nMessage("EnterEmailForNewPass"));
		enterEmail.setSizeFull();

		TextField email = new TextField();
		email.setPlaceholder(vaadinSessionManager.getI18nMessage("Email"));
		email.setWidthFull();
		email.addClassName("marginRight10pxResponsive");
		email.setValueChangeMode(ValueChangeMode.EAGER);
		email.addValueChangeListener(event -> {
			if(cboUsers.isVisible()) {
				cboUsers.clear();
				cboUsers.setVisible(false);
				users= null;
			}
		});

		cboUsers = new ComboBox<UserDTO>(vaadinSessionManager.getI18nMessage("Users"));
		cboUsers.setItemLabelGenerator(UserDTO::getFullName);
		cboUsers.setClearButtonVisible(true);
		cboUsers.setVisible(false);
		cboUsers.setWidthFull();
		
		btnOk = new Button(vaadinSessionManager.getI18nMessage("SendRecoveryCode"));
		btnOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnOk.setDisableOnClick(true);
		btnOk.addClickListener(event -> sendRecoveryEmail(email.getValue()));
		btnOk.addClassName("buttonAutoWith");
		btnOk.addClassName("marginLeft10pxResponsive");
		btnOk.getStyle().set("margin-right", "10px");

		cancelButton = new Button(vaadinSessionManager.getI18nMessage("Cancel"));
		cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancelButton.addClickListener(event -> buildLoginForm());
		cancelButton.addClassName("marginLeft10pxResponsive");
		cancelButton.addClassName("buttonAutoWith");

		/*******************************************************************/

		/**************************** LAYOUT *******************************/

		VerticalLayout vlRecover = new VerticalLayout();
		vlRecover.setPadding(false);
		vlRecover.setMargin(false);
		vlRecover.setSizeFull();
		vlRecover.add(lblRecoverPassword);
		vlRecover.add(enterEmail);
		vlRecover.setHorizontalComponentAlignment(Alignment.CENTER, enterEmail);
	
		Div fl = new Div();
		fl.add(email,cboUsers);
		fl.setSizeFull();
		vlRecover.add(fl);

		Div divButton = new Div();
		divButton.setSizeFull();
		divButton.addClassName("responsiveOneLine");
		divButton.add(btnOk);
		divButton.add(cancelButton);

		vlRecover.add(divButton);

		loginLayout.removeAll();
		loginLayout.add(vlRecover);
		vlRecover.getElement().addEventListener("keypress", event -> sendRecoveryEmail(email.getValue())).setFilter("event.key == 'Enter'");
	}

	protected void sendRecoveryEmail(String email) {
		if (StringsUtils.isNullOrEmptyTrimmed(email)) {
			UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("PleaseCompleteEmail"),2000,Position.TOP_CENTER);
			btnOk.setEnabled(true);
			return;
		}
		try {
			if (users == null) {
				users = userService.loadUsersByEmail(email);
				sendRecoveryEmail(email);
				return;
			} else if (users.size() > 1 && !cboUsers.isVisible() ) {
				cboUsers.setDataProvider(DataProvider.ofCollection(users));
				cboUsers.setVisible(true);
			}

			if (cboUsers.isVisible() && cboUsers.getValue()==null) {
				UIUtils.showNotification(vaadinSessionManager.getI18nMessage("PleaseSelectUserToChangePassword"),0,Notification.Position.MIDDLE,NotificationVariant.LUMO_PRIMARY);
				btnOk.setEnabled(true);
				return;
			}

			userService.sendRecoveryEmail(users.size() > 1?cboUsers.getValue().getUsername():users.get(0).getUsername());

			Dialog dialog = new Dialog();
			dialog.setCloseOnEsc(false);
			VerticalLayout vlDialog = new VerticalLayout();

			Label textHead = new Label(vaadinSessionManager.getI18nMessage("PasswordResetEmailSent"));
			textHead.addClassName("h4");

			Label textContent = new Label(String.format(vaadinSessionManager.getI18nMessage("EmailSentToResetPassword"),email ));

			btnOk = new Button(vaadinSessionManager.getI18nMessage("Accept"));
			btnOk.addClickListener(e -> {
				buildLoginForm();
				dialog.close();
			});
			btnOk.getElement().setAttribute("theme", "primary");

			vlDialog.add(textHead);
			vlDialog.add(textContent);
			vlDialog.add(btnOk);
			vlDialog.setPadding(false);
			vlDialog.setHorizontalComponentAlignment(Alignment.START, btnOk);

			dialog.add(vlDialog);
			dialog.open();

		} catch (HttpClientErrorException e) {
			if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("EmailNotRegistered"),3000,null);
				btnOk.setEnabled(true);
				return;
			}
			if(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
				UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("OperationError"),3000,null);
				btnOk.setEnabled(true);
				return;
			}
		}
	}

	private HorizontalLayout createWelcomeLayout() {
		Label welcome = new Label(vaadinSessionManager.getI18nMessage("Welcome"));
		welcome.setSizeFull();
		welcome.getStyle().set("font-size", "150%");
		welcome.getStyle().set("color", "steelblue");

		imageLogo =  UIUtils.getLogoImage();
		imageLogo.addClassName("logo");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.setHeight("3em");
		hl.setMargin(false);
		hl.add(welcome);
		hl.add(imageLogo);
		hl.setVerticalComponentAlignment(Alignment.CENTER, welcome);
		hl.getStyle().set("background", "white");
		return hl;
	}

	protected void enableLoginButton() {
		UI.getCurrent().access(() -> {
			login.setEnabled(true);
		});
	}

	protected void setLoginFail() {
		UI.getCurrent().access(() -> {
			password.setValue("");
			UIUtils.showErrorNotification(vaadinSessionManager.getI18nMessage("ErrorLogin"),2000,null);

			if (vaadinSessionManager.isSessionNeedsCaptcha()) {
				if (reCaptcha != null) {
					loginLayout.remove(reCaptcha);
				}
				reCaptcha = new ReCaptcha(sitekey, privatekey);
				loginLayout.add(reCaptcha);
			}
		});
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (SecurityConfig.isUserLoggedIn()) {
			UI.getCurrent().navigate(appController.getAppPath());
		}

		UI.getCurrent().getPage().setTitle(appController.getApptitle());
	}

	@Override
	public String getPageTitle() {
		return appController.getApptitle();
	}

	@Override
	public void setParameter(BeforeEvent event,@OptionalParameter String parameter) {
		Location location = event.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();
		parametersMap = queryParameters.getParameters();
	}

	@Override
	public void modifyBootstrapPage(BootstrapPageResponse response) {
		Document document = response.getDocument();
		Element head = document.head();

		head.appendChild(createMeta(document, "apple-mobile-web-app-capable", "yes"));
		head.appendChild(createMeta(document, "apple-mobile-web-app-status-bar-style", "black"));

		LoadingIndicatorConfiguration conf = response.getUI().getLoadingIndicatorConfiguration();

		head.append("<link rel=\"shortcut icon\" href=\"img/favicon.ico\">");
		
	}
	
	private Element createMeta(Document document, String property, String content) {
		Element meta = document.createElement("meta");
		meta.attr("property", property);
		meta.attr("content", content);
		return meta;
	}
}