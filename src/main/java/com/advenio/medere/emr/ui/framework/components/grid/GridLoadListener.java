package com.advenio.medere.emr.ui.framework.components.grid;

import java.io.Serializable;

import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;

public interface GridLoadListener<T> extends Serializable{

	public Page<T> load(PageLoadConfig<T> loadconfig);
	public default Integer count(PageLoadConfig<T> loadconfig) {return 0;};
}