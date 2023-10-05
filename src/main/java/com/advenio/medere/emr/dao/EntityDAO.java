package com.advenio.medere.emr.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.Hibernate.HibernateCriteriaQueryBuilder;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.emr.objects.Category;
import com.advenio.medere.emr.objects.Priority;
import com.advenio.medere.emr.objects.Profile;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.objects.State.States;
import com.advenio.medere.objects.Language;

@Repository
@Transactional
public class EntityDAO {

	private static final Logger logger = LoggerFactory.getLogger(EntityDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NativeSQLQueryBuilder nativeQueryBuilder;
	
	@Autowired
	private HibernateCriteriaQueryBuilder hibernateCriteriaQueryBuilder;

	
	@Transactional(readOnly = true)
	public Language loadLanguageByCode(String languageCode) {		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Language> criteriaLanguage = builder.createQuery(Language.class);
		Root<Language> rootLanguage = criteriaLanguage.from(Language.class);
		criteriaLanguage.where(builder.equal(rootLanguage.get("code"),languageCode));		
		return  entityManager.createQuery(criteriaLanguage).getSingleResult();
	}

	public List<Task> loadCreatedTasks(User owner) {
		return entityManager.createQuery("From Task WHERE owner = :user AND state.state != :deleted ORDER BY priority.priority ASC",Task.class).setParameter("user", owner).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}

	public List<Task> loadMyTasks(User solver) {
		return entityManager.createQuery("From Task WHERE solver = :solver AND state.state != :deleted ORDER BY priority.priority ASC",Task.class).setParameter("solver", solver).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}

	public List<Task> loadSectorsTasks(Sector sector) {
		return entityManager.createQuery("From Task WHERE sector = :sector AND state.state != :deleted ORDER BY priority.priority ASC",Task.class).setParameter("sector", sector).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
	}
	
	public List<Sector> loadSectors() {
		return entityManager.createQuery("From Sector",Sector.class).getResultList();
	}
	public Sector loadSector(Long sector) {
		return entityManager.createQuery("From Sector where sector = :sector",Sector.class).setParameter("sector", sector).getSingleResult();
	}
	public Profile loadProfile(Long profile) {
		return entityManager.createQuery("From Profile where profile = :profile",Profile.class).setParameter("profile", profile).getSingleResult();
	}

    public List<State> getStates() {
        return entityManager.createQuery("From State",State.class).getResultList();
    }

	public List<Task> loadSubtasks(Task task) {
		return entityManager.createQuery("From Task WHERE parentTask = :parent AND state.state != :deleted ORDER BY priority.priority ASC",Task.class).setParameter("parent", task).setParameter("deleted", States.CANCELADO.getValue()).getResultList();
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

    public List<Category> loadCategories() {
        return entityManager.createQuery("From Category",Category.class).getResultList();
    }

	public Collection<Priority> loadPriorities() {
		return entityManager.createQuery("From Priority",Priority.class).getResultList();
	}

    public void updateTask(Task task) {
		entityManager.merge(task);
    }
}