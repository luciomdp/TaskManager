package com.advenio.medere.emr.ui.taskrelated;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.ui.framework.MainLayout;
import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.advenio.medere.emr.ui.framework.views.BaseCRUDView;
import com.advenio.medere.emr.view.VisualiceTaskWindow;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "myTasks", layout = MainLayout.class)
public class MyTasksView extends BaseCRUDView<Task> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";
	@Value("${medere.medereaddress}")
	private String medereAddress;

	private static final long serialVersionUID = -1985837633347632519L;
	private static final Logger logger = LoggerFactory.getLogger(CreatedTasksView.class);

	@Autowired private EntityDAO entityDAO;
	@Autowired private UserDAO userDAO;
	@Autowired
	private ISessionManager sessionManager;
	@Autowired
	private ApplicationContext context;

	@Override
	protected void createGrid() {
		grid = new DataGrid<Task>(Task.class, false, false);
		
		grid.getGrid().setItems(entityDAO.loadMyTasks(userDAO.findUserFull(sessionManager.getUser().getUsername())));

		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn(e -> e.getPriority()!=null?e.getPriority().getDescription():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getTitle()!=null?e.getTitle():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getOwner()!=null?e.getOwner().getName():"").setHeader("Creador").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(e -> e.getDatelimit()!=null?e.getDatelimit():"").setHeader("Fecha limite").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(e ->e.getSolver()!=null? e.getSolver().getName():"").setHeader("Resolutor").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
	
		grid.init();
	}

	@PostConstruct
	@Override
	public void init() {
		super.init();

		grid.getGrid().addSelectionListener(new SelectionListener<Grid<Task>, Task>() {
			private static final long serialVersionUID = -1266658791714326144L;
			@Override
			public void selectionChange(SelectionEvent<Grid<Task>, Task> event) {
				if (event.getFirstSelectedItem().isPresent()) {
					editItem(grid.getGrid().asSingleSelect().getValue());
				}
			}
		});

		btnDelete.setVisible(false);
		btnNew.setVisible(false);
	}

	@Override
	public String getPageTitle() {
		return "Tareas asignadas";
	}

	@Override
	protected void editItem(Task item) {
		VisualiceTaskWindow w = context.getBean(VisualiceTaskWindow.class, "Editar tarea", item, null);
		w.addDetachListener(e ->loadDataGrid());
	}

	@Override
	protected void deleteItem(Task item) {
	}

	@Override
	protected void newItem() {
	}

	private void loadDataGrid() {
		grid.getGrid().setItems(entityDAO.loadMyTasks(userDAO.findUserFull(sessionManager.getUser().getUsername())));
		UI.getCurrent().push();
	}

}