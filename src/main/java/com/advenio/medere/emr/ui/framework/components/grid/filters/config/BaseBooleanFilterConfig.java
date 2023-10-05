package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

public class BaseBooleanFilterConfig<T> extends BaseFilterConfig<T,Boolean>{

	public BaseBooleanFilterConfig(String columnId,String caption) {
		super(columnId,caption);
	}

	private String yesCaption;
	private String noCaption;
	private String toolTip;
	
	public String getYesCaption() {
		return yesCaption;
	}

	public String getNoCaption() {
		return noCaption;
	}

	public String getToolTip() {
		return toolTip;
	}
	
	public T withYesNoCaptionConfig(String yesCaption,String noCaption) {
		this.yesCaption = yesCaption;
		this.noCaption = noCaption;
		return (T) this;
	}

	public T withToolTipConfig(String toolTip) {
		this.toolTip = toolTip;
		return (T) this;
	}

}
