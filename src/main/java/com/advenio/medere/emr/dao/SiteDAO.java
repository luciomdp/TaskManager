package com.advenio.medere.emr.dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.DAOUtils;
import com.advenio.medere.dao.nativeSQL.FieldDataRequest;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.nativeSQL.ParameterData;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.dto.SiteDTO;
import com.advenio.medere.emr.objects.user.UserEMR;
import com.advenio.medere.objects.site.Site;

@Repository
@Transactional
public class SiteDAO {
	
	@Autowired
	protected DAOUtils daoUtils;
	
	@Autowired
	protected NativeSQLQueryBuilder nativeQueryBuilder;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Transactional
	public void updateSite(Site site) {
		entityManager.merge(site);
	}
	
	@Transactional
	public Long saveSite(Site site,UserEMR user) {
		entityManager.persist(site);
		if(user != null) {
			user.setSite(site);
			user.getMedereEntity().setSite(site);
			entityManager.persist(user.getMedereEntity());
			entityManager.persist(user);
		}
		return site.getSite();
	}
	
	@Transactional(readOnly=true)
	public Site loadSite(long siteId) {
		return entityManager.find(Site.class, siteId);
	}
	
	@Transactional(readOnly=true)
	public Site loadDefaultSite() {
		return entityManager.find(Site.class, (long)1);
	}
	
	public Page<SiteDTO> loadSites(PageLoadConfig<SiteDTO> loadconfig,Long language) {
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
        ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
        Page page = nativeQueryBuilder.runReport(loadconfig.getStartIndex(), loadconfig.getPageSize(),
        		loadconfig.getSortingList(), loadconfig.getFilters(), "loadsites",
        		language, params, false, true, fieldDataRequest);
        return page;
	}
	
	public int countSites(PageLoadConfig<SiteDTO> loadconfig,Long language) {
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
        ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
        Page page = nativeQueryBuilder.runReport(loadconfig.getStartIndex(), loadconfig.getPageSize(),
        		loadconfig.getSortingList(), loadconfig.getFilters(), "loadsites",
        		language, params, true, false, fieldDataRequest);
        return page.getCount();
	}

}
