package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

public abstract class BaseTextFilterConfig<T,K> extends BaseFilterConfig<T,K>{

	protected String toolTip;
	protected String inputPrompt;
	
	public BaseTextFilterConfig(String columnId,String caption) {
		super(columnId,caption);		
	}

	public T withInputPrompt(String inputPrompt){
		this.inputPrompt = inputPrompt;
		return (T)this;
	}
	
	public T withCaptionAndToolTip(String toolTip){
		this.toolTip = toolTip;
		return (T)this;
	}

	public String getToolTip() {
		return toolTip;
	}

	public String getInputPrompt() {
		return inputPrompt;
	}
	
	
}
