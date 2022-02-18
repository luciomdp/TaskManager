package com.advenio.medere.emr.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.DAOUtils;
import com.advenio.medere.dao.exceptions.PersistException;
import com.advenio.medere.dao.nativeSQL.FieldDataRequest;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.nativeSQL.ParameterData;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.dto.GenericDrugDTO;
import com.advenio.medere.emr.objects.medicine.GenericDrug;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.user.User;
import com.advenio.medere.server.session.VaadinSessionManager;

@Repository
@Transactional
public class GenericDrugDAO {

	protected static final Logger logger = LoggerFactory.getLogger(GenericDrugDAO.class);
	@Autowired
	protected DAOUtils daoUtils;
	@PersistenceContext
	protected EntityManager entityManager;		
	@Autowired
	protected NativeSQLQueryBuilder nativeQueryBuilder;
	
	public GenericDrugDAO() {
	}
	
	@Transactional(readOnly = true)
	public List<GenericDrug> loadGenericDrugs(String filter, int firstRow, int pageSize) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GenericDrug> criteria = builder.createQuery(GenericDrug.class);
		Root<GenericDrug> root = criteria.from(GenericDrug.class);
		
		if ((filter != null) && (!filter.trim().isEmpty())) {
				criteria.where(builder.like(builder.lower(root.get("name")), "%" + filter.toLowerCase() + "%"));
		}
		criteria.orderBy(builder.asc(root.get("name")));

		TypedQuery<GenericDrug> query = entityManager.createQuery(criteria);
		query.setFirstResult(firstRow);
		query.setMaxResults(pageSize);

		final List<GenericDrug> result = query.getResultList();
		return result;
	}
	
	
	
public void processExternalDrugInfo() {
		
		//http://garrahan.gov.ar/vademecum/alfabetico_nquimico.php --> Indice alfabetico de Monodrogas de Garrahan
		List<GenericDrug> genericDrugs = loadGenericDrugs("", 0, 99999);
		Iterator<GenericDrug> it = genericDrugs.iterator();
		while (it.hasNext()) {
			GenericDrug gd = it.next();
			String strGenericDrug = Character.toUpperCase(gd.getName().charAt(0)) + gd.getName().substring(1, gd.getName().length());
			if (strGenericDrug.contains("acetilsalicilico")) {
				strGenericDrug = "Aspirina";
			}
		    strGenericDrug = strGenericDrug.replace("+", "%2B");
		    strGenericDrug = strGenericDrug.replace(" ", "+");
			executePost("http://garrahan.gov.ar/vademecum/vademec.php", "campo=nom_generico&ntexto=" + strGenericDrug, gd);
		}
	}
	@Transactional(readOnly=true)
	public GenericDrug findGenericDrugById(Long numberID){
		GenericDrug drug = entityManager.find(GenericDrug.class, numberID);		
		return drug;
	}

	@Transactional
	public void delete(long numberID) throws PersistException{		
		GenericDrug obj = entityManager.find(GenericDrug.class, numberID);		
		daoUtils.delete(obj, false);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page <GenericDrugDTO> loadGenericDrugList(PageLoadConfig loadConfig,Long languageId) throws PersistException{
		
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
//		params.add(new ParameterData().setParamName("language").setValue(currentUser.getLanguage().getLanguage()));

		ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
		Page page = nativeQueryBuilder.runReport(loadConfig.getStartIndex(), loadConfig.getPageSize(), loadConfig.getSortingList(), loadConfig.getFilters(), "genericdruglist", 
				languageId, params,true,true,fieldDataRequest);
		return page;		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int countGenericDrugList(PageLoadConfig loadConfig,Long languageId) throws PersistException{
		
		ArrayList<ParameterData> params = new ArrayList<ParameterData>();
//		params.add(new ParameterData().setParamName("language").setValue(currentUser.getLanguage().getLanguage()));

		ArrayList<FieldDataRequest> fieldDataRequest = new ArrayList<FieldDataRequest>();
		Page page = nativeQueryBuilder.runReport(loadConfig.getStartIndex(), loadConfig.getPageSize(), loadConfig.getSortingList(), loadConfig.getFilters(), "genericdruglist", 
				languageId, params,true,true,fieldDataRequest);
		return page.getCount();		
	}
	

	@Transactional
	public void saveNewGenericDrug(GenericDrug genericDrug) throws PersistException {
		entityManager.persist(genericDrug);	

	}
	
	public void executePost(String targetURL, String urlParameters, GenericDrug gd) {
		  HttpURLConnection connection = null;

		  try {  
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		    connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    String nextData = "";
		    boolean updateFlag = false;
		    boolean eof = false; //controla el caso en que se llegue al fin de la página en el WHILE interno
		    while (!eof && (line = rd.readLine()) != null) {
		      if (line.contains("<td>")) {
		    	  StringBuilder usefulLine = new StringBuilder(); // or StringBuffer if Java version 5+
		    	  usefulLine.append(line);
		    	  if (!line.contains("</td>")) { //controla el caso en el que </td> venga en la misma linea de <td>
		    		  while (((line = rd.readLine()) != null) && (!line.contains("</td>"))) {
		    			  if (line == null) {
		    				  eof = true;
		    			  }
		    			  else {
		    			     usefulLine.append(line);
		    			  }
		    		  }
	    			  if (line == null) {
	    				  eof = true;
	    			  }
	    			  else {
	    				  usefulLine.append(line);
	    			  }
		    	  }
		    	  line = usefulLine.toString();    	  
		    	  if (nextData.isEmpty()) {	    	
		    		  if ((line.contains("Nombre")) && (line.contains("Comercial"))) {
		    			  nextData = "commercialName";
		    		  }  
		    		  if ((line.contains("Comentario")) && (line.contains("Acci")) && (line.contains("Terap"))) {
		    			  nextData = "therapeuticComments";
		    		  }  
				      if (line.contains("Grupo")) {
				    	  nextData = "group";
				      }  
				      if (line.contains("Subgrupo")) {
				    	  nextData = "subgroup";
				      }  
				      if (line.contains("Dosis")) {
				    	  nextData = "dose";
				      }  
				      if ((line.contains("Vias")) && (line.contains("Aplicaci"))) {
				    	  nextData = "routes";
				      }  
				      if ((line.contains("Efectos")) && (line.contains("Adversos"))) {
				    	  nextData = "adverseEffects";
				      }  
				      if (line.contains("Observaci")) {
				    	  nextData = "observations";
				      }
				      if (!nextData.isEmpty()) {
				    	  updateFlag = true; //Persistir el objeto si al menos un dato se actualizó
				      }
		    	  }
		    	  else
		    	  {
		    		  line = line.replace("href=\"/", "href=\"http://garrahan.gov.ar/");
		    		  if (nextData.equals("commercialName")) {
		    			 gd.setCommercialNames(line); 
		    		  }
		    		  if (nextData.equals("group")) {
		    			  gd.setTherapeuticalGroup(line); 
		    		  }
		    		  if (nextData.equals("subgroup")) {
		    			  gd.setTherapeuticalSubgroup(line); 
		    		  }
		    		  if (nextData.equals("therapeuticComments")) {
		    			  gd.setTherapeuticComments(line); 
		    		  }
		    		  if (nextData.equals("dose")) {
		    			 gd.setDosageInformation(line); 
		    		  }
		    		  if (nextData.equals("routes")) {
		    			  gd.setRoutes(line); 
		    		  }
		    		  if (nextData.equals("adverseEffects")) {
		    			  gd.setAdverseEffects(line); 
		    		  }
		    		  if (nextData.equals("observations")) {
		    			  gd.setAdditionalObservations(line); 
		    		  }		    		  
		    		  nextData = "";
		    	  }
		      }
		    }
		    rd.close();
		    if (updateFlag) {
		    	daoUtils.save(gd, false);
		    }
		  } 
		  catch (Exception e) {
			  e.printStackTrace();
		  } 
		  finally {
			  if (connection != null) {
				  connection.disconnect();
			  }
		  }
	}

	public void updateGenericDrug(GenericDrug genericDrug) {
		// TODO Auto-generated method stub
		entityManager.merge(genericDrug);
		
	}	

}