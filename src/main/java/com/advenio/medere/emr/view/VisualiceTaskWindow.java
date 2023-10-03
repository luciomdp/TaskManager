package com.advenio.medere.emr.view;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.advenio.medere.server.session.ISessionManager;
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
public class VisualiceTaskWindow extends Dialog implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";

	@Autowired protected EntityDAO entityDAO;
	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	
	protected static final Logger logger = LoggerFactory.getLogger(VisualiceTaskWindow.class);

	private VerticalLayout mainLayout;
   	private HorizontalLayout footerLayout;
	private VerticalLayout vlMain;
   	private HorizontalLayout headerLayout;
	
	private TextField txtTitle;
	private Label lblOwner;
	private Label lblSector;
	private Button btnCancelTask;
	private TextArea txtDescription;
	private ComboBox<State> cboState;

	private Button addSubtask;
	private DataGrid<Task> gridSubtasks;

	private Task task;
	private boolean showSubtasks;
	private String caption;
	
	public VisualiceTaskWindow(String caption, Task task, boolean showSubtasks) {
		this.caption = caption;
		this.task = task;
		this.showSubtasks = showSubtasks;
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
		lblSector = new Label();

		btnCancelTask = new Button();

		cboState = new ComboBox<State>("Estado");
		cboState.setSizeFull();
		cboState.setItems(entityDAO.getStates());
		cboState.setItemLabelGenerator(e -> e.getDescription());

		txtDescription = new TextArea("Descripción");
		txtDescription.setSizeFull();
		
		createGrid();	

		vlMain = new VerticalLayout();
		vlMain.setSizeFull();
		headerLayout = new HorizontalLayout(new Component[]{txtTitle,btnCancelTask});
		headerLayout.setSizeUndefined();
		headerLayout.setSpacing(true);
		headerLayout.setPadding(false);
		footerLayout = new HorizontalLayout(new Component[]{lblOwner});
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
		txtTitle.setValue(task.getTitle()!=null?task.getTitle():txtTitle.getValue());
		lblOwner.setText(task.getOwner()!=null?task.getOwner().getName():lblOwner.getText());
		lblSector.setText(task.getSector()!=null?task.getSector().getName():lblSector.getText());
		txtDescription.setValue(task.getDescription()!=null?task.getDescription():txtDescription.getValue());
		cboState.setValue(task.getState());
	}

	private void createGrid() {
		gridSubtasks = new DataGrid<Task>(Task.class, true, false);

		gridSubtasks.getGrid().setItems(entityDAO.loadSubtasks(task));

		gridSubtasks.getGrid().removeAllColumns();

		gridSubtasks.getGrid().addColumn(e -> e.getPriority()!=null?e.getPriority().getDescription():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		gridSubtasks.getGrid().addColumn(e -> e.getTitle()!=null?e.getTitle():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		gridSubtasks.getGrid().addColumn(e -> e.getOwner()!=null?e.getOwner().getName():"").setHeader("Creador").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		gridSubtasks.getGrid().addColumn(e -> e.getDatelimit()!=null?e.getDatelimit():"").setHeader("Fecha limite").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		gridSubtasks.getGrid().addColumn(e ->e.getSolver()!=null? e.getSolver().getName():"").setHeader("Resolutor").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		gridSubtasks.getGrid().addItemClickListener(e -> {
			VisualiceTaskWindow w = context.getBean(VisualiceTaskWindow.class, "Editar subtarea", e.getItem(),false);// sessionManager.getI18nMessage("EditMMSI"));
		});

		gridSubtasks.init();

		addSubtask = new Button("Agregar subtarea");
		addSubtask.addClickListener(e -> {

		});

		gridSubtasks.addControlToHeader(addSubtask, true);
		
	}
	
}
