package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.dao.filters.FilteringInfo;
import com.advenio.medere.dao.filters.FilterList.AsociationType;
import com.advenio.medere.dao.filters.FilteringInfo.FilterType;
import com.advenio.medere.emr.ui.framework.components.grid.filters.config.TextFilterConfig;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class TextFieldFilter extends BaseGridFilter<TextField, TextFilterConfig> {

	public TextFieldFilter(TextFilterConfig filterConfig, HeaderCell headerCell) {
		super(filterConfig, headerCell);
	}

	@Override
	public void fillFilteringInfo(FilterList filterlist) {
		if (!filterControl.getValue().trim().isEmpty()) {
			if (filterConfig.getFields().size() > 1) {
				FilterList fl = new FilterList();
				fl.setAsociationType(AsociationType.OR);
				for (String filterFieldName : filterConfig.getFields()) {
					FilteringInfo fi = new FilteringInfo();
					fi.setFieldname(filterFieldName);
					fi.addValue(filterControl.getValue().trim());
					fi.setFilterType(FilterType.ILIKE);
					fl.add(fi);
				}
				filterlist.getFilterLists().add(fl);
			} else {
				FilteringInfo fi = new FilteringInfo();
				fi.setFieldname(filterConfig.getFields().get(0));
				fi.addValue(filterControl.getValue().trim());
				fi.setFilterType(FilterType.ILIKE);
				filterlist.add(fi);
			}
		}
	}

	@Override
	public void clearFilter() {
		filterControl.clear();
	}

	@Override
	public void init() {
		filterControl = new TextField();
		filterControl.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		filterControl.setSizeFull();
		filterControl.setMaxLength(200);

		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getInputPrompt())) {
			filterControl.setPlaceholder(filterConfig.getInputPrompt());
		}
		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getCaption())) {
			filterControl.setLabel(filterConfig.getCaption());
		}
		filterControl.addThemeVariants(TextFieldVariant.LUMO_SMALL);

		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getToolTip())) {
			filterControl.setHelperText(filterConfig.getToolTip());
		}

		if (headerCell != null) {
			headerCell.setComponent(filterControl);
		}
		
		if (filterConfig.hasInitialValue()) {
			filterControl.setValue(filterConfig.getInitialValue());
		}
		
		if (filterConfig.isSearchByChange()) {
			filterControl.setValueChangeMode(ValueChangeMode.TIMEOUT);
			filterControl.setValueChangeTimeout(1500);
			
			filterControl.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<String>>() {

				@Override
				public void valueChanged(ValueChangeEvent<String> event) {
					if (filterChangeListener != null) {
						filterChangeListener.filterChanged();
					}
				}
			});
		} else {
			if (filterConfig.isSearchByEnter()) {
				filterControl.addKeyDownListener(Key.ENTER, new ComponentEventListener<KeyDownEvent>() {

					@Override
					public void onComponentEvent(KeyDownEvent event) {
						if (event.getSource().equals(filterControl)) {
							if (filterChangeListener != null) {
								filterChangeListener.filterChanged();
							}
						}
					}
				}, null, null);
			}
		}

	}

}
