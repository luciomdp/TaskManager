package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

import java.math.BigInteger;
import java.util.List;

import com.advenio.medere.dao.pagination.IDataDTO;

public class ComboBoxFilterConfig extends BaseFilterConfig<ComboBoxFilterConfig,BigInteger>{

	private List<IDataDTO> items;
		
	public ComboBoxFilterConfig(String columnId, String caption, List<String> fields,List<IDataDTO> items) {
		super(columnId,caption);		
		this.items = items;
	}

	public List<IDataDTO> getItems() {
		return items;
	}
	
	
}
