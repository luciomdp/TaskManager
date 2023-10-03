package com.advenio.medere.emr.ui.framework.components.grid.filters.config;

public class TextFilterConfig extends BaseTextFilterConfig<TextFilterConfig,String>{

	protected boolean searchByEnter;
	protected boolean searchByChange;
	
	public TextFilterConfig(String columnId,String caption) {
		super(columnId,caption);		
	}

	public TextFilterConfig withSearchOptions(boolean searchByEnter,boolean searchByChange) {
		this.searchByChange = searchByChange;
		this.searchByEnter = searchByEnter;
		return this;
	}

	public boolean isSearchByEnter() {
		return searchByEnter;
	}

	public boolean isSearchByChange() {
		return searchByChange;
	}
	
}
