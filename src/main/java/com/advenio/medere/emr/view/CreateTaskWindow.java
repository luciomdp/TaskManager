package com.advenio.medere.emr.view;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.objects.Category;
import com.advenio.medere.emr.objects.Priority;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.objects.Priority.Priorities;
import com.advenio.medere.emr.objects.State.States;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
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
public class CreateTaskWindow extends Dialog implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";

	@Autowired private EntityDAO entityDAO;
	@Autowired private ApplicationContext context;
	@Autowired private ISessionManager sessionManager;
	@Autowired private MessageBusContainer messageBus;
	
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

	private User owner;
	private String caption;
	private Task parentTask;
	
	public CreateTaskWindow(String caption, User owner) {
		this.add(caption);
		this.owner = owner;
		parentTask = null;
	}

	public CreateTaskWindow(String caption, User owner, Task parentTask) {
		this.add(caption);
		this.owner = owner;
		this.parentTask = parentTask;
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
		Task t = new Task();
		t.setTitle(txtTitle.getValue());
		t.setDescription(txtDescription.getValue());
		t.setOwner(owner);
		t.setParentTask(null);
		t.setState(cboState.getValue());
		t.setCategory(cboCategory.getValue());
		t.setPriority(cboPriority.getValue());
		t.setSector(cboSector.getValue());
		t.setSolver(null);
		t.setDatelimit(dateLimit.getValue());
		t.setParentTask(parentTask);
		entityDAO.createTask(t);
		this.close();
	 };
	
}
