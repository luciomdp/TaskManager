package com.advenio.medere.emr.ui.framework.components.grid.filters.controls;

import com.advenio.medere.emr.ui.framework.components.grid.filters.config.NumberFilterConfig;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class IntegerFieldFilter extends BaseNumericFieldFilter<IntegerField,Integer>{

	public IntegerFieldFilter(NumberFilterConfig filterConfig, HeaderCell headerCell) {
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

		filterControl = new IntegerField();
		filterControl.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		
		if (filterConfig.hasInitialValue()) {
			filterControl.setValue((Integer) filterConfig.getInitialValue());
		}
		
		if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getInputPrompt())) {
			filterControl.setPlaceholder(filterConfig.getInputPrompt());
			filterControl.setHasControls(true);
			filterControl.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<Integer>>() {

				@Override
				public void valueChanged(ValueChangeEvent<Integer> event) {
					smallestValue = event.getValue();
					if (filterChangeListener!=null) {
						filterChangeListener.filterChanged();
					}
				}
			});
		}
		
		if (filterConfig.isRangeFilter()) {
			nfUpper = new IntegerField();
			
			if (filterConfig.hasInitialValue()) {
				nfUpper.setValue((Integer) filterConfig.getInitialEndValue());				
			}
			
			if (!StringsUtils.isNullOrEmptyTrimmed(filterConfig.getBiggestInputPrompt())) {
				nfUpper.setPlaceholder(filterConfig.getBiggestInputPrompt());	
				nfUpper.setHasControls(true);
			}
			nfUpper.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<Integer>>() {

				@Override
				public void valueChanged(ValueChangeEvent<Integer> event) {
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
