package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import java.math.BigDecimal;

import com.advenio.medere.emr.ui.framework.components.grid.filters.config.NumberFilterConfig;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class BigDecimalFieldFilter extends BaseNumericFieldFilter<BigDecimalField,BigDecimal> {

	public BigDecimalFieldFilter(NumberFilterConfig filterConfig, HeaderCell headerCell) {
		super(filterConfig, headerCell);	
	}

	@Override
	public void clearFilter() {
		filterControl.clear();
		if (nfUpper != null) {
			nfUpper.clear();
		}	
	}

	@Override
	public void init() {
		filterControl = new BigDecimalField();
		
		if (filterConfig.hasInitialValue()) {
			filterControl.setValue((BigDecimal) filterConfig.getInitialValue());
		}
		
		filterControl.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getInputPrompt())) {
			filterControl.setPlaceholder(filterConfig.getInputPrompt());
			filterControl.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<BigDecimal>>() {

				@Override
				public void valueChanged(ValueChangeEvent<BigDecimal> event) {
					smallestValue = event.getValue();
					if (filterChangeListener!=null) {
						filterChangeListener.filterChanged();
					}
				}
			});
		}
		
		if (filterConfig.isRangeFilter()) {
			nfUpper = new BigDecimalField();
			if (filterConfig.hasInitialValue()) {
				nfUpper.setValue((BigDecimal) filterConfig.getInitialEndValue());
			}
			
			if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getBiggestInputPrompt())) {
				nfUpper.setPlaceholder(filterConfig.getBiggestInputPrompt());	
			}
			nfUpper.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<BigDecimal>>() {

				@Override
				public void valueChanged(ValueChangeEvent<BigDecimal> event) {
					biggestValue = event.getValue();	
					if (filterChangeListener!=null) {
						filterChangeListener.filterChanged();
					}
				}
			});
			
			if (headerCell!=null) {
				HorizontalLayout hlFilter = new HorizontalLayout();
				hlFilter.add(filterControl,nfUpper);
				headerCell.setComponent(hlFilter);	
			}			
		}else {
			if (headerCell!=null) {
				headerCell.setComponent(filterControl);
			}
		}
		
	}

}
