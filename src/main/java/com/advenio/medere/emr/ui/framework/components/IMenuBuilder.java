package com.advenio.medere.emr.ui.framework.components;

import java.util.List;

import com.advenio.medere.emr.ui.framework.components.navigation.drawer.NaviMenu;
import com.advenio.medere.ui.components.menu.MenuItemDTO;

public interface IMenuBuilder {
	
	public void buildMenu(NaviMenu menu,List<MenuItemDTO> menuOptions);
}
