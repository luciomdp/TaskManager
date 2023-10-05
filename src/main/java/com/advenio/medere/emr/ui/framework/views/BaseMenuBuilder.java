package com.advenio.medere.emr.ui.framework.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.advenio.medere.emr.ui.framework.components.IMenuBuilder;
import com.advenio.medere.emr.ui.framework.components.navigation.drawer.NaviItem;
import com.advenio.medere.emr.ui.framework.components.navigation.drawer.NaviMenu;
import com.advenio.medere.ui.components.menu.MenuItemDTO;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;

@Service
public class BaseMenuBuilder implements IMenuBuilder {

	private static final Logger logger = LoggerFactory.getLogger(BaseMenuBuilder.class);
	
	@Autowired
	ResourceLoader resourceLoader;  
	
	private void addToTree(MenuItemDTO menuItem,List<MenuItemDTO> rootMenu,List<MenuItemDTO> menuOptions) {
		
		if (menuItem.getParentId() == null) {
			rootMenu.add(menuItem);
			return;
		}
		
		MenuItemDTO parent = findParent(menuItem, rootMenu);
		if (parent != null) {
			if (!parent.getChilds().stream().filter(e-> e.getItemId().longValue() == menuItem.getItemId().longValue()).findFirst().isPresent()) {
				parent.getChilds().add(menuItem);	
			}			
			return;
		}
		
		parent =  menuOptions.stream().filter(e-> e.getItemId().longValue() == menuItem.getParentId().longValue()).findFirst().get();
		addToTree(parent, rootMenu, menuOptions);
		if (!parent.getChilds().stream().filter(e-> e.getItemId().longValue() == menuItem.getItemId().longValue()).findFirst().isPresent()) {
			parent.getChilds().add(menuItem);
		}
	}

	private MenuItemDTO findParent(MenuItemDTO menuItem,List<MenuItemDTO> rootMenu) {
		for (MenuItemDTO mParent : rootMenu) {
			if (mParent.getItemId().longValue() == menuItem.getParentId().longValue()) {
				return mParent;
			}
			
			if (!mParent.getChilds().isEmpty()) {
				MenuItemDTO mParent_ = findParent(menuItem, mParent.getChilds());
				if (mParent_!=null) {
					return mParent_;
				}	
			}			
		}		
		return null;
	}
	

	private List<MenuItemDTO> buildRootMenu(List<MenuItemDTO> menuOptions) {
		List<MenuItemDTO> rootMenu = new ArrayList<MenuItemDTO>();		
		for (MenuItemDTO menuItem : menuOptions) {
			addToTree(menuItem, rootMenu, menuOptions);
		}

		// sort
		sortItems(rootMenu);
		return rootMenu;
	}
	
	private void sortItems(List<MenuItemDTO> menuOptions) {
		
		Collections.sort(menuOptions,
				new Comparator<MenuItemDTO>(){
					@Override
					public int compare(MenuItemDTO o1, MenuItemDTO o2) {
						return o1.getOrder() < o2.getOrder() ? -1 : (o1.getOrder() > o2.getOrder() ) ? 1 : 0;
					}
			
		});
		
		for(MenuItemDTO menuItem:menuOptions) {
			sortItems(menuItem.getChilds());
		}
	}
	
	private void makeMenu(NaviItem parentItem,NaviMenu menu, List<MenuItemDTO> menuOptions) {
		// build menu
		for(MenuItemDTO menuItem:menuOptions) {
			Class _class = null;
			if (!StringsUtils.isNullOrEmptyTrimmed(menuItem.getItemClassName())) {
				try {
					_class = Class.forName(menuItem.getItemClassName());
				} catch (ClassNotFoundException e) {
					logger.error(e.getMessage(),e);
				}
			}			
			
			VaadinIcon icon = null;
			Image image = null;
			if (!StringsUtils.isNullOrEmptyTrimmed(menuItem.getIcon())) {
				try {
					icon = VaadinIcon.valueOf(menuItem.getIcon());
				}catch(Exception e) {
					
				}
				
				if (icon == null) {
					if (resourceLoader.getResource("imgs/menu/"+menuItem.getIcon()+".png").exists()) {
						image = new Image("imgs/menu/"+menuItem.getIcon()+".png", "");	
					}					
				}
			}
			NaviItem item = null;
			if (parentItem == null) {
				if (image!=null) {
					item = menu.addNaviItem(image , menuItem.getItemName(),_class);	
				}else {
					if (icon!=null) {
						item = menu.addNaviItem(icon , menuItem.getItemName(),_class);	
					}else {
						item = menu.addNaviItem(menuItem.getItemName(),_class);
					}
					
				}								
			}else {				
				if (image!=null) {
					item = new NaviItem(image,  menuItem.getItemName(),_class);
				}else {
					if (icon!=null) {
						item = new NaviItem(icon,  menuItem.getItemName(),_class);	
					}else {
						item = menu.addNaviItem(menuItem.getItemName(),_class);
					}
					
				}
				menu.addNaviItem(parentItem, item);				
			}
			
			if (!menuItem.getChilds().isEmpty()) {
				makeMenu(item, menu, menuItem.getChilds());
			}			
		}
	}
	
	@Override
	public void buildMenu(NaviMenu menu, List<MenuItemDTO> menuOptions) {
		List<MenuItemDTO> menuWithChilds = buildRootMenu(menuOptions);
		makeMenu(null, menu, menuWithChilds);	
	}

}
