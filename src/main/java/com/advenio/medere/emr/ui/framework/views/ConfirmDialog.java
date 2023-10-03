package com.advenio.medere.emr.ui.framework.views;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;


@SpringComponent()
@Scope("prototype")
public class ConfirmDialog extends Dialog{
	
	private Label sureToExit;
	private String sureToExitMessage;
	private IOnNotificationListener acceptListener;
	private IOnNotificationListener cancelListener;
	protected HorizontalLayout footerLayout;
	protected HorizontalLayout headerLayout;
	protected VerticalLayout mainLayout;
	protected Button btnCancel;
	protected Button btnAccept;

	public ConfirmDialog(String sureToExitMessage) {
		super();
		this.sureToExitMessage = sureToExitMessage;
	}
	
	
	@PostConstruct
	public void init() {
		
		setCloseOnOutsideClick(false);
		setDraggable(false);
		setModal(true);
		setResizable(false);
		
        btnCancel = new Button(VaadinIcon.CLOSE.create());
        btnCancel.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = -6034396587337815015L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				if(cancelListener != null) 
					cancelListener.onNotification();
				close();				
			}
		});
        
        btnAccept = new Button(VaadinIcon.CHECK.create());
        btnAccept.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = 910212650806027099L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				if(acceptListener != null) 
					acceptListener.onNotification();
				close();				
			}
		});
        
        sureToExit = new Label(sureToExitMessage);
		sureToExit.getElement().getStyle().set("text-align", "center");
		sureToExit.getElement().getStyle().set("font-size", "larger");
        
        headerLayout= new HorizontalLayout(sureToExit);
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        headerLayout.setSizeFull();
        headerLayout.setPadding(false);
        
        footerLayout = new HorizontalLayout(btnCancel,btnAccept);
        footerLayout.setSizeUndefined();
        footerLayout.setSpacing(true);
        footerLayout.setPadding(false);
        
        mainLayout = new VerticalLayout(headerLayout, footerLayout);
        mainLayout.setHorizontalComponentAlignment(Alignment.END, footerLayout);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(true);	
        
        add(mainLayout);

		open();
	}

	public void addAcceptListener(IOnNotificationListener ioNl) {
		acceptListener = ioNl;
	}
	public void addCancelListener(IOnNotificationListener ioNl) {
		cancelListener = ioNl;
	}


}
