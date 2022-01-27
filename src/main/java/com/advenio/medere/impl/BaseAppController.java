package com.advenio.medere.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.advenio.medere.IAppController;
import com.advenio.medere.objects.dto.LanguageDTO;
import com.advenio.medere.objects.dto.users.RegionalSettingsDTO;
import com.vaadin.flow.i18n.I18NProvider;

@Component
public class BaseAppController implements IAppController, I18NProvider {

	private static final long serialVersionUID = -671369395114970191L;

	@Value("${medere.defaultLangCode:es}")
	private String defaultLangCode;
	
	@Value("${medere.appPath:}")
	private String appPath;
	
	@Value("${medere.appTitle:Medere UI}")
	private String appTitle;
	
	protected HashMap<String, ResourceBundle> i18nMessages;
	protected List<LanguageDTO> activeLanguages;
	protected LanguageDTO defaultLanguage;
	protected RegionalSettingsDTO defaultRegionalSettings;
	
	@PostConstruct
	public void init() {
		
		activeLanguages = new ArrayList<LanguageDTO>();
		
		LanguageDTO lang = new LanguageDTO();
		lang.setCode("es");
		lang.setLanguage(Long.valueOf(1));
		lang.setName("Español");
		activeLanguages.add(lang);

		lang = new LanguageDTO();
		lang.setCode("en");
		lang.setLanguage(Long.valueOf(2));
		lang.setName("English");
		activeLanguages.add(lang);
		
		Optional<LanguageDTO> oLang = activeLanguages.stream()
				.filter(e -> e.getCode().equalsIgnoreCase(defaultLangCode)).findFirst();
		if (oLang.isPresent()) {
			defaultLanguage = oLang.get();
		}
		
		i18nMessages = new HashMap<String, ResourceBundle>();
		for (LanguageDTO l : activeLanguages) {
			ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(l.getCode()));
			i18nMessages.put(l.getCode(), rb);
		}
		
		defaultRegionalSettings = new RegionalSettingsDTO();
		defaultRegionalSettings.setCurrencySymbolString("$");
		defaultRegionalSettings.setDateTimeFormatString("dd/MM/yyyy HH:mm");
		defaultRegionalSettings.setDecimalSeparator(',');
		defaultRegionalSettings.setGroupingSeparator('.');
		defaultRegionalSettings.setLocale(new Locale(defaultLangCode));
		defaultRegionalSettings.setShortDateFormatString("dd/MM/yyyy");
		defaultRegionalSettings.setTimeFormatString("HH:mm"); 
	}
	
	@Override
	public String getI18nMessage(String key, String lang) {
		if (i18nMessages.containsKey(lang)) {
			ResourceBundle rb = i18nMessages.get(lang);
			return rb.getString(key);
		}
		return "";
	}

	@Override
	public boolean isLangAvailable(String code) {
		for (LanguageDTO l : activeLanguages) {
			if (code.equals(l.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDefaultLangCode() {
		return defaultLangCode;
	}

	@Override
	public String getApptitle() {		
		return appTitle;
	}

	@Override
	public String getAppPath() {
		return appPath;
	}

	@Override
	public List<Locale> getProvidedLocales() {
		return Arrays.asList(Locale.ENGLISH, new Locale("es"));
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		if (i18nMessages.containsKey(locale.getLanguage())) {
			ResourceBundle rb = i18nMessages.get(locale.getLanguage());
			return rb.getString(key);
		}
		return "";
	}

	@Override
	public List<LanguageDTO> getActiveLanguages() {
		return activeLanguages;
	}

	
}