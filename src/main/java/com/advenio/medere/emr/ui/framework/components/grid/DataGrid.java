package com.advenio.medere.emr.ui.framework.components.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;

public class DataGrid<T> {

	private Grid<T> grid;
	private Class<T> domainType;
	private GridLoadListener<T> loadListener;
	private boolean enableHeaderFilter;
	private boolean enableControlFilter;

	private Label lblRecordCount;
	private HorizontalLayout hlControls;
	private HorizontalLayout hlAdditionalControls;
	private VerticalLayout vlContainer;
	
	
	public DataGrid(Class<T> domainType, boolean enableHeaderFilter, boolean enableControlFilter) {
		this.domainType = domainType;		
		this.enableControlFilter = enableControlFilter;
		this.enableHeaderFilter = enableHeaderFilter;
		
		grid = new Grid<T>(domainType);
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setMultiSort(true);
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addThemeVariants(GridVariant.LUMO_COMPACT);
		grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);		
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

		
		lblRecordCount = new Label(String.format(grid.getTranslation("GridRecordCount",null),0));
		lblRecordCount.getElement().getStyle().set("font-size","smaller");
		lblRecordCount.setWidthFull();
	}

	public void setRecordCount(int recordCount) {
		lblRecordCount.setText(String.format(grid.getTranslation("GridRecordCount",null),recordCount));
	}
	
	public void init() {
		
		hlControls = new HorizontalLayout();
		hlControls.setWidthFull();
		hlControls.setDefaultVerticalComponentAlignment(Alignment.START);
				
		hlAdditionalControls = new HorizontalLayout();
		hlAdditionalControls.setWidthFull();
		hlAdditionalControls.setJustifyContentMode(JustifyContentMode.END);
		hlAdditionalControls.setDefaultVerticalComponentAlignment(Alignment.START);
		hlAdditionalControls.getElement().getStyle().set("display","flex");
		hlAdditionalControls.getElement().getStyle().set("flex-wrap","wrap");
		
		
		hlControls.add(lblRecordCount);			
		
		hlControls.add(hlAdditionalControls);
		
		vlContainer = new VerticalLayout();
		vlContainer.setSizeFull();
		vlContainer.setMargin(false);
		vlContainer.add(hlControls);
		vlContainer.addAndExpand(grid);
	}
	
	public Grid<T> getGrid() {
		return grid;
	}

	public GridLoadListener<T> getLoadListener() {
		return loadListener;
	}

	public void setLoadListener(GridLoadListener<T> loadListener) {
		this.loadListener = loadListener;
	}

	public Component getComponent() {
		return vlContainer;
	}
	
	public void addControlToHeader(Component control,boolean addAsFirst) {
		if (addAsFirst) {
			hlAdditionalControls.addComponentAsFirst(control);	
		}else {
			hlAdditionalControls.add(control);
		}
		
	}
}
