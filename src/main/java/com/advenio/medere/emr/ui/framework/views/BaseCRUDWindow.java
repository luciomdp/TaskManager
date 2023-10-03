package com.advenio.medere.emr.ui.framework.views;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class BaseCRUDWindow<T> extends Dialog{

	private static final long serialVersionUID = 5893946646450621089L;
	
	protected FormLayout formLayout;
	protected VerticalLayout mainLayout;
	protected HorizontalLayout footerLayout;
	protected HorizontalLayout headerLayout;
	
	protected Button btnCancel;
	protected Button btnAccept;
	
	protected String windowTitle;
	protected Label lblWindowTitle;

	protected boolean newItem = true;
	
	public BaseCRUDWindow(String windowTitle) {
		super();
		this.windowTitle = windowTitle;
	}
	
	@PostConstruct
	public void init() {
		
		setCloseOnOutsideClick(false);
		setDraggable(false);
		setModal(true);
		setResizable(false);
				
		formLayout = new FormLayout();
        formLayout.setSizeFull();
		
        btnCancel = new Button(VaadinIcon.CLOSE.create());
        btnCancel.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = -6034396587337815015L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				cancel();				
			}
		});
        
        btnAccept = new Button(VaadinIcon.CHECK.create());
        btnAccept.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = 910212650806027099L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				accept();				
			}
		});
        
        lblWindowTitle = new Label(windowTitle);
        
        headerLayout= new HorizontalLayout(lblWindowTitle);
        headerLayout.setSizeUndefined();
        headerLayout.setSpacing(true);
        headerLayout.setPadding(false);
        
        footerLayout = new HorizontalLayout(btnCancel,btnAccept);
        footerLayout.setSizeUndefined();
        footerLayout.setSpacing(true);
        footerLayout.setPadding(false);
        
        mainLayout = new VerticalLayout(headerLayout,formLayout, footerLayout);
        mainLayout.setFlexGrow(1, formLayout);
        mainLayout.setHorizontalComponentAlignment(Alignment.END, footerLayout);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(true);	
        
        add(mainLayout);
        
        createControls();
        
		open();
	}
	
	protected abstract void createControls();
	
	protected void cancel() {
		close();
	}
	
	protected abstract void accept();	
	public abstract void editItem(T item);
	
}
