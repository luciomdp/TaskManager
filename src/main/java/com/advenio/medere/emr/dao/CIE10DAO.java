package com.advenio.medere.emr.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.exceptions.PersistException;
import com.advenio.medere.dao.nativeSQL.FieldDataRequest;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.nativeSQL.ParameterData;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.dto.DiseaseDTO;
import com.advenio.medere.emr.objects.Disease;

@Repository
@Transactional
public class CIE10DAO {

	@Autowired
	protected NativeSQLQueryBuilder nativeQueryBuilder;
	@PersistenceContext
	protected EntityManager entityManager;

	protected static final Logger logger = LoggerFactory.getLogger(CIE10DAO.class);
	@Transactional
	public void saveDisease(Disease d) throws PersistException {
		entityManager.persist(d);
	}
	
	@Transactional
	public void updateDisease(Disease d) throws PersistException {
		entityManager.merge(d);
	}
	@Transactional
	public void deleteDisease(Long diseaseId) throws PersistException {
		entityManager.remove(findCie10ById(diseaseId));
	}
	
	public Disease findCie10ById(long diseaseId) {
		StringBuilder queryStr = new StringBuilder();
		queryStr.append("select * from Disease where disease = :diseaseid");
		return (Disease) entityManager.createNativeQuery(queryStr.toString(), Disease.class)
				.setParameter("diseaseid", diseaseId).getResultList().get(0);
	}
	
	public List<Disease> findAllCie10() {
		return entityManager.createNativeQuery("select * from Disease", Disease.class).getResultList();
	}
	
	public Page<DiseaseDTO> loadCIE10(PageLoadConfig<DiseaseDTO> loadconfig,Long language) {
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
        ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
        Page page = nativeQueryBuilder.runReport(loadconfig.getStartIndex(), loadconfig.getPageSize(),
        		loadconfig.getSortingList(), loadconfig.getFilters(), "loaddiseases",
        		language, params, false, true, fieldDataRequest);
        return page;
	}
	
	public int countCIE10(PageLoadConfig<DiseaseDTO> loadconfig,Long language) {
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
        ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
        Page page = nativeQueryBuilder.runReport(loadconfig.getStartIndex(), loadconfig.getPageSize(),
        		loadconfig.getSortingList(), loadconfig.getFilters(), "loaddiseases",
        		language, params, true, false, fieldDataRequest);
        return page.getCount();
	}

}