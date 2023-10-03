package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.dao.filters.FilteringInfo;
import com.advenio.medere.dao.filters.FilterList.AsociationType;
import com.advenio.medere.dao.filters.FilteringInfo.FilterType;
import com.advenio.medere.dao.pagination.IDataDTO;
import com.advenio.medere.emr.ui.framework.components.grid.filters.config.ComboBoxFilterConfig;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class ComboBoxFieldFilter extends BaseGridFilter<ComboBox<IDataDTO>, ComboBoxFilterConfig> {

	public ComboBoxFieldFilter(ComboBoxFilterConfig filterConfig, HeaderCell headerCell) {
		super(filterConfig, headerCell);
	}

	@Override
	public void fillFilteringInfo(FilterList filterlist) {
		if (filterControl.getValue() != null) {
			FilterList fl = filterlist;
			if (filterConfig.getFields().size() > 1) {
				fl = new FilterList();
				fl.setAsociationType(AsociationType.OR);
			}

			for (String filterFieldName : filterConfig.getFields()) {
				FilteringInfo fi = new FilteringInfo();
				fi.setFieldname(filterFieldName);
				fi.addValue(filterControl.getValue().getId());
				fi.setFilterType(FilterType.EQUAL);
				fl.add(fi);
			}
			
			if (filterConfig.getFields().size() > 1) {
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
		filterControl = new ComboBox<IDataDTO>();
		filterControl.getElement().setAttribute("theme", TextFieldVariant.LUMO_SMALL.getVariantName());

		filterControl.setItems(filterConfig.getItems());
		filterControl.setItemLabelGenerator(e-> e.getDescription());
		filterControl.setClearButtonVisible(true);
		
		if (filterConfig.hasInitialValue()) {
			filterControl.setValue(filterConfig.getItems().get(filterConfig.getInitialValue().intValue()));
		}
		
		filterControl.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<IDataDTO>>() {

			@Override
			public void valueChanged(ValueChangeEvent<IDataDTO> event) {
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
