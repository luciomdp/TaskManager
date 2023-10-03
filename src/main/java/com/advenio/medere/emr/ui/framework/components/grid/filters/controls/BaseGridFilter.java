package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.emr.ui.framework.components.grid.filters.FilterChangeEventListener;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;

public abstract class BaseGridFilter<C extends Component,T>  {

	protected C filterControl;
	protected T filterConfig;
	protected HeaderCell headerCell;
	protected FilterChangeEventListener filterChangeListener;
	
	protected BaseGridFilter(T filterConfig,HeaderCell headerCell) {
		this.filterConfig = filterConfig;
		this.headerCell = headerCell;
	}
		
	public abstract void fillFilteringInfo(FilterList filterlist);
	public abstract void clearFilter();
	public abstract void init();

	public FilterChangeEventListener getFilterChangeListener() {
		return filterChangeListener;
	}

	public void setFilterChangeListener(FilterChangeEventListener filterChangeListener) {
		this.filterChangeListener = filterChangeListener;
	}

	public C getFilterControl() {
		return filterControl;
	}
		
}
