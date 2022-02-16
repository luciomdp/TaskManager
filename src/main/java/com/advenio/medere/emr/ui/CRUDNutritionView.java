package com.advenio.medere.emr.ui;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

import com.advenio.medere.dao.SortingFieldInfo;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.NutritionalItemDAO;
import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.dao.dto.NutritionalItemDTO;
import com.advenio.medere.emr.view.edit.CRUDNutritionWindow;
import com.advenio.medere.emr.view.edit.EventStateChanged;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.MainLayout;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.GridLoadListener;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.advenio.medere.ui.components.grid.filters.config.BooleanFilterConfig;
import com.advenio.medere.ui.components.grid.filters.config.TextFilterConfig;
import com.advenio.medere.ui.views.BaseCRUDView;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;

import net.engio.mbassy.listener.Handler;

@Route(value = "nutritionGrid", layout = MainLayout.class)
public class CRUDNutritionView extends BaseCRUDView<NutritionalItemDTO> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";
	@Value("${medere.medereaddress}")
	private String medereAddress;

	private static final long serialVersionUID = -1985837633347632519L;
	protected static final Logger logger = LoggerFactory.getLogger(CRUDSitesView.class);

	@Autowired
	protected SiteDAO siteDAO;
	@Autowired
	protected MedereDAO medereDAO;
	@Autowired
	protected ISessionManager sessionManager;
	@Autowired
	protected ApplicationContext context;
	@Autowired protected NutritionalItemDAO nutritionalItemDAO;

	@Override
	protected void createGrid() {
		grid = new DataGrid<NutritionalItemDTO>(NutritionalItemDTO.class, true, false, FILTERMODE.FILTERMODELAZY);// primer boolean true																								// para// filtro
		grid.setLoadListener(new GridLoadListener<NutritionalItemDTO>() {
			@Override
			public Page<NutritionalItemDTO> load(PageLoadConfig<NutritionalItemDTO> loadconfig) {
				ArrayList<SortingFieldInfo> sortingFields = new ArrayList<SortingFieldInfo>();
		        SortingFieldInfo sfi = new SortingFieldInfo();
		        sfi.setFieldname("description");
		        sfi.setAscOrder(true);
		        sortingFields.add(sfi);
		        loadconfig.setSortingList(sortingFields);
				return nutritionalItemDAO.loadNutritionalItemList(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()));
			}

			@Override
			public Integer count(PageLoadConfig<NutritionalItemDTO> loadconfig) {
				return nutritionalItemDAO.loadNutritionalItemList(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId())).getCount();
			}
		});

		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn("description").setHeader(sessionManager.getI18nMessage("Description")).setTextAlign(ColumnTextAlign.CENTER)
		.setWidth(WIDTH_MEDIUM).setId("description");

		grid.getGrid().addColumn("typedescription").setHeader(sessionManager.getI18nMessage("Category")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_BIG).setId("typedescription");
		
		grid.getGrid().addColumn(new ComponentRenderer<>(e-> booleanRender(e.isActive()))).setHeader(sessionManager.getI18nMessage("Active")).setTextAlign(ColumnTextAlign.CENTER)
		.setWidth(WIDTH_BIG).setId("active");
		
		grid.init();
		
		grid.getFilterController().addFilter(new TextFilterConfig("description","").addField("nutritionalitem.description"), true);
	
	}

	@PostConstruct
	@Override
	public void init() {
		super.init();
		titleDeleteItemText = sessionManager.getI18nMessage("AreYouSureToDeleteItem");
	}

	@Override
	public String getPageTitle() {
		return sessionManager.getI18nMessage("EditCIE10");
	}

	@Override
	protected void editItem(NutritionalItemDTO item) {
		windowOpen = true;
		CRUDNutritionWindow w = context.getBean(CRUDNutritionWindow.class, sessionManager.getI18nMessage("EditItem"));// sessionManager.getI18nMessage("EditMMSI"));
		w.editItem(nutritionalItemDAO.findNutritionalItemById(((NutritionalItemDTO)item).getNutritionalitem().longValue(),
				medereDAO.loadLanguage(sessionManager.getUser().getLanguageId())));
		w.addDetachListener(new ComponentEventListener<DetachEvent>() {
			@Override
            public void onComponentEvent(DetachEvent event) {
                windowOpen = false;
                if (pendingRefresh) {
                    pendingRefresh = false;
                    grid.loadData();
                }
            }
		});
	}

	@Override
	protected void deleteItem(NutritionalItemDTO item) {
		try {
			nutritionalItemDAO.deleteNutritionalItem(((NutritionalItemDTO)item).getNutritionalitem().longValue());
		}catch(DataIntegrityViolationException e) {
			Notification.show(sessionManager.getI18nMessage("ImposibleToDeleteThereAreReferencesToThisItem")).setPosition(Position.MIDDLE);
		}
		grid.loadData();
	}

	@Override
	protected void newItem() {
		windowOpen = true;
		CRUDNutritionWindow w = context.getBean(CRUDNutritionWindow.class, sessionManager.getI18nMessage("NewItem"));
		w.addDetachListener(new ComponentEventListener<DetachEvent>() {
			@Override
            public void onComponentEvent(DetachEvent event) {
                windowOpen = false;
                if (pendingRefresh) {
                    pendingRefresh = false;
                    grid.loadData();
                }
            }
		});
	}
	
	@Handler
    public void handleEvent(EventStateChanged event) {
        if ((getUI().isPresent()) && (!getUI().get().isClosing()) ) {
            getUI().get().access(new Command() {
                private static final long serialVersionUID = 7766674267731647725L;
                @Override
                public void execute() {
                    if (windowOpen) {
                        pendingRefresh = true;
                    }else {
                        grid.loadData();
                    }
                }
            });
        }
    }

}