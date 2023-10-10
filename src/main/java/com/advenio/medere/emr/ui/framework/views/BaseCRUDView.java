package com.advenio.medere.emr.ui.framework.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


public abstract class BaseCRUDView<T> extends BaseView {

	private static final long serialVersionUID = 6181657629179191274L;
	@Autowired ApplicationContext context;
	protected Button btnEdit;
	protected Button btnDelete;
	protected Button btnNew;
	protected DataGrid<T> grid;
	
	protected String titleDelete = "";
	protected String titleDeleteItemText = "";
	
	protected boolean windowOpen = false;
	protected boolean pendingRefresh = false;
	
	public void init() {
		createGrid();

		btnNew = new Button(VaadinIcon.PLUS.create());
		btnNew.addThemeVariants(ButtonVariant.LUMO_SMALL);
		btnNew.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = -4512181173967300148L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				newItem();
			}
		});

		btnEdit = new Button(VaadinIcon.PENCIL.create());
		btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
		btnEdit.setVisible(false);
		btnEdit.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			private static final long serialVersionUID = 6829749887572947797L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				editItem(grid.getGrid().asSingleSelect().getValue());
			}
		});

		btnDelete = new Button(VaadinIcon.TRASH.create());
		btnDelete.setVisible(false);
		btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL);

		btnDelete.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			public void onComponentEvent(ClickEvent<Button> event) {
				ConfirmDialog deleteDialog = context.getBean(ConfirmDialog.class,titleDeleteItemText);
				deleteDialog.addAcceptListener(new IOnNotificationListener() {
					public void onNotification() {	
						deleteItem(grid.getGrid().asSingleSelect().getValue());
					}	
				});
			}
		});
	
		grid.addControlToHeader(btnNew, false);
		grid.addControlToHeader(btnEdit, false);
		grid.addControlToHeader(btnDelete, false);
		setViewContent(grid.getComponent());
	}
	
	
	
	private Component booleanRender(boolean value) {
		Icon icon = null;
		if (value) {
			icon = VaadinIcon.CHECK.create();
		}else{
			icon = VaadinIcon.CLOSE.create(); 
		}
		icon.setSize("1.25em");
		return icon;
	}

	protected abstract void newItem();
	protected abstract void editItem(T item);
	protected abstract void deleteItem(T item);
	protected abstract void createGrid();
}
