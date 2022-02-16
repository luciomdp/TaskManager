package com.advenio.medere.emr.dao;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.DAOUtils;
import com.advenio.medere.dao.exceptions.PersistException;
import com.advenio.medere.dao.nativeSQL.FieldDataRequest;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.nativeSQL.ParameterData;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.dto.NutritionalItemDTO;
import com.advenio.medere.emr.objects.NutritionalItem;
import com.advenio.medere.emr.objects.NutritionalType;
import com.advenio.medere.emr.objects.NutritionalTypeI18n;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.user.User;
import com.advenio.medere.server.session.VaadinSessionManager;

@Repository("nutritionalItemDAO")
@Transactional
public class NutritionalItemDAO {

	protected static final Logger logger = LoggerFactory.getLogger(NutritionalItemDAO.class);
	
	@Autowired
	protected DAOUtils daoUtils;
	@PersistenceContext
	protected EntityManager entityManager;		
	@Autowired
	protected NativeSQLQueryBuilder nativeQueryBuilder;

	public NutritionalItemDAO() {
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page <NutritionalItemDTO>loadNutritionalItemList(PageLoadConfig loadConfig,long language) throws PersistException{
		
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
		nativeQueryBuilder.addLanguageParam(params, language);
				
		ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
	
		Page<NutritionalItemDTO> page = nativeQueryBuilder.runReport(loadConfig.getStartIndex(), loadConfig.getPageSize(), loadConfig.getSortingList(), loadConfig.getFilters(), 
				                                   "nutritionalitemlist", language, params,true,true,fieldDataRequest);
		return page;	
	}		
	
	
	@Transactional(readOnly=true)
	public NutritionalItem findNutritionalItemById(Long numberID, Language language){
		
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("org.hibernate.cacheable", "true");
		
    	NutritionalItem item = entityManager.find(NutritionalItem.class, numberID,properties);	
		return item;
	}
	
	@Transactional
	public void saveNewNutritionalItem(NutritionalItem nutritionalItem) throws PersistException {
		daoUtils.save(nutritionalItem, false);
	}
	
	@Transactional
	public void updateNutritionalItem(NutritionalItem nutritionalItem) throws PersistException{		
		daoUtils.update(nutritionalItem, false);		
	}
	
	@Transactional
	public void deleteNutritionalItem(long numberID) throws PersistException{		
		NutritionalItem obj = entityManager.find(NutritionalItem.class, numberID);		
		daoUtils.delete(obj, false);
	}
	
	@Transactional(readOnly = true)
	public List<NutritionalType> loadNutritionalTypes(Language language) {
		List<NutritionalType> result = entityManager.createQuery("from NutritionalType", NutritionalType.class).getResultList();
		result.sort((e1, e2) -> e1.getDescription(language).compareTo(e2.getDescription(language)));
		return result;
	}	
	
}
