package com.advenio.medere.emr.ui;

import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.MedereDAO;
import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.dao.dto.SiteDTO;
import com.advenio.medere.emr.view.edit.CRUDSitesWindow;
import com.advenio.medere.emr.view.edit.EventStateChanged;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.MainLayout;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.GridLoadListener;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.advenio.medere.ui.components.grid.filters.config.TextFilterConfig;
import com.advenio.medere.ui.views.BaseCRUDView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;

import net.engio.mbassy.listener.Handler;

@Route(value = "siteGrid", layout = MainLayout.class)
public class CRUDSitesView extends BaseCRUDView<SiteDTO> implements HasDynamicTitle {

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
	@Autowired protected UserDAO userDAO;

	@Override
	protected void createGrid() {
		grid = new DataGrid<SiteDTO>(SiteDTO.class, true, false, FILTERMODE.FILTERMODELAZY);// primer boolean true																								// para// filtro
		grid.setLoadListener(new GridLoadListener<SiteDTO>() {
			@Override
			public Page<SiteDTO> load(PageLoadConfig<SiteDTO> loadconfig) {
				return siteDAO.loadSites(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()));
			}

			@Override
			public Integer count(PageLoadConfig<SiteDTO> loadconfig) {
				return siteDAO.countSites(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()));
			}
		});

		grid.getGrid().removeAllColumns();

		grid.getGrid().addColumn("site").setHeader(sessionManager.getI18nMessage("SiteId")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(new ComponentRenderer<>(e-> booleanRender(e.isActive()))).setHeader(sessionManager.getI18nMessage("Active")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn("apptitle").setHeader(sessionManager.getI18nMessage("Title")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_MEDIUM).setId("apptitle");
		
		grid.getGrid().addColumn("companyname").setHeader(sessionManager.getI18nMessage("Name")).setTextAlign(ColumnTextAlign.CENTER)
		.setWidth(WIDTH_MEDIUM).setId("companyname");
		
		grid.getGrid().addColumn("companywebsite").setHeader(sessionManager.getI18nMessage("Website"))
		.setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_BIG);

		grid.getGrid().addColumn("companyaddress").setHeader(sessionManager.getI18nMessage("Address"))
				.setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn("companyemail").setHeader(sessionManager.getI18nMessage("Email")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn("companytelephone").setHeader(sessionManager.getI18nMessage("Phone"))
				.setTextAlign(ColumnTextAlign.CENTER).setWidth(WIDTH_MEDIUM);

		grid.getGrid().addColumn(new ComponentRenderer<>(e-> booleanRender(e.isDefaultsite()))).setHeader(sessionManager.getI18nMessage("DefaultSite")).setTextAlign(ColumnTextAlign.CENTER)
				.setWidth(WIDTH_MEDIUM);
		grid.init();
		
		grid.getFilterController().addFilter(new TextFilterConfig("apptitle","").addField("apptitle"), true);
		grid.getFilterController().addFilter(new TextFilterConfig("companyname","").addField("companyname"), true);
	
	}

	@PostConstruct
	@Override
	public void init() {
		createGrid();
		grid.getGrid().addSelectionListener(new SelectionListener<Grid<SiteDTO>, SiteDTO>() {

			private static final long serialVersionUID = -1266658791714326144L;

			@Override
			public void selectionChange(SelectionEvent<Grid<SiteDTO>, SiteDTO> event) {
				if (event.getFirstSelectedItem().isPresent()) {
					editItem(grid.getGrid().asSingleSelect().getValue());
				}
			}
		});

		Button btnNew = new Button(VaadinIcon.PLUS.create());
		btnNew.addThemeVariants(ButtonVariant.LUMO_SMALL);
		btnNew.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			
			private static final long serialVersionUID = -4512181173967300148L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				newItem();
			}
		});		
		grid.addControlToHeader(btnNew, false);
		setViewContent(grid.getComponent());
		titleDelete = sessionManager.getI18nMessage("SiteView");
		titleDeleteItemText = sessionManager.getI18nMessage("AreYouSureToDeleteSite");
	}

	@Override
	public String getPageTitle() {
		return sessionManager.getI18nMessage("EditSite");
	}

	@Override
	protected void editItem(SiteDTO item) {
		windowOpen = true;
		CRUDSitesWindow w = context.getBean(CRUDSitesWindow.class, sessionManager.getI18nMessage("EditSite"));// sessionManager.getI18nMessage("EditMMSI"));
		w.editItem(siteDAO.loadSite(((SiteDTO)item).getSite().longValue()));
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
	protected void deleteItem(SiteDTO item) {

	}

	@Override
	protected void newItem() {
		windowOpen = true;
		CRUDSitesWindow w = context.getBean(CRUDSitesWindow.class, sessionManager.getI18nMessage("NewSite"));
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


