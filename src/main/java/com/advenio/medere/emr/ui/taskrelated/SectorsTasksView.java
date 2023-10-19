package com.advenio.medere.emr.ui.taskrelated;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.objects.Profile.Profiles;
import com.advenio.medere.emr.ui.framework.MainLayout;
import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.advenio.medere.emr.ui.framework.views.BaseCRUDView;
import com.advenio.medere.emr.view.SelectUserWindow;
import com.advenio.medere.emr.view.VisualiceTaskWindow;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "sectorsTasksView", layout = MainLayout.class)
public class SectorsTasksView extends BaseCRUDView<Task> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	@Value("${medere.medereaddress}")
	private String medereAddress;

	private static final long serialVersionUID = -1985837633347632519L;
	private static final Logger logger = LoggerFactory.getLogger(CreatedTasksView.class);

	private ComboBox<Sector> cboSector;
	private User user;

	@Autowired
	private EntityDAO entityDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ISessionManager sessionManager;
	@Autowired
	private ApplicationContext context;

	@PostConstruct
	@Override
	public void init() {
		super.init();

		user = userDAO.findUserFull(sessionManager.getUser().getUsername());

		cboSector = new ComboBox<Sector>("Sector");
		cboSector.setSizeFull();
		cboSector.setItems(entityDAO.loadSectors());
		cboSector.setItemLabelGenerator(e -> e.getDescription());
		if(user.getProfile().getProfile().longValue() == Profiles.SECTOR_MANAGER.getValue()) 	
			cboSector.setEnabled(false);
		cboSector.addValueChangeListener(e -> {
			loadDataGrid();
		});
		cboSector.setValue(user.getSectormanager()!=null?user.getSectormanager():user.getSector());

		
		grid.addControlToHeader(cboSector, false);

		btnNew.setVisible(false);
		titleDelete = "Borrar tarea";
		titleDeleteItemText = "Estas seguro de borrar esa tarea?";

		loadDataGrid();
	}

	@Override
	protected void createGrid() {
		grid = new DataGrid<Task>(Task.class, false, false);

		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn(e -> e.getPriority()!=null?e.getPriority().getDescription():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getTitle()!=null?e.getTitle():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getOwner()!=null?e.getOwner().getName():"").setHeader("Creador").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(e -> e.getDatelimit()!=null?e.getDatelimit():"").setHeader("Fecha limite").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(e ->e.getSolver()!=null? e.getSolver().getName():"").setHeader("Resolutor").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM).setId("solver");

		grid.getGrid().addItemClickListener(item -> {
			if(!item.getColumn().getId().isPresent())
				editItem(grid.getGrid().asSingleSelect().getValue());
			else if(item.getColumn().getId().get().equals("solver")) {
				SelectUserWindow w = context.getBean(SelectUserWindow.class, "Resolutor", null, item.getItem().getCategory().getSector());
				w.addDetachListener(c -> {
					Task selectedTask = item.getItem();
					selectedTask.setSolver(w.getSelectedUser());
					entityDAO.updateTask(selectedTask);
					loadDataGrid();
				});
			}	
			return;	
		});

		grid.init();
		
	}

	@Override
	public String getPageTitle() {
		return "Visualizar tareas de sector";
	}

	@Override
	protected void editItem(Task item) {
		VisualiceTaskWindow w = context.getBean(VisualiceTaskWindow.class, "Editar tarea",item,null);
		w.addDetachListener(e ->loadDataGrid());
	}

	@Override
	protected void deleteItem(Task item) {
		entityDAO.deleteTask(item);
		loadDataGrid();
	}

	@Override
	protected void newItem() {
	}

	private void loadDataGrid() {
		grid.getGrid().setItems(entityDAO.loadSectorsTasks(cboSector.getValue()));
		UI.getCurrent().push();
	}

}