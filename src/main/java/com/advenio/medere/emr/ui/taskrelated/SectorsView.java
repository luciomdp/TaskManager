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
import com.advenio.medere.emr.ui.framework.MainLayout;
import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.advenio.medere.emr.ui.framework.views.BaseCRUDView;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.ui.components.SelectUserWindow;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "sectorsGrid", layout = MainLayout.class)
public class SectorsView extends BaseCRUDView<Sector> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";
	@Value("${medere.medereaddress}")
	private String medereAddress;

	private static final long serialVersionUID = -1985837633347632519L;
	protected static final Logger logger = LoggerFactory.getLogger(SectorsView.class);

	@Autowired 
	private EntityDAO entityDAO;
	@Autowired
	protected ISessionManager sessionManager;
	@Autowired
	protected ApplicationContext context;
	@Autowired 
	protected UserDAO userDAO;
	
	//TODO armar el label de factor de carga de especialistas por seccion
	protected Label lblLoadFactor;

	@Override
	protected void createGrid() {
		//TODO cambiar a SectorDTO porque vamos a tener que tener una columna que nos indique la cantidad de especialistas por sector
		//TODO armar window de alta baja y mod de especialista por sector
		grid = new DataGrid<Sector>(Sector.class, false, false);
		
		grid.getGrid().setItems(entityDAO.loadSectors());

		grid.getGrid().removeAllColumns();
		
		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn(e -> e.getName()!=null?e.getName():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getDescription()!=null?e.getDescription():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		
		grid.getGrid().addColumn(e -> e.getSector_manager()!=null?e.getSector_manager().getName():"").setHeader("Jefe").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM).setId("sectormanager");;
		
		grid.getGrid().addItemClickListener(e -> {
			if (e.getColumn().getId().equals("sectormanager")){
				//TODO COMPLETADA armar window de cambio de jefe de sector
				SelectUserWindow w = context.getBean(SelectUserWindow.class, "Seleccionar jefe de sector", null, e.getItem().getSector());
				w.addDialogCloseActionListener(c -> {
					User u = w.getSelectedUser();
					u.setSectorspecialist(null);
					u.setAreamanager(null);
					u.setSectormanager(e.getItem());
					userDAO.updateUser(u);
				});
			}
		});
		grid.init();
		
		//TODO para las windows de cambios que debe devolver especialista, armar una window generica que devuelva un User y se tome de ahi la mod. 
	}

	@PostConstruct
	@Override
	public void init() {
		super.init();
		titleDelete = sessionManager.getI18nMessage("GenericDrugView");
		titleDeleteItemText = sessionManager.getI18nMessage("AreYouSureToDeleteGenericDrug");
		lblLoadFactor = new Label("Factor de carga: ");
		grid.addControlToHeader(lblLoadFactor, false);
	}
	@Override
	public String getPageTitle() {
		return "Sectores";
	}

	@Override
	protected void editItem(Sector item) {
		
	}

	@Override
	protected void newItem() {
		
	}
	

	@Override
	protected void deleteItem(Sector item) {
		loadDataGrid();
		
	}

	private void loadDataGrid() {
		grid.getGrid().setItems(entityDAO.loadSectors());
	}
}


