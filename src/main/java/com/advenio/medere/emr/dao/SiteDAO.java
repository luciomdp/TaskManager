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

	@Transactional
	public Integer copyNomeclators (long fromSite, long toSite){
		StringBuilder sb = new StringBuilder();
		sb.append("with fromSite as (" +
				" select " + fromSite + " as siteId" +
				")," +
				"toSite as (" +
				" select " + toSite + " as siteId" +
				")," +
				"tmp_nomenclator as (" +
				"SELECT " +
				"        nomenclator.nomenclatortype," +
				"        nextval('nomenclator_nomenclator_seq') as nomenclator," +
				"        nomenclator.nomenclator as id," +
				"        nomenclator.abbreviation," +
				"        nomenclator.active," +
				"        nomenclator.code," +
				"        nomenclator.description," +
				"        nomenclator.studytype," +
				"        nomenclator.healthentity," +
				"        nomenclator.relatednomenclator," +
				"        nomenclator.nomenchierarchytype," +
				"        nomenclator.defaultprice," +
				"        nomenclator.nomenclated," +
				"        (select siteId from toSite) as site" +
				"    from nomenclator inner join fromSite on fromSite.siteId = nomenclator.site " +
				")," +
				"tmp_nomenclator_determination as (" +
				"SELECT " +
				"        tn.nomenclator as nomenclator," +
				"        nd.determination as determination" +
				"    FROM" +
				"        public.nomenclator_determination as nd inner join tmp_nomenclator as tn on nd.nomenclator = tn.id" +
				")" +
				"," +
				"nomenclator_res as (" +
				"    INSERT INTO" +
				"        public.nomenclator(" +
				"        nomenclatortype," +
				"        nomenclator," +
				"        abbreviation," +
				"        active," +
				"        code," +
				"        description," +
				"        studytype," +
				"        healthentity," +
				"        relatednomenclator," +
				"        nomenchierarchytype," +
				"        defaultprice," +
				"        nomenclated," +
				"        site)" +
				"    select nomenclatortype," +
				"        nomenclator," +
				"        abbreviation," +
				"        active," +
				"        code," +
				"        description," +
				"        studytype," +
				"        healthentity," +
				"        relatednomenclator," +
				"        nomenchierarchytype," +
				"        defaultprice," +
				"        nomenclated," +
				"        site" +
				"    from tmp_nomenclator" +
				"    returning *" +
				")," +
				"determination_res as (" +
				"    INSERT INTO nomenclator_determination(nomenclator, determination)" +
				"    select nomenclator, determination from tmp_nomenclator_determination" +
				"    returning *" +
				") select * from nomenclator_res");
		System.out.println(sb.toString());
		//entityManager.createNativeQuery(sb.toString()).setParameter("fromSite", fromSite).setParameter("toSite", toSite).executeUpdate();
		int result = entityManager.createNamedStoredProcedureQuery();   //createNativeQuery(sb.toString()).executeUpdate();
		return 1;
	}

}
