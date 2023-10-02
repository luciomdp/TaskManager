package com.advenio.medere.emr.ui;

import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.MainLayout;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.advenio.medere.ui.components.grid.filters.config.TextFilterConfig;
import com.advenio.medere.ui.views.BaseCRUDView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
	
	protected Button btnProcessExternalDrugInfo;

	@Override
	protected void createGrid() {
		grid = new DataGrid<Sector>(Sector.class, true, false, FILTERMODE.FILTERMODELAZY);// primer boolean true																								// para// filtro
		
		grid.getGrid().setItems(entityDAO.loadSectors());

		grid.getGrid().removeAllColumns();
		
		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn(e -> e.getName()!=null?e.getName():"").setHeader("Prioridad").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getDescription()!=null?e.getDescription():"").setHeader("Titulo").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);
		
		grid.getGrid().addColumn(e -> e.getSector_manager()!=null?e.getSector_manager().getName():"").setHeader("Creador").setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.init();
		
		grid.getFilterController().addFilter(new TextFilterConfig("name","").addField("name"), true);
		
	
	}

	@PostConstruct
	@Override
	public void init() {
		super.init();
		titleDelete = sessionManager.getI18nMessage("GenericDrugView");
		titleDeleteItemText = sessionManager.getI18nMessage("AreYouSureToDeleteGenericDrug");
		btnProcessExternalDrugInfo = new Button();
		btnProcessExternalDrugInfo.setIcon(VaadinIcon.FILE_PROCESS.create());   
		btnProcessExternalDrugInfo.setText(sessionManager.getI18nMessage("ProcessGenericDrugsInfo")); 
		btnProcessExternalDrugInfo.addClickListener(event -> {
			try {
				grid.loadData();
			}
			catch (Exception e) {
				Notification.show(sessionManager.getI18nMessage("OperationError"))
				.setPosition(Position.MIDDLE);
				logger.error(e.getMessage(), e);
			}
		});
		grid.addControlToHeader(btnProcessExternalDrugInfo, false);
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
		// TODO Auto-generated method stub
		try {
			//genericDrugDAO.delete(((Sector)item).getGenericdrug().longValue());
			Notification.show(sessionManager.getI18nMessage("DrugWadSuccesfullyDeleted")).setPosition(Position.MIDDLE);
		}catch(DataIntegrityViolationException e) {
			Notification.show(sessionManager.getI18nMessage("ImposibleToDeleteThereAreReferencesToThisItem")).setPosition(Position.MIDDLE);
		}
		grid.loadData();
		
	}

	private void loadDataGrid() {
		grid.getGrid().setItems(entityDAO.loadSectors());
	}
}


