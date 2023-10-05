package com.advenio.medere.emr.view;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Category;
import com.advenio.medere.emr.objects.Priority;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.advenio.medere.emr.ui.framework.views.IOnNotificationListener;
import com.advenio.medere.emr.objects.Priority.Priorities;
import com.advenio.medere.emr.objects.State.States;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
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

	@Autowired private ISessionManager sessionManager;
	@Autowired private EntityDAO entityDAO;
	@Autowired private UserDAO userDAO;
	@Autowired private ApplicationContext context;
	
	private static final Logger logger = LoggerFactory.getLogger(CreateTaskWindow.class);

	private VerticalLayout mainLayout;
   	private HorizontalLayout footerLayout;
	private VerticalLayout vlMain;
   	private HorizontalLayout headerLayout;
	private Button btnCancel;
   	private Button btnAccept;
	
	private TextField txtTitle;
	private ComboBox<State> cboState;
	private ComboBox<Category> cboCategory;
	private ComboBox<Sector> cboSector;
	private ComboBox<Priority> cboPriority;
	private DatePicker dateLimit;
	
	private TextArea txtDescription;

	private Button btnEdit;
	private Button btnDelete;
	private Button btnNew;
	private DataGrid<Task> gridSubtasks;

	private Task task;
	private Boolean showSubtasks;
	private String caption;
	
	public VisualiceTaskWindow(String caption, Task task, boolean showSubtasks) {
		this.add(caption);
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

		cboState = new ComboBox<State>("Estado");
		cboState.setSizeFull();
		cboState.setItems(entityDAO.getStates());
		cboState.setValue(entityDAO.getStateById(States.SIN_ASIGNAR.getValue()));
		cboState.setEnabled(false);
		cboState.setItemLabelGenerator(e -> e.getDescription());

		cboCategory = new ComboBox<Category>("Categoria");
		cboCategory.setSizeFull();
		cboCategory.setItems(entityDAO.loadCategories());
		cboCategory.setItemLabelGenerator(e -> e.getDescription());

		cboPriority = new ComboBox<Priority>("Prioridad");
		cboPriority.setSizeFull();
		cboPriority.setItems(entityDAO.loadPriorities());
		cboPriority.setItemLabelGenerator(e -> e.getDescription());

		cboSector = new ComboBox<Sector>("Sector");
		cboSector.setSizeFull();
		cboSector.setItems(entityDAO.loadSectors());
		cboSector.setItemLabelGenerator(e -> e.getDescription());

		dateLimit = new DatePicker();
		dateLimit.setLabel("Fecha limite");

		this.btnCancel = new Button(VaadinIcon.CLOSE.create());
		this.btnCancel.addClickListener(e -> cancel());
		this.btnAccept = new Button(VaadinIcon.CHECK.create());
		this.btnAccept.addClickListener(e -> accept());

		txtDescription = new TextArea("Descripción");
		txtDescription.setPlaceholder("Descripcion de la tarea");
		txtDescription.setHeight(300, Unit.PIXELS);
		txtDescription.setWidth(txtTitle.getWidth());

		//TODO arreglar layouts Visualice es igual
		HorizontalLayout hlCbos = new HorizontalLayout(cboState,cboCategory,cboPriority,cboSector);

		vlMain = new VerticalLayout(hlCbos,dateLimit,txtDescription);
		vlMain.setSizeFull();
		if(showSubtasks)
			createGrid(vlMain);

		headerLayout = new HorizontalLayout(txtTitle);
		headerLayout.setSizeUndefined();
		headerLayout.setWidthFull();
		headerLayout.setSpacing(true);
		headerLayout.setPadding(false);
		footerLayout = new HorizontalLayout(new Component[]{this.btnCancel, this.btnAccept});
		footerLayout.setSizeUndefined();
		footerLayout.setSpacing(true);
		footerLayout.setPadding(false);
		mainLayout = new VerticalLayout(new Component[]{this.headerLayout, this.vlMain, this.footerLayout});
		mainLayout.setFlexGrow(1.0, new HasElement[]{this.vlMain});
		mainLayout.setHorizontalComponentAlignment(Alignment.END, new Component[]{this.footerLayout});
		mainLayout.setMargin(false);
		mainLayout.setPadding(false);
		mainLayout.setSpacing(true);
		setWidth("70%");
		add(mainLayout);
		open();
	}

	@Override
	public String getPageTitle() {
		return caption;
	}

	private void cancel() {
		this.close();
	}
  
	private void accept() {
		//TODO agregar campos y ponerlos en la task cuando se crea Visualice es igual
		task.setTitle(txtTitle.getValue());
		task.setDescription(txtDescription.getValue());
		task.setParentTask(null);
		task.setState(cboState.getValue());
		task.setCategory(cboCategory.getValue());
		task.setPriority(cboPriority.getValue());
		task.setSector(cboSector.getValue());
		task.setDatelimit(dateLimit.getValue());
		
		entityDAO.updateTask(task);
		this.close();
	};

	private void createGrid(VerticalLayout layout) {

		gridSubtasks = new DataGrid<Task>(Task.class, false, false);																						
		gridSubtasks.getGrid().setItems(entityDAO.loadCreatedTasks(userDAO.findUserFull(sessionManager.getUser().getUsername())));

		gridSubtasks.getGrid().removeAllColumns();

		gridSubtasks.getGrid().addColumn(e -> e.getPriority()!=null?e.getPriority().getDescription():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		gridSubtasks.getGrid().addColumn(e -> e.getTitle()!=null?e.getTitle():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		gridSubtasks.getGrid().addColumn(e -> e.getOwner()!=null?e.getOwner().getName():"").setHeader("Creador").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		gridSubtasks.getGrid().addColumn(e -> e.getDatelimit()!=null?e.getDatelimit():"").setHeader("Fecha limite").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		gridSubtasks.getGrid().addColumn(e ->e.getSolver()!=null? e.getSolver().getName():"").setHeader("Resolutor").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		gridSubtasks.init();

		btnNew = new Button(VaadinIcon.PLUS.create());
		btnNew.addThemeVariants(ButtonVariant.LUMO_SMALL);
		btnNew.addClickListener(e -> newSubtask());

		btnEdit = new Button(VaadinIcon.PENCIL.create());
		btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
		btnEdit.addClickListener(e -> editSubtask(gridSubtasks.getGrid().asSingleSelect().getValue()));

		btnDelete = new Button(VaadinIcon.TRASH.create());
		btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL);
		btnDelete.addClickListener(e -> deleteSubtask(gridSubtasks.getGrid().asSingleSelect().getValue()));

		gridSubtasks.addControlToHeader(btnNew, false);
		gridSubtasks.addControlToHeader(btnEdit, false);
		gridSubtasks.addControlToHeader(btnDelete, false);
		loadDataGrid();
		layout.add(gridSubtasks.getComponent());
	}

	private void loadDataGrid() {
		gridSubtasks.getGrid().setItems(entityDAO.loadSubtasks(task));
		UI.getCurrent().push();
	}

	protected void editSubtask(Task item) {
		VisualiceTaskWindow w = context.getBean(VisualiceTaskWindow.class, "Editar subtarea",item,false);
		w.addDetachListener(e -> loadDataGrid());
	}

	protected void deleteSubtask(Task item) {
		entityDAO.deleteTask(item);
		loadDataGrid();
	}

	protected void newSubtask() {
		CreateTaskWindow w = context.getBean(CreateTaskWindow.class, "Crear subtarea",userDAO.findUserFull(sessionManager.getUser().getUsername()),task);
		w.addDetachListener(e -> loadDataGrid());
	}
	
}
