package com.advenio.medere.emr.view;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent()
@Scope("prototype")
public class CreateTaskWindow extends Dialog implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";

	@Autowired protected EntityDAO entityDAO;
	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	
	protected static final Logger logger = LoggerFactory.getLogger(CreateTaskWindow.class);

	private VerticalLayout mainLayout;
   	private HorizontalLayout footerLayout;
	private VerticalLayout vlMain;
   	private HorizontalLayout headerLayout;
	
	private TextField txtTitle;
	private Label lblOwner;
	private ComboBox<Sector> cboSector;
	private Button btnCancelTask;
	private TextArea txtDescription;
	private ComboBox<State> cboState;

	private User owner;
	private String caption;
	
	public CreateTaskWindow(String caption, User owner) {
		this.caption = caption;
		this.owner = owner;
	}

	@PostConstruct
	public void init() {
		setCloseOnOutsideClick(false);
		setDraggable(false);
		setModal(true);
		setResizable(false);
		txtTitle = new TextField("Titulo");
		txtTitle.setSizeFull();	

		lblOwner = new Label(this.caption + ": ");

		cboSector = new ComboBox<Sector>("Estado");
		cboSector.setSizeFull();
		cboSector.setItems(entityDAO.loadSectors());
		cboSector.setItemLabelGenerator(e -> e.getDescription());

		btnCancelTask = new Button();

		cboState = new ComboBox<State>("Estado");
		cboState.setSizeFull();
		cboState.setItems(entityDAO.getStates());
		cboState.setItemLabelGenerator(e -> e.getDescription());

		txtDescription = new TextArea("Descripción");
		txtDescription.setSizeFull();	

		vlMain = new VerticalLayout();
		vlMain.setSizeFull();
		headerLayout = new HorizontalLayout(new Component[]{txtTitle,lblOwner,btnCancelTask});
		headerLayout.setSizeUndefined();
		headerLayout.setSpacing(true);
		headerLayout.setPadding(false);
		footerLayout = new HorizontalLayout(new Component[]{});
		footerLayout.setSizeUndefined();
		footerLayout.setSpacing(true);
		footerLayout.setPadding(false);
		mainLayout = new VerticalLayout(new Component[]{this.headerLayout, this.vlMain, this.footerLayout});
		mainLayout.setFlexGrow(1.0, new HasElement[]{this.vlMain});
		mainLayout.setHorizontalComponentAlignment(Alignment.END, new Component[]{this.footerLayout});
		mainLayout.setMargin(false);
		mainLayout.setPadding(false);
		mainLayout.setSpacing(true);
		open();
		inputData();
	}

	@Override
	public String getPageTitle() {
		return caption;
	}

	private void inputData() {
		lblOwner.setText(owner.getName());
	}
	
}
