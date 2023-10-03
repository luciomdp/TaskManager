package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

public class NumberFilterConfig extends BaseTextFilterConfig<NumberFilterConfig,Number>{

	protected String biggestInputPrompt;
	protected String wrongValueMessage;
	protected Class propertyType;
	protected boolean rangeFilter;
	protected Number initialEndValue;
	
	public NumberFilterConfig(String columnId,String caption,Class propertyType,boolean rangeFilter) {
		super(columnId,caption);
		this.propertyType = propertyType;
		this.rangeFilter = rangeFilter;
	}

	public NumberFilterConfig withPropertyType(Class propertyType) {
		this.propertyType = propertyType;
		return this;
	}
	
	public NumberFilterConfig withWrongValueMessage(String wrongValueMessage) {
		this.wrongValueMessage = wrongValueMessage;
		return this;
	}
	
	public NumberFilterConfig withSmallestAndBiggestInputPrompt(String smallestInputPrompt,String biggestInputPrompt){
		this.inputPrompt = smallestInputPrompt;
		this.biggestInputPrompt = biggestInputPrompt;
		return this;
	}

	public String getWrongValueMessage() {
		return wrongValueMessage;
	}

	public String getBiggestInputPrompt() {
		return biggestInputPrompt;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public boolean isRangeFilter() {
		return rangeFilter;
	}
	
	public NumberFilterConfig withInitialRangeValue(Number initialStartValue,Number initialEndValue) {
		this.initialValue = initialStartValue;
		this.initialEndValue = initialEndValue;		
		return this;
	}
	
	public Number getInitialEndValue() {
		return initialEndValue;
	}
}
