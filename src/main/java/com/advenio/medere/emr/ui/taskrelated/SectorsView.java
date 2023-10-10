package com.advenio.medere.emr.ui.taskrelated;

import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.dao.dto.SectorDTO;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.ui.framework.MainLayout;
import com.advenio.medere.emr.ui.framework.components.grid.DataGrid;
import com.advenio.medere.emr.ui.framework.views.BaseCRUDView;
import com.advenio.medere.emr.view.CreateSectorWIndow;
import com.advenio.medere.emr.view.SelectUserWindow;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "sectorsView", layout = MainLayout.class)
public class SectorsView extends BaseCRUDView<SectorDTO> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";
	private final String DEMAND_FACTOR = "Factor de carga: ";
	@Value("${medere.medereaddress}")
	private String medereAddress;

	private static final long serialVersionUID = -1985837633347632519L;
	private static final Logger logger = LoggerFactory.getLogger(SectorsView.class);

	@Autowired 
	private EntityDAO entityDAO;
	@Autowired
	private ISessionManager sessionManager;
	@Autowired
	private ApplicationContext context;
	@Autowired 
	private UserDAO userDAO;
	
	//TODO armar el label de factor de carga de especialistas por seccion
	private Label lblLoadFactor;
	// cantidad de pedidos en estado “por realizar” dividido la cantidad de especialistas de un sector

	@Override
	protected void createGrid() {
		//TODO cambiar a SectorDTO porque vamos a tener que tener una columna que nos indique la cantidad de especialistas por sector
		//TODO armar window de alta baja y mod de especialista por sector
		grid = new DataGrid<SectorDTO>(SectorDTO.class, false, false);
		lblLoadFactor = new Label(DEMAND_FACTOR);
		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn(e -> e.getName()!=null?e.getName():"").setHeader("Nombre").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getDescription()!=null?e.getDescription():"").setHeader("Descripción").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getSectormanagermame()).setHeader("Jefe de sector").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM).setId("sectormanager");

		grid.getGrid().addColumn(e -> e.getQtyemployeers()).setHeader("Especialistas asignados").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM).setId("qtyemployeers");

		grid.getGrid().addColumn(e -> e.getLoadfactor()).setHeader("Factor de carga").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM).setId("loadfactor");
		
		grid.getGrid().addItemClickListener(item -> {
			if(!item.getColumn().getId().isPresent())
				return;
			if(item.getColumn().getId().get().equals("sectormanager")) {
				SelectUserWindow w = context.getBean(SelectUserWindow.class, "Seleccionar jefe de sector", null, entityDAO.loadSector(item.getItem().getSector().longValue()));
				w.addDetachListener(c -> {
					User u = w.getSelectedUser();
					u.setSectorspecialist(entityDAO.loadSector(item.getItem().getSector().longValue()));
					u.setAreamanager(null);
					u.setSectormanager(entityDAO.loadSector(item.getItem().getSector().longValue()));
					userDAO.updateUser(u);
				});
			}
		});
		
		//grid.addControlToHeader(lblLoadFactor, false);

		loadDataGrid();

		grid.init();

		
		//TODO para las windows de cambios que debe devolver especialista, armar una window generica que devuelva un User y se tome de ahi la mod. 
	}

	@PostConstruct
	@Override
	public void init() {
		super.init();
		btnNew.addClickListener(e -> {
				CreateSectorWIndow w = context.getBean(CreateSectorWIndow.class, "Seleccionar jefe de sector");
		});
		titleDelete = sessionManager.getI18nMessage("GenericDrugView");
		titleDeleteItemText = sessionManager.getI18nMessage("AreYouSureToDeleteGenericDrug");

	}
	@Override
	public String getPageTitle() {
		return "Sectores";
	}

	@Override
	protected void editItem(SectorDTO item) {
		
	}

	@Override
	protected void newItem() {
		
	}
	

	@Override
	protected void deleteItem(SectorDTO item) {
		loadDataGrid();
		
	}

	private void loadDataGrid() {
		grid.getGrid().setItems(entityDAO.loadSectorInfo().getData());
		UI.getCurrent().push();
	}
}


