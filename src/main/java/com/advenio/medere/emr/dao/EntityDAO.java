package com.advenio.medere.emr.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.Hibernate.HibernateCriteriaQueryBuilder;
import com.advenio.medere.dao.exceptions.PersistException;
import com.advenio.medere.dao.nativeSQL.FieldDataRequest;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.nativeSQL.ParameterData;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.dto.DiseaseDTO;
import com.advenio.medere.emr.dao.dto.SiteDTO;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.objects.State.States;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.location.City;
import com.advenio.medere.objects.location.Country;
import com.advenio.medere.objects.location.Province;
import com.advenio.medere.objects.location.i18n.CityI18n;
import com.advenio.medere.objects.location.i18n.CountryI18n;
import com.advenio.medere.objects.location.i18n.ProvinceI18n;

@Repository
@Transactional
public class EntityDAO {

	protected static final Logger logger = LoggerFactory.getLogger(EntityDAO.class);

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	protected NativeSQLQueryBuilder nativeQueryBuilder;
	
	@Autowired
	protected HibernateCriteriaQueryBuilder hibernateCriteriaQueryBuilder;

	@Transactional(readOnly = true)
	public List<City> loadCities(String filter, int firstRow, int pageSize, Language language) {

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<City> criteria = builder.createQuery(City.class);
		Root<City> root = criteria.from(City.class);
		Join<City, CityI18n> joinCity = root.join("cityI18ns");

		if ((filter != null) && (!filter.trim().isEmpty())) {
			criteria.where(builder.like(builder.lower(joinCity.get("description")), "%" + filter.toLowerCase() + "%"));
		}
		criteria.orderBy(builder.asc(joinCity.get("description")));

		TypedQuery<City> query = entityManager.createQuery(criteria);
		query.setFirstResult(firstRow);
		query.setMaxResults(pageSize);

		final List<City> result = query.getResultList();
		return result;

	}

	@Transactional(readOnly = true)
	public Long countCities(String filter, Language language) {

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<City> root = criteria.from(City.class);
		Join<City, CityI18n> joinCity = root.join("cityI18ns");

		criteria.select(builder.count(root));

		if ((filter != null) && (!filter.trim().isEmpty())) {
			criteria.where(builder.like(builder.lower(joinCity.get("description")), "%" + filter.toLowerCase() + "%"));
		}
		return Long.valueOf(entityManager.createQuery(criteria).getSingleResult());
	}
	
	@Transactional(readOnly = true)
	public City findCityComplete(long cityID) {
		City city = entityManager.find(City.class, cityID);
		city.getProvince();
		city.getProvince().getProvinceI18ns();
		city.getProvince().getCountry();
		city.getProvince().getCountry().getCountryI18ns();
		return city;
	}

	@Transactional(readOnly = true)
	public List<Country> findAllCountries(Language language) {

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Country> criteria = builder.createQuery(Country.class);
		Root<Country> root = criteria.from(Country.class);
		Join<Country, CountryI18n> joinI18n = root.join("countryI18ns");
		criteria = criteria.where(builder.equal(joinI18n.get("language"), language.getLanguage()));
		criteria = criteria.orderBy(builder.asc(joinI18n.get("description")));

		TypedQuery<Country> query = entityManager.createQuery(criteria);
		final List<Country> result = query.getResultList();
		return result;

	}

	@Transactional(readOnly = true)
	public List<Country> findCountriesWithCities(Language language) {

		List<Country> lCountry = entityManager.createQuery(
				"select distinct i.country from Country c" + " inner join CountryI18n i on c.country = i.country"
						+ " inner join Province p on p.country = c.country" + " where i.language = :language",
				Country.class).setParameter("language", language).getResultList();

		return lCountry;
	}

	@Transactional(readOnly = true)
	public List<Province> loadProvinces(String filter, int firstRow, int pageSize, Language language, Country country) {

		if (country == null) {
			return new ArrayList<Province>();
		}

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Province> criteria = builder.createQuery(Province.class);
		Root<Province> root = criteria.from(Province.class);
		Join<Province, ProvinceI18n> joinProvince = root.join("provinceI18ns");

		if ((filter != null) && (!filter.trim().isEmpty())) {
			criteria.where(builder.and(builder.equal(root.get("country"), country.getCountry()),
					builder.equal(joinProvince.get("language"), language.getLanguage()),
					builder.like(builder.lower(joinProvince.get("description")), "%" + filter.toLowerCase() + "%")));
		} else {
			criteria.where(builder.equal(root.get("country"), country.getCountry()));
		}
		criteria.orderBy(builder.asc(joinProvince.get("description")));

		TypedQuery<Province> query = entityManager.createQuery(criteria);
		query.setFirstResult(firstRow);
		query.setMaxResults(pageSize);

		final List<Province> result = query.getResultList();

		return result;
	}

	@Transactional(readOnly = true)
	public Long countProvinces(String filter, Language language, Country country) {

		if (country == null) {
			return Long.valueOf(0);
		}

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Province> root = criteria.from(Province.class);
		Join<Province, ProvinceI18n> joinProvince = root.join("provinceI18ns");

		criteria.select(builder.count(root));

		if ((filter != null) && (!filter.trim().isEmpty())) {
			criteria.where(builder.and(builder.equal(root.get("country"), country.getCountry()),
					builder.equal(joinProvince.get("language"), language.getLanguage()),
					builder.like(builder.lower(joinProvince.get("description")), "%" + filter.toLowerCase() + "%")));
		} else {
			criteria.where(builder.equal(root.get("country"), country.getCountry()));
		}
		return Long.valueOf(entityManager.createQuery(criteria).getSingleResult());
	}

	@Transactional(readOnly = true)
	public List<City> loadCities(String filter, int firstRow, int pageSize, Language language, Province province) {

		if (province == null) {
			return new ArrayList<City>();
		}

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<City> criteria = builder.createQuery(City.class);
		Root<City> root = criteria.from(City.class);
		Join<City, CityI18n> joinCity = root.join("cityI18ns");

		if ((filter != null) && (!filter.trim().isEmpty())) {
			criteria.where(builder.and(builder.equal(root.get("province"), province.getProvince()),
					builder.equal(joinCity.get("language"), language.getLanguage()),
					builder.like(builder.lower(joinCity.get("description")), "%" + filter.toLowerCase() + "%")));
		} else {
			criteria.where(builder.equal(root.get("province"), province.getProvince()));
		}
		criteria.orderBy(builder.asc(joinCity.get("description")));

		TypedQuery<City> query = entityManager.createQuery(criteria);
		query.setFirstResult(firstRow);
		query.setMaxResults(pageSize);

		final List<City> result = query.getResultList();
		return result;

	}

	@Transactional(readOnly = true)
	public Long countCities(String filter, Language language, Province province) {

		if (province == null) {
			return Long.valueOf(0);
		}

		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<City> root = criteria.from(City.class);
		Join<City, CityI18n> joinCity = root.join("cityI18ns");

		criteria.select(builder.count(root));

		if ((filter != null) && (!filter.trim().isEmpty())) {
			criteria.where(builder.and(builder.equal(root.get("province"), province.getProvince()),
					builder.equal(joinCity.get("language"), language.getLanguage()),
					builder.like(builder.lower(joinCity.get("description")), "%" + filter.toLowerCase() + "%")));
		} else {
			criteria.where(builder.equal(root.get("province"), province.getProvince()));
		}
		return Long.valueOf(entityManager.createQuery(criteria).getSingleResult());
	}
	
	@Transactional(readOnly = true)
	public Language loadLanguageByCode(String languageCode) {		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Language> criteriaLanguage = builder.createQuery(Language.class);
		Root<Language> rootLanguage = criteriaLanguage.from(Language.class);
		criteriaLanguage.where(builder.equal(rootLanguage.get("code"),languageCode));		
		return  entityManager.createQuery(criteriaLanguage).getSingleResult();
	}

	public List<Task> loadCreatedTasks(User owner) {
		return entityManager.createQuery("From Task WHERE owner = :user AND state.state =! :deleted",Task.class).setParameter("user", owner).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}

	public List<Task> loadMyTasks(User solver) {
		return entityManager.createQuery("From Task WHERE solver = :solver AND state.state =! :deleted",Task.class).setParameter("solver", solver).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}

	public List<Task> loadSectorsTasks(Sector sector) {
		return entityManager.createQuery("From Task WHERE sector = :sector AND state.state =! :deleted",Task.class).setParameter("sector", sector).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}
	
	public List<Sector> loadSectors() {
		return entityManager.createQuery("From Sector",Sector.class).getResultList();
	}

    public List<State> getStates() {
        return entityManager.createQuery("From State",State.class).getResultList();
    }

	public List<Task> loadSubtasks(Task task) {
		return entityManager.createQuery("From Task WHERE parentTask = :parent AND state.state =! :deleted",Task.class).setParameter("parent", task).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}

	public void deleteTask(Task item) {
		item.setState(entityManager.find(State.class, States.CANCELADO.getValue()));
		entityManager.merge(item);
	}

    public State getStateById(Long value) {
        return entityManager.createQuery("From State WHERE state = :state",State.class).setParameter("state", value).getResultList().get(0);
    }

    public void createTask(Task t) {
		entityManager.persist(t);
    }
}