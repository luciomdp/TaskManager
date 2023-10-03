package com.advenio.medere.emr.ui.framework.views;

import com.advenio.medere.emr.ui.framework.MainLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

@Route(value = "home", layout = MainLayout.class)
public class HomeView extends SplitViewFrame implements HasDynamicTitle {

	private static final long serialVersionUID = -2531929645091117022L;

	@Override
	public String getPageTitle() {		
		return "Home";
	}

}
