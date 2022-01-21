package com.advenio.medere.emr.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.annotation.CacheResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.objects.DocumentType;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.location.City;
import com.advenio.medere.objects.user.Profile;

@Repository
@Transactional
public class MedereDAO {

	@PersistenceContext
	protected EntityManager entityManager;

	@Transactional(readOnly = true)
	@CacheResult(cacheName = "languages")
	public Language loadLanguage(Long language) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("org.hibernate.cacheable", true);
		return entityManager.find(Language.class, language, props);
	}

	@Transactional(readOnly = true)
	public City loadCity(long city) {
		return entityManager.find(City.class, city);
	}

	@Transactional(readOnly = true)
	@CacheResult(cacheName = "documenttypes")
	public DocumentType loadDocumentType(long documenType) {
		return entityManager.find(DocumentType.class, documenType);
	}
	
	@Transactional(readOnly = true)
	@CacheResult(cacheName = "documenttypes")
	public List<DocumentType> loadDocumentTypes() {
		return entityManager.createQuery("From DocumentType", DocumentType.class).setHint("org.hibernate.cacheable", "true")
				.getResultList();
	}

	@Transactional(readOnly = true)
	@CacheResult(cacheName = "languages")
	public List<Language> loadLanguages() {
		return entityManager.createQuery("From Language", Language.class).setHint("org.hibernate.cacheable", "true")
				.getResultList();
	}

	@Transactional(readOnly = true)
	@CacheResult(cacheName = "profile")
	public List<Profile> loadProfiles() {
		List<Profile> profiles = entityManager.createQuery("From Profile p inner join fetch p.homePage", Profile.class).getResultList();
		for (Profile p : profiles) {
			if (p.getHomePage() != null) {
				p.getHomePage().getName();
			}
			p.getItems().size();
			p.getAllowedActions().size();
			p.getWidgets().size();
		}
		return profiles;
	}
}
