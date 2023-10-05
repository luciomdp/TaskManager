package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateRangeFilterConfig extends BaseTextFilterConfig<LocalDateRangeFilterConfig,LocalDate>{

	private SimpleDateFormat dateFormat;
	private String captionTo;
	private LocalDate dateTo;
	private boolean rangeFilter;
	private Locale locale;
	
	public LocalDateRangeFilterConfig(String columnId,String caption,boolean rangeFilter,Locale locale) {
		super(columnId,caption);
		this.rangeFilter = rangeFilter;
		this.locale = locale;
	}

	public LocalDateRangeFilterConfig withFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}
	
	public LocalDateRangeFilterConfig withCaptionFromCaptionTo(String captionFrom,String captionTo) {
		this.caption = captionFrom;
		this.captionTo = captionTo;
		return this;
	}
	
	public LocalDateRangeFilterConfig withDateValues(LocalDate dateFrom,LocalDate dateTo) {
		this.initialValue = dateFrom;
		this.dateTo = dateTo;
		return this;
	}
	
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getCaptionTo() {
		return captionTo;
	}

	public void setCaptionTo(String captionTo) {
		this.captionTo = captionTo;
	}

	public LocalDate getDateFrom() {
		return initialValue;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.initialValue = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public boolean isRangeFilter() {
		return rangeFilter;
	}

	public Locale getLocale() {
		return locale;
	}
	
	
}
