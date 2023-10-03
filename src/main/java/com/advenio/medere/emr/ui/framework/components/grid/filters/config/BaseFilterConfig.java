package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

import java.util.ArrayList;
import java.util.List;

public class BaseFilterConfig<T,K> {

	protected String columnId;
	protected List<String> fields;
	protected String caption;
	
	protected K initialValue;
	
	public BaseFilterConfig(String columnId,String caption) {
		this.columnId = columnId;
		this.fields = new ArrayList<String>();
		this.caption = caption;
	}
	
	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public List<String> getFields() {
		return fields;
	}
	
	public String getCaption() {
		return caption;
	}

	public T addField(String field) {
		fields.add(field);
		return (T)this;
	}
	
	public T withInitialValue(K initialValue) {
		this.initialValue = initialValue;
		return (T)this;
	}
	
	public boolean hasInitialValue() {
		return initialValue != null;
	}
	
	public K getInitialValue() {
		return initialValue;
	}
	
}
