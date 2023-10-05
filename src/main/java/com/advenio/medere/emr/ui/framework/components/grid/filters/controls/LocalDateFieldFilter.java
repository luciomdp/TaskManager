package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import java.time.LocalDate;

import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.dao.filters.FilteringInfo;
import com.advenio.medere.dao.filters.FilterList.AsociationType;
import com.advenio.medere.dao.filters.FilteringInfo.FilterType;
import com.advenio.medere.emr.ui.framework.components.grid.filters.config.LocalDateRangeFilterConfig;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class LocalDateFieldFilter extends BaseGridFilter<DatePicker, LocalDateRangeFilterConfig>{

	private DatePicker dpUpper;
	private LocalDate smallestValue;
	private LocalDate biggestValue;
	
	public LocalDateFieldFilter(LocalDateRangeFilterConfig filterConfig, HeaderCell headerCell) {
		super(filterConfig, headerCell);
	}

	@Override
	public void fillFilteringInfo(FilterList filterlist) {
		
		FilterList fl = filterlist;
		if (filterConfig.getFields().size() > 1) {
			fl = new FilterList();
			fl.setAsociationType(AsociationType.OR);
		}

		for (String fieldName : filterConfig.getFields()) {
			if ((smallestValue != null) && (biggestValue == null)) {
				FilteringInfo fi = new FilteringInfo();
				fi.setFieldname(fieldName);
				fi.addValue(smallestValue);
				fi.setFilterType(FilterType.GREATEREQUAL);
				fl.add(fi);
			} else {
				if ((smallestValue == null) && (biggestValue != null)) {
					FilteringInfo fi = new FilteringInfo();
					fi.setFieldname(fieldName);
					fi.addValue(biggestValue);
					fi.setFilterType(FilterType.LOWEREQUAL);
					fl.add(fi);
				} else {

					if ((smallestValue != null) && (biggestValue != null)) {
						FilterList filterlistValues = new FilterList();
						filterlistValues.setAsociationType(AsociationType.AND);

						FilteringInfo fi = new FilteringInfo();
						fi.setFieldname(fieldName);
						fi.addValue(smallestValue);
						fi.setFilterType(FilterType.GREATEREQUAL);
						filterlistValues.add(fi);

						fi = new FilteringInfo();
						fi.setFieldname(fieldName);
						fi.addValue(biggestValue);
						fi.setFilterType(FilterType.LOWEREQUAL);
						filterlistValues.add(fi);

						fl.getFilterLists().add(filterlistValues);
					}
				}
			}
		}

		if (filterConfig.getFields().size() > 1) {
			filterlist.getFilterLists().add(fl);
		}
		
	}

	@Override
	public void clearFilter() {
		
		filterControl.clear();
		if (dpUpper!=null) {
			dpUpper.clear();
		}		
	}

	@Override
	public void init() {
		filterControl = new DatePicker();
		filterControl.getElement().setAttribute("theme", TextFieldVariant.LUMO_SMALL.getVariantName());
		filterControl.setLocale(filterConfig.getLocale());
		filterControl.setWidth("110px");
		
		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getCaption())) {
			filterControl.setLabel(filterConfig.getCaption());
		}
		
		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getToolTip())) {
			filterControl.setHelperText(filterConfig.getToolTip());
		}
		
		if (filterConfig.getDateFrom()!=null) {
			filterControl.setValue(filterConfig.getDateFrom());
			smallestValue = filterConfig.getDateFrom();
		}
		
		filterControl.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<LocalDate>>() {

			@Override
			public void valueChanged(ValueChangeEvent<LocalDate> event) {
				smallestValue = event.getValue();
				if (filterChangeListener != null) {
					filterChangeListener.filterChanged();
				}
			}
		});
		
		if (filterConfig.isRangeFilter()) {
			
			dpUpper = new DatePicker();
			dpUpper.setLocale(filterConfig.getLocale());
			dpUpper.getElement().setAttribute("theme", TextFieldVariant.LUMO_SMALL.getVariantName());
			dpUpper.setWidth("110px");
			
			if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getCaptionTo())) {
				dpUpper.setLabel(filterConfig.getCaptionTo());
			}
			
			if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getToolTip())) {
				dpUpper.setHelperText(filterConfig.getToolTip());
			}
			
			if (filterConfig.getDateTo()!=null) {
				dpUpper.setValue(filterConfig.getDateTo());
				biggestValue =  filterConfig.getDateTo();
			}
			
			dpUpper.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<LocalDate>>() {

				@Override
				public void valueChanged(ValueChangeEvent<LocalDate> event) {
					biggestValue = event.getValue();
					if (filterChangeListener != null) {
						filterChangeListener.filterChanged();
					}
				}
			});
			
			if (headerCell!=null) {
				HorizontalLayout hlFilter = new HorizontalLayout();
				hlFilter.add(filterControl,dpUpper);
				headerCell.setComponent(hlFilter);					
			}
			
		}else {
			if (headerCell!=null) {
				headerCell.setComponent(filterControl);
			}
			
		}
		
		
	}

	public DatePicker getDpUpper() {
		return dpUpper;
	}
	
	

}
