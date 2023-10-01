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
import com.advenio.medere.dao.nativeSQL.NativeSQLQueryBuilder;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.dto.DiseaseDTO;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.emr.view.edit.CRUDCie10Window;
import com.advenio.medere.emr.view.edit.CRUDSitesWindow;
import com.advenio.medere.emr.view.edit.EventStateChanged;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.MainLayout;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.GridLoadListener;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.advenio.medere.ui.components.grid.filters.config.TextFilterConfig;
import com.advenio.medere.ui.views.BaseCRUDView;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;

import net.engio.mbassy.listener.Handler;

@Route(value = "myTasks", layout = MainLayout.class)
public class MyTasksView extends BaseCRUDView<Task> implements HasDynamicTitle {

	private static final String WIDTH_MEDIUM = "100px";
	private static final String WIDTH_BIG = "200px";
	@Value("${medere.medereaddress}")
	private String medereAddress;

	private static final long serialVersionUID = -1985837633347632519L;
	protected static final Logger logger = LoggerFactory.getLogger(CreatedTasksView.class);

	@Autowired private EntityDAO entityDAO;

	@Autowired
	private NativeSQLQueryBuilder nativeQueryBuilder;

	@Autowired
	protected ISessionManager sessionManager;
	@Autowired
	protected ApplicationContext context;

	@Override
	protected void createGrid() {
		grid = new DataGrid<Task>(Task.class, true, false, FILTERMODE.FILTERMODELAZY);// primer boolean true																								// para// filtro
		grid.setLoadListener(new GridLoadListener<Task>() {
			@Override
			public Page<Task> load(PageLoadConfig<Task> loadconfig) {
				ArrayList<SortingFieldInfo> sortingFields = new ArrayList<SortingFieldInfo>();
		        SortingFieldInfo sfi = new SortingFieldInfo();
		        sfi.setFieldname("cie10");
		        sfi.setAscOrder(true);
		        sortingFields.add(sfi);
		        loadconfig.setSortingList(sortingFields);
				return entityDAO.loadMyTasks(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()),false);
			}

			@Override
			public Integer count(PageLoadConfig<Task> loadconfig) {
				return entityDAO.loadMyTasks(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()),true).getCount();
			}
		});

		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn("cie10").setHeader(sessionManager.getI18nMessage("Cie10Abreviation")).setTextAlign(ColumnTextAlign.CENTER)
		.setWidth(WIDTH_MEDIUM).setId("cie10");

		grid.getGrid().addColumn("name").setHeader(sessionManager.getI18nMessage("DiseaseName")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_BIG).setId("name");
		
		grid.init();
		
		grid.getFilterController().addFilter(new TextFilterConfig("cie10","").addField("cie10"), true);
		grid.getFilterController().addFilter(new TextFilterConfig("name","").addField("name"), true);
	
	}

	@PostConstruct
	@Override
	public void init() {
		super.init();
		titleDelete = sessionManager.getI18nMessage("CIE10View");
		titleDeleteItemText = sessionManager.getI18nMessage("AreYouSureToDeleteItem");
	}

	@Override
	public String getPageTitle() {
		return sessionManager.getI18nMessage("EditCIE10");
	}

	@Override
	protected void editItem(Task item) {
		windowOpen = true;
		CRUDCie10Window w = context.getBean(CRUDCie10Window.class, sessionManager.getI18nMessage("EditCIE10"));// sessionManager.getI18nMessage("EditMMSI"));
		//w.editItem(cie10Dao.findCie10ById(((DiseaseDTO)item).getDisease().longValue()));
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
	protected void deleteItem(Task item) {
		try {
			//cie10Dao.deleteDisease(((DiseaseDTO)item).getDisease().longValue());
		}catch(DataIntegrityViolationException e) {
			Notification.show(sessionManager.getI18nMessage("ImposibleToDeleteThereAreReferencesToThisItem")).setPosition(Position.MIDDLE);
		}
		grid.loadData();
	}

	@Override
	protected void newItem() {
		windowOpen = true;
		CRUDCie10Window w = context.getBean(CRUDCie10Window.class, sessionManager.getI18nMessage("NewCie10"));
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


