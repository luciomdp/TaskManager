package com.advenio.medere.emr.dao.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;


public class RegionalSettingsDTO implements Serializable{

	private static final long serialVersionUID = -8604417215812171689L;

	protected BigInteger regionalSettings;
	protected char decimalSeparator;
	protected char groupingSeparator;
	protected String currencySymbolString;
	protected String shortDateFormatString;
	protected String dateTimeFormatString;
	protected String timeFormatString;
	protected String monthNameAndYearFormatString;

	public BigInteger getRegionalSettings() {
		return regionalSettings;
	}
	public void setRegionalSettings(BigInteger regionalSettings) {
		this.regionalSettings = regionalSettings;
	}
	public char getDecimalSeparator() {
		return decimalSeparator;
	}
	public void setDecimalSeparator(char decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
	public char getGroupingSeparator() {
		return groupingSeparator;
	}
	public void setGroupingSeparator(char groupingSeparator) {
		this.groupingSeparator = groupingSeparator;
	}
	public String getCurrencySymbolString() {
		return currencySymbolString;
	}
	public void setCurrencySymbolString(String currencySymbol) {
		this.currencySymbolString = currencySymbol;
	}
	public String getShortDateFormatString() {
		return shortDateFormatString;
	}
	public void setShortDateFormatString(String shortDateFormat) {
		this.shortDateFormatString = shortDateFormat;
	}
	public String getDateTimeFormatString() {
		return dateTimeFormatString;
	}
	public void setDateTimeFormatString(String dateTimeFormat) {
		this.dateTimeFormatString = dateTimeFormat;
	}
	public String getTimeFormatString() {
		return timeFormatString;
	}
	public void setTimeFormatString(String timeFormat) {
		this.timeFormatString = timeFormat;
	}
	public String getMonthNameAndYearFormatString() {
		return monthNameAndYearFormatString;
	}
	public void setMonthNameAndYearFormatString(String monthNameAndYearFormatString) {
		this.monthNameAndYearFormatString = monthNameAndYearFormatString;
	}

	protected SimpleDateFormat timeFormat;
	protected SimpleDateFormat dateTimeFormat;
	protected SimpleDateFormat shortDateFormat;
	protected SimpleDateFormat monthNameAndYearFormat;
	protected Locale locale;
	protected DecimalFormat dfCurrency;
	protected DecimalFormatSymbols otherSymbols;
	protected DecimalFormat dfBigDecimalWithoutFixedDecimals;
	protected DateTimeFormatter timeFormatter;
	protected DateTimeFormatter shortDateFormatter;
	protected DateTimeFormatter dateTimeFormatter;
	protected DateTimeFormatter monthNameAndYearFormatter;
	protected DecimalFormat dfBigDecimalWithFixedDecimals;

	public RegionalSettingsDTO(){
	}


	public void setLocale(Locale l){
		locale = l;
	}


	public SimpleDateFormat getTimeFormat() {
		if (timeFormat == null)
			timeFormat = new SimpleDateFormat(timeFormatString);
		return timeFormat;
	}


	public SimpleDateFormat getDateTimeFormat() {
		if (dateTimeFormat == null)
			dateTimeFormat = new SimpleDateFormat(dateTimeFormatString);
		return dateTimeFormat;
	}


	public SimpleDateFormat getShortDateFormat() {
		if (shortDateFormat == null)
			shortDateFormat = new SimpleDateFormat(shortDateFormatString);
		return shortDateFormat;
	}


	protected DecimalFormatSymbols getDecimalFormatSymbols(){
		if (otherSymbols ==null){
			otherSymbols = new DecimalFormatSymbols(locale);
			otherSymbols.setDecimalSeparator(decimalSeparator);
			otherSymbols.setGroupingSeparator(groupingSeparator);
			otherSymbols.setCurrencySymbol(currencySymbolString);
		}
		return otherSymbols;
	}


	public String formatNumberAsCurrency(BigDecimal number){
		if (number == null)	{
			return "";
		}
		else {
			if (dfCurrency==null){
				dfCurrency = new DecimalFormat(currencySymbolString + " #,##0.00", getDecimalFormatSymbols());
			}
			return dfCurrency.format(number);
		}
	}


	public DecimalFormat getCurrencyFormat() {
		if (dfCurrency==null){
			dfCurrency = new DecimalFormat(currencySymbolString + " #,##0.00", getDecimalFormatSymbols());
		}
		return dfCurrency;
	}


	public DecimalFormat getDfBigDecimalWithoutFixedDecimals(){
		if (dfBigDecimalWithoutFixedDecimals==null){
			dfBigDecimalWithoutFixedDecimals = new DecimalFormat("#,###.####", getDecimalFormatSymbols());
			dfBigDecimalWithoutFixedDecimals.setParseBigDecimal(true);
		}
		return dfBigDecimalWithoutFixedDecimals;
	}


	public BigDecimal parseStringToBigDecimal(String text){
		try {
			return (BigDecimal) getDfBigDecimalWithoutFixedDecimals().parseObject(text);
		} catch (ParseException e) {
			return new BigDecimal(0);
		}
	}


	public String formatNumberAsFloatWithoutFixedDecimals(BigDecimal number){
		if (number == null) {
			return "";
		}
		else {
			return getDfBigDecimalWithoutFixedDecimals().format(number);
		}
	}


	public DateTimeFormatter getShortDateFormatter(){
		if (shortDateFormatter==null) {
			shortDateFormatter = DateTimeFormatter.ofPattern(shortDateFormatString);
		}
		return shortDateFormatter;
	}


	public DateTimeFormatter getDateTimeFormatter(){
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatString);
		}
		return dateTimeFormatter;
	}


	public DateTimeFormatter getTimeFormatter(){
		if (timeFormatter==null) {
			timeFormatter = DateTimeFormatter.ofPattern(timeFormatString);
		}
		return timeFormatter;
	}



	public DecimalFormat getDfBigDecimalWithFixedDecimals(int decimals){
		if (dfBigDecimalWithFixedDecimals==null){
			dfBigDecimalWithFixedDecimals = new DecimalFormat(String.format("%s%s","#,###.", StringUtils.repeat("0", 2)), getDecimalFormatSymbols());
			dfBigDecimalWithFixedDecimals.setParseBigDecimal(true);
		}
		return dfBigDecimalWithFixedDecimals;
	}


	public String formatNumberAsFloatWithFixedDecimals(BigDecimal number, int decimals){
		return getDfBigDecimalWithFixedDecimals(decimals).format(number);
	}


	public String formatBigDecimalWithLeadingZeros(BigDecimal number, int numbers){

		DecimalFormat dfBigDecimalWithLeadingZeros = new DecimalFormat(String.format("%s",StringUtils.repeat("0", numbers)), getDecimalFormatSymbols());
		dfBigDecimalWithLeadingZeros.setParseBigDecimal(true);
		return dfBigDecimalWithLeadingZeros.format(number);
	}


	public SimpleDateFormat getMonthNameAndYearFormat() {
		if (monthNameAndYearFormat == null)
			monthNameAndYearFormat = new SimpleDateFormat(monthNameAndYearFormatString);
		return monthNameAndYearFormat;
	}


	public DateTimeFormatter getMonthNameAndYearFormatter(){
		if (monthNameAndYearFormatter==null) {
			monthNameAndYearFormatter = DateTimeFormatter.ofPattern(monthNameAndYearFormatString);
		}
		return monthNameAndYearFormatter;
	}


	public Locale getLocale() {
		return locale;
	}
}
