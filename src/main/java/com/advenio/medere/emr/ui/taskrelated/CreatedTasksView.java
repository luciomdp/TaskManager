package com.advenio.medere.emr.ui.taskrelated;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.ui.framework.BaseCRUDView;
import com.advenio.medere.emr.view.CreateTaskWindow;
import com.advenio.medere.emr.view.VisualiceTaskWindow;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.MainLayout;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.advenio.medere.ui.components.grid.filters.config.TextFilterConfig;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "createTask", layout = MainLayout.class)
public class CreatedTasksView extends BaseCRUDView<Task> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";

	private static final long serialVersionUID = -1985837633347632519L;
	protected static final Logger logger = LoggerFactory.getLogger(CreatedTasksView.class);

	@Autowired
	protected EntityDAO entityDAO;
	@Autowired
	protected UserDAO userDAO;

	@Autowired
	protected ISessionManager sessionManager;
	@Autowired
	protected ApplicationContext context;

	@Override
	protected void createGrid() {
		grid = new DataGrid<Task>(Task.class, true, false, FILTERMODE.FILTERMODELAZY);// primer boolean true	
																								
		grid.getGrid().setItems(entityDAO.loadCreatedTasks(userDAO.findUserFull(sessionManager.getUser().getUsername())));

		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn(e -> e.getPriority()!=null?e.getPriority().getDescription():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getTitle()!=null?e.getTitle():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getOwner()!=null?e.getOwner().getName():"").setHeader("Creador").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(e -> e.getDatelimit()!=null?e.getDatelimit():"").setHeader("Fecha limite").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(e ->e.getSolver()!=null? e.getSolver().getName():"").setHeader("Resolutor").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);


		grid.init();
		
		grid.getFilterController().addFilter(new TextFilterConfig("apptitle","").addField("apptitle"), true);
		grid.getFilterController().addFilter(new TextFilterConfig("companyname","").addField("companyname"), true);
	
	}

	@PostConstruct
	@Override
	public void init() {
		createGrid();
		
		grid.getGrid().addSelectionListener(new SelectionListener<Grid<Task>, Task>() {

			private static final long serialVersionUID = -1266658791714326144L;

			@Override
			public void selectionChange(SelectionEvent<Grid<Task>, Task> event) {
				if (event.getFirstSelectedItem().isPresent()) {
					editItem(grid.getGrid().asSingleSelect().getValue());
				}
			}
		});

		titleDelete = "Borrar tarea";
		titleDeleteItemText = "Estas seguro de borrar esa tarea?";
	}


	@Override
	public String getPageTitle() {
		return "Tareas creadas";
	}

	@Override
	protected void editItem(Task item) {
		VisualiceTaskWindow w = context.getBean(VisualiceTaskWindow.class, "Editar tarea",item,true);
		w.addDetachListener(new ComponentEventListener<DetachEvent>() {
			@Override
            public void onComponentEvent(DetachEvent event) {
                loadDataGrid();
            }
		});
	}

	@Override
	protected void deleteItem(Task item) {
		entityDAO.deleteTask(item);
		loadDataGrid();
	}

	@Override
	protected void newItem() {
		CreateTaskWindow w = context.getBean(CreateTaskWindow.class, "Crear tarea",sessionManager.getUser());
		w.addDetachListener(new ComponentEventListener<DetachEvent>() {
			@Override
            public void onComponentEvent(DetachEvent event) {
                loadDataGrid();
            }
		});
	}
	
	private void loadDataGrid() {
		grid.getGrid().setItems(entityDAO.loadCreatedTasks(null));
	}

}


