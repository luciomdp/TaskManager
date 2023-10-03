package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import java.util.Arrays;

import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.dao.filters.FilteringInfo;
import com.advenio.medere.dao.filters.FilterList.AsociationType;
import com.advenio.medere.dao.filters.FilteringInfo.FilterType;
import com.advenio.medere.emr.ui.framework.components.grid.filters.config.BooleanFilterConfig;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class BooleanFieldFilter extends BaseGridFilter<ComboBox<Boolean>, BooleanFilterConfig>{

	public BooleanFieldFilter(BooleanFilterConfig filterConfig, HeaderCell headerCell) {
		super(filterConfig, headerCell);		
	}

	@Override
	public void fillFilteringInfo(FilterList filterlist) {
		if (!filterControl.isEmpty()) {
			
			FilterList fl = filterlist;
			if (filterConfig.getFields().size()>1) {
				fl = new FilterList();
				fl.setAsociationType(AsociationType.OR);
			}
			
			for(String fieldName:filterConfig.getFields()) {
				FilteringInfo fi = new FilteringInfo();
				fi.setFieldname(fieldName);
				fi.addValue(filterControl.getValue());
				fi.setFilterType(FilterType.EQUAL);
				fl.add(fi);	
			}
			
			if (filterConfig.getFields().size()>1) {
				filterlist.getFilterLists().add(fl);
			}			
		}
				
	}

	@Override
	public void clearFilter() {
		filterControl.clear();		
	}

	@Override
	public void init() {
		filterControl = new ComboBox<Boolean>();
		filterControl.getElement().setAttribute("theme", TextFieldVariant.LUMO_SMALL.getVariantName());

		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getCaption())) {
			filterControl.setLabel(filterConfig.getCaption());
		}
		
		filterControl.setItems(Arrays.asList(Boolean.TRUE, Boolean.FALSE));
		filterControl.setItemLabelGenerator(e-> e.booleanValue() ? filterConfig.getYesCaption() : filterConfig.getNoCaption());
		filterControl.setClearButtonVisible(true);
		
		if (filterConfig.hasInitialValue()) {
			filterControl.setValue(filterConfig.getInitialValue());			
		}
		
		filterControl.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<Boolean>>() {

			@Override
			public void valueChanged(ValueChangeEvent<Boolean> event) {
				if (filterChangeListener != null) {
					filterChangeListener.filterChanged();
				}				
			}
		});
		
		if (headerCell!=null) {
			headerCell.setComponent(filterControl);
		}
		
	}

}
