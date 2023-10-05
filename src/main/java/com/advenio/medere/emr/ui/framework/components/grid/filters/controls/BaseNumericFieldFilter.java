package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import com.advenio.medere.dao.filters.FilterList;
import com.advenio.medere.dao.filters.FilteringInfo;
import com.advenio.medere.dao.filters.FilterList.AsociationType;
import com.advenio.medere.dao.filters.FilteringInfo.FilterType;
import com.advenio.medere.emr.ui.framework.components.grid.filters.config.NumberFilterConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;

public abstract class BaseNumericFieldFilter<C extends Component, T> extends BaseGridFilter<C, NumberFilterConfig> {

	private C nfUpper;
	private T smallestValue;
	private T biggestValue;

	private BaseNumericFieldFilter(NumberFilterConfig filterConfig, HeaderCell headerCell) {
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

	public C getNfUpper() {
		return nfUpper;
	}

	
	
}
