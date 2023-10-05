package com.advenio.medere.emr.ui.framework;

import javax.annotation.PostConstruct;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.advenio.medere.IAppController;
import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.emr.ui.UIUtils;
import com.advenio.medere.emr.ui.framework.components.IMenuBuilder;
import com.advenio.medere.emr.ui.framework.components.navigation.bar.AppBar;
import com.advenio.medere.emr.ui.framework.components.navigation.bar.TabBar;
import com.advenio.medere.emr.ui.framework.components.navigation.drawer.NaviDrawer;
import com.advenio.medere.emr.ui.framework.components.navigation.drawer.NaviItem;
import com.advenio.medere.emr.ui.framework.components.navigation.drawer.NaviMenu;
import com.advenio.medere.emr.ui.framework.views.HomeView;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.components.FlexBoxLayout;
import com.advenio.medere.ui.util.LumoStyles;
import com.advenio.medere.ui.util.css.Overflow;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport(value = "./styles/components/floating-action-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/button-clickable-cursor.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/components/dialog-overlay.css", themeFor = "vaadin-dialog-overlay")
@CssImport("./styles/lumo/border-radius.css")
@CssImport("./styles/lumo/icon-size.css")
@CssImport("./styles/lumo/margin.css")
@CssImport("./styles/lumo/padding.css")
@CssImport("./styles/lumo/shadow.css")
@CssImport("./styles/lumo/spacing.css")
@CssImport("./styles/lumo/typography.css")
@CssImport("./styles/misc/box-shadow-borders.css")
@CssImport(value = "./styles/styles.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge")
@JsModule("./js/grid-styles.js")
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PreserveOnRefresh
@Push(transport = Transport.WEBSOCKET_XHR, value = PushMode.AUTOMATIC)
public class MainLayout extends FlexBoxLayout
		implements HasDynamicTitle, RouterLayout, BootstrapListener, AfterNavigationObserver {

	private static final long serialVersionUID = 4158256725031712444L;
	private static final Logger logger = LoggerFactory.getLogger(MainLayout.class);
	private static final String CLASS_NAME = "root";

	private FlexBoxLayout row;
	private NaviDrawer naviDrawer;
	private FlexBoxLayout column;

	private Div appHeaderInner;
	private FlexBoxLayout viewContainer;

	private TabBar tabBar;
	private boolean navigationTabs = false;
	private AppBar appBar;
	private UI ui;

	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private IAppController appController;
	@Autowired
	private ISessionManager vaadinSessionManager;
	@Autowired
	private IMenuBuilder menuBuilder;

	@Autowired
	private MessageBusContainer eventBus;

	public MainLayout() {

	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		this.ui = attachEvent.getUI();
		eventBus.register(this);
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
	}

	@PostConstruct
	private void init() {

		if (vaadinSessionManager.getUser() == null) {
			UI.getCurrent().getPage().setLocation("/login");
			return;
		}

		setId("MainLayout");
		VaadinSession.getCurrent().setErrorHandler((ErrorHandler) errorEvent -> {
			logger.error("Uncaught UI exception", errorEvent.getThrowable());
			Notification.show("We are sorry, but an internal error occurred");
		});

		addClassName(CLASS_NAME);
		setBackgroundColor(LumoStyles.Color.Contrast._5);
		setFlexDirection(FlexDirection.COLUMN);
		setSizeFull();

		// Initialise the UI building blocks
		initStructure();

		// Populate the navigation
		initNaviItems();

		// Configure the headers and footers (optional)
		initHeadersAndFooters();

		UI.getCurrent().getPage().setTitle(appController.getApptitle());

	}

	/**
	 * Initialise the required components and containers.
	 */
	private void initStructure() {
		naviDrawer = appContext.getBean(NaviDrawer.class);

		viewContainer = new FlexBoxLayout();
		viewContainer.addClassName(CLASS_NAME + "__view-container");
		viewContainer.setOverflow(Overflow.HIDDEN);

		column = new FlexBoxLayout(viewContainer);
		column.addClassName(CLASS_NAME + "__column");
		column.setFlexDirection(FlexDirection.COLUMN);
		column.setFlexGrow(1, viewContainer);
		column.setOverflow(Overflow.HIDDEN);

		row = new FlexBoxLayout(naviDrawer, column);
		row.addClassName(CLASS_NAME + "__row");
		row.setFlexGrow(1, column);
		row.setOverflow(Overflow.HIDDEN);
		add(row);
		setFlexGrow(1, row);
	}

	/**
	 * Initialise the navigation items.
	 */
	private void initNaviItems() {
		NaviMenu menu = naviDrawer.getMenu();
		menuBuilder.buildMenu(menu, vaadinSessionManager.loadMenu());
	}

	/**
	 * Configure the app's inner and outer headers and footers.
	 */
	private void initHeadersAndFooters() {

		appBar = appContext.getBean(AppBar.class, "");
		// Tabbed navigation
		if (navigationTabs) {
			tabBar = new TabBar();
			UIUtils.setTheme(Lumo.DARK, tabBar);

			// Shift-click to add a new tab
			for (NaviItem item : naviDrawer.getMenu().getNaviItems()) {
				item.addClickListener(e -> {
					if (e.getButton() == 0 && e.isShiftKey()) {
						tabBar.setSelectedTab(tabBar.addClosableTab(item.getText(), item.getNavigationTarget()));
					}
				});
			}
			appBar.getAvatar().setVisible(false);
			setAppHeaderInner(tabBar, appBar);

			// Default navigation
		} else {
			UIUtils.setTheme(Lumo.DARK, appBar);
			setAppHeaderInner(appBar);
		}

	}

	private void setAppHeaderInner(Component... components) {
		if (appHeaderInner == null) {
			appHeaderInner = new Div();
			appHeaderInner.addClassName("app-header-inner");
			column.getElement().insertChild(0, appHeaderInner.getElement());
		}
		appHeaderInner.removeAll();
		appHeaderInner.add(components);
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		if (this.viewContainer != null) {
			this.viewContainer.getElement().appendChild(content.getElement());
		}
	}

	public NaviDrawer getNaviDrawer() {
		return naviDrawer;
	}

	public static MainLayout get() {
		return (MainLayout) UI.getCurrent().getChildren().filter(component -> component.getClass() == MainLayout.class)
				.findFirst().get();
	}

	public AppBar getAppBar() {
		return appBar;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (vaadinSessionManager.getUser() == null) {
			UI.getCurrent().getPage().setLocation("/login");
			return;
		}

		if (navigationTabs) {
			afterNavigationWithTabs(event);
		} else {
			afterNavigationWithoutTabs(event);
		}

		UI.getCurrent().getPage().setTitle(appController.getApptitle());
	}

	private void afterNavigationWithTabs(AfterNavigationEvent e) {
		NaviItem active = getActiveItem(e);
		if (active == null) {
			if (tabBar.getTabCount() == 0) {
				tabBar.addClosableTab("", HomeView.class);
			}
		} else {
			if (tabBar.getTabCount() > 0) {
				tabBar.updateSelectedTab(active.getText(), active.getNavigationTarget());
			} else {
				tabBar.addClosableTab(active.getText(), active.getNavigationTarget());
			}
		}
		appBar.getMenuIcon().setVisible(false);
	}

	private NaviItem getActiveItem(AfterNavigationEvent e) {
		for (NaviItem item : naviDrawer.getMenu().getNaviItems()) {
			if (item.isHighlighted(e)) {
				return item;
			}
		}
		return null;
	}

	private void afterNavigationWithoutTabs(AfterNavigationEvent e) {
		
		NaviItem active = getActiveItem(e);
		if (active != null) {
			getAppBar().setTitle(active.getText());
		}
		
		UI.getCurrent().getPage().setTitle(appController.getApptitle());
	}

	@Override
	public String getPageTitle() {
		return appController.getApptitle();
	}

	@Override
	public void modifyBootstrapPage(BootstrapPageResponse response) {

		Document document = response.getDocument();
		Element head = document.head();

		head.appendChild(createMeta(document, "apple-mobile-web-app-capable", "yes"));
		head.appendChild(createMeta(document, "apple-mobile-web-app-status-bar-style", "black"));

		LoadingIndicatorConfiguration conf = response.getUI().getLoadingIndicatorConfiguration();

		head.append("<link rel=\"shortcut icon\" href=\"img/favicon.ico\">");

		// disable default theme -> loading indicator will not be shown
		// conf.setApplyDefaultTheme(false);
	}

	private Element createMeta(Document document, String property, String content) {
		Element meta = document.createElement("meta");
		meta.attr("property", property);
		meta.attr("content", content);
		return meta;
	}

}