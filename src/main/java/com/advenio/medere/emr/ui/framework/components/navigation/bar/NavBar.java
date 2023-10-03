package com.advenio.medere.emr.ui.framework.components.navigation.bar;

import com.advenio.medere.emr.ui.framework.components.navigation.drawer.BrandExpression;
import com.advenio.medere.emr.ui.framework.components.navigation.tab.NaviTabs;
import com.advenio.medere.ui.components.FlexBoxLayout;
import com.advenio.medere.ui.util.LumoStyles;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class NavBar extends FlexBoxLayout {
	private static final long serialVersionUID = 4527943771038004993L;

	private String CLASS_NAME = "nav-bar";

	private VerticalLayout mainContent;
	private Button menuIcon;

	private NaviTabs tabs;
	private FlexBoxLayout tabContainer;

	private FlexBoxLayout divBtn ;
	private Tab hiddenTab ;

	public NavBar() {
		setClassName(CLASS_NAME);
		initMainContent();
		initHeader();
		initMenu();
	}

	private void initMainContent() {
		mainContent = new VerticalLayout();
		mainContent.setPadding(false);
		mainContent.setSpacing(false);
		mainContent.addClassName(CLASS_NAME + "__content");
		add(mainContent);
	}

	private void initHeader() {
		HorizontalLayout hl= new HorizontalLayout();
		hl.setWidthFull();
		hl.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		BrandExpression logo=	new BrandExpression("");

		divBtn = new FlexBoxLayout();
		divBtn.setWidthFull();
		divBtn.setJustifyContentMode(JustifyContentMode.END);

		hl.add(logo,divBtn);
		mainContent.add(hl);
	}

	private void initMenu() {
		tabs = new NaviTabs();
		tabs.setClassName(CLASS_NAME + "__tabs");
		tabs.addClassName(LumoStyles.Padding.Uniform.XS);

		hiddenTab = new Tab();
		hiddenTab.setVisible(false);
		addTab(hiddenTab);
		tabContainer = new FlexBoxLayout(this.tabs);
		tabContainer.setBackgroundColor("#bfcfdf");
		tabContainer.addClassName(CLASS_NAME + "__tab-container");
		tabContainer.setAlignItems(FlexComponent.Alignment.CENTER);
		tabContainer.setWidthFull();
		centerTabs();
		mainContent.add(tabContainer);
	}

	public void addComponentHead(Component c) {
		divBtn.add(c);
	}
	/* === MENU ICON === */

	public Button getMenuIcon() {
		return menuIcon;
	}

	/* === TABS === */

	public void addButtonTab(Button btn) {
		//		Tab tab = new Tab(btn);
		tabs.add(btn);
	}

	public void centerTabs() {
		tabs.addClassName(LumoStyles.Margin.Horizontal.AUTO);
	}

	private void configureTab(Tab tab) {
		tab.addClassName(CLASS_NAME + "__tab");
	}

	public Tab addTab(String text) {
		Tab tab = tabs.addTab(text);
		configureTab(tab);
		return tab;
	}

	public Tab addTab(String text,
			Class<? extends Component> navigationTarget) {
		Tab tab = tabs.addTab(text, navigationTarget);
		tab.addClassName(LumoStyles.Heading.H4);
		configureTab(tab);
		return tab;
	}

	public Tab addTab(Tab tab) {
		tabs.add(tab);
		configureTab(tab);
		return tab;
	}

	public Tab addClosableTab(String text,
			Class<? extends Component> navigationTarget) {
		Tab tab = tabs.addClosableTab(text, navigationTarget);
		configureTab(tab);
		return tab;
	}

	public Tab getSelectedTab() {
		return tabs.getSelectedTab();
	}

	public void setSelectedTab(Tab selectedTab) {
		tabs.setSelectedTab(selectedTab);
	}

	public void updateSelectedTab(String text,
			Class<? extends Component> navigationTarget) {
		tabs.updateSelectedTab(text, navigationTarget);
	}

	public void addTabSelectionListener(
			ComponentEventListener<Tabs.SelectedChangeEvent> listener) {
		tabs.addSelectedChangeListener(listener);
	}

	public int getTabCount() {
		return tabs.getTabCount();
	}

	public void removeAllTabs() {
		tabs.removeAll();
	}

	public NaviTabs getTabs() {
		return tabs;
	}

	// Selecciona tab oculto para limpiar la seleccion de los tabs visibles
	public void hideSelection() {
		if(hiddenTab!=null) {
			setSelectedTab(hiddenTab);
		}
	}
}
