package com.advenio.medere.emr.objects.audit;

public class AuditStore {

	protected Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void clear() {
		userId = null;
	}
}
