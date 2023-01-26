package com.advenio.medere.emr.dao;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.DAOUtils;
import com.advenio.medere.dao.SortingFieldInfo;
import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.dao.nativeSQL.FieldDataRequest;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.nativeSQL.ParameterData;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.dto.PrevScheduledMessageDTO;
import com.advenio.medere.emr.dao.dto.SiteDTO;
import com.advenio.medere.emr.objects.user.UserEMR;
import com.advenio.medere.objects.site.Site;
import com.advenio.medere.sender.objects.accounts.AccountMs;

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

	public List<PrevScheduledMessageDTO> loadPreviousScheduledMessagesForSite(Long siteId) {
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
		params.add(new ParameterData ().setParamName("site").setValue(siteId));
        return nativeQueryBuilder.runReport(0,0,  new ArrayList<SortingFieldInfo>(), new FilterList(),
		"loadpreviousscheduledmessagesforsite", Long.valueOf(1), params, false, true,
		new ArrayList<FieldDataRequest>()).getData();
	}

	@Transactional
	public void copyInfoBetweenSites (long fromSite, long toSite, boolean copyNomenclators, boolean copyHealthEntities,
									  boolean copyProfiles) {
		StringBuilder sb = new StringBuilder();
		sb.append("call sp_copyinfobetweensites(:fromsite, :tosite, :copyprofiles, :copyhealthentities,:copynomenclator)");
		entityManager.createNativeQuery(sb.toString())
				.setParameter("fromsite", fromSite)
				.setParameter("tosite", toSite)
				.setParameter("copyprofiles", copyProfiles)
				.setParameter("copyhealthentities", copyHealthEntities)
				.setParameter("copynomenclator", copyNomenclators)
				.executeUpdate();
	}

	public boolean hasAnyHealthEntity (long siteid){
		StringBuilder sb = new StringBuilder();
		sb.append("select count (m.medereentity) from medereentity m " +
				"where m.medereentitytype = 6 and m.site = :site");
		BigInteger qnty = (BigInteger)entityManager.createNativeQuery(sb.toString()).setParameter("site", siteid).getSingleResult();
		if (qnty.longValue() > 0)
			return true;
		else
			return false;
	}
	public boolean hasAnyProfile (long siteid){
		StringBuilder sb = new StringBuilder();
		sb.append("select count(p.profile) from profile p " +
				"where p.site = :site");
		BigInteger qnty = (BigInteger)entityManager.createNativeQuery(sb.toString()).setParameter("site", siteid).getSingleResult();
		if (qnty.longValue() > 0)
			return true;
		else
			return false;
	}
	public boolean hasAnyNomenclator (long siteid){
		StringBuilder sb = new StringBuilder();
		sb.append("select count(n.nomenclator) from nomenclator n " +
				"where n.site = :site");
		BigInteger qnty = (BigInteger)entityManager.createNativeQuery(sb.toString()).setParameter("site", siteid).getSingleResult();
		if (qnty.longValue() > 0)
			return true;
		else
			return false;
	}

    public void saveMessageSenderAccount(AccountMs body) {
		entityManager.merge(body);
    }

}
