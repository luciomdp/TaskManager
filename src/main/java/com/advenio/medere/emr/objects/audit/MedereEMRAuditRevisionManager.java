package com.advenio.medere.emr.objects.audit;

import java.io.Serializable;

import org.hibernate.envers.RevisionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.objects.audit.AuditEvent;
import com.advenio.medere.objects.audit.IAuditRevisionManager;

@Component
public class MedereEMRAuditRevisionManager implements IAuditRevisionManager{

	@Autowired
	protected ApplicationContext applicationContext;
	
	@Autowired
	protected AuditStore auditStore;
	
	@Autowired
	protected UserDAO userDAO;
	
	@Override
	public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
			Object revisionEntity) {
		
		final AuditEvent revision = (AuditEvent) revisionEntity;
		
		if (entityId instanceof Long) {
			revision.setEntityID((Long)entityId);
		}
		
		if ((revision.get_user() == null) && (auditStore.getUserId() != null)) {			
			revision.set_user(userDAO.findUserById(auditStore.getUserId()));
		}
	}

}
