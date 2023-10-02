package com.advenio.medere.emr.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.IUserDAO;
import com.advenio.medere.emr.objects.Profile;
import com.advenio.medere.emr.objects.Profile.Profiles;
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.dto.users.IPLoginAttemptDTO;
import com.advenio.medere.objects.dto.users.UserDTO;
import com.advenio.medere.security.SecurityConfig;
import com.advenio.medere.ui.components.menu.MenuItemDTO;

@Service
@Primary
public class UserDAO implements IUserDAO {
	
	@Value("${userPassword}")     
	private String userPassword;
	@Value("${showSite}")     
	private boolean showSite;
	@Autowired
	protected ApplicationContext context;
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	@Override
	public void clearAttempts(String username, String remoteAddress) {
				
	}
	
	
	@Override
	public UserDTO findUser(String username) {
	
		
		UserDTO user = new UserDTO();
		user.setActive(true);
		user.setBlocked(false);
		user.setFirstname("Dummy");
		user.setLanguageCode("es");
		user.setLastname("User");
		user.set_user("1");
		user.setProfile("admin");
		user.setProfileid(BigInteger.valueOf(1));
		user.setUsername(username);
		
		SecurityConfig securityConfig = context.getBean(SecurityConfig.class);
		
		user.setPassword(securityConfig.passwordEncoder().encode(userPassword));
		user.setLanguageId(1);
		return user;
	}

	@Override
	public IPLoginAttemptDTO getIPLoginAttempt(String remoteAddress) {
		IPLoginAttemptDTO attempt = new IPLoginAttemptDTO();
		attempt.setBlocked(false);
		attempt.setIp(remoteAddress);
		attempt.setLastAttemptTime(new Date());
		attempt.setQty(1);
		
		return attempt;
	}

	@Override
	public IPLoginAttemptDTO saveIPLoginAttempt(String remoteAddress) {
		IPLoginAttemptDTO attempt = new IPLoginAttemptDTO();
		attempt.setBlocked(false);
		attempt.setIp(remoteAddress);
		attempt.setLastAttemptTime(new Date());
		attempt.setQty(1);
		
		return attempt;
	}

	@Override
	public int saveUserLoginAttempt(String username) {
		return 1;
	}

	@Override
	public List<MenuItemDTO> loadMenu(long profileId,long languageId) {
		List<MenuItemDTO> menu = new ArrayList<MenuItemDTO>();
		int i = 1;

		MenuItemDTO createdTasksGrid = new MenuItemDTO();
		createdTasksGrid.setIcon("home");
		createdTasksGrid.setItemClassName("com.advenio.medere.emr.ui.CreatedTasksView");
		createdTasksGrid.setItemId(Long.valueOf(i));
		createdTasksGrid.setItemName("Tareas creadas");
		createdTasksGrid.setOrder(i++);
		menu.add(createdTasksGrid);

		if((profileId == Profiles.SPECIALIST.getValue()) || (profileId == Profiles.SECTOR_MANAGER.getValue()) || (profileId == Profiles.AREA_MANAGER.getValue())) {
			MenuItemDTO myTasksGrid = new MenuItemDTO();
			myTasksGrid.setIcon("home");
			myTasksGrid.setItemClassName("com.advenio.medere.emr.ui.MyTasksView");
			myTasksGrid.setItemId(Long.valueOf(i));
			myTasksGrid.setItemName("Mis tareas");
			myTasksGrid.setOrder(i++);
			menu.add(myTasksGrid);
		}
		if((profileId == Profiles.SECTOR_MANAGER.getValue()) || (profileId == Profiles.AREA_MANAGER.getValue())) {
			MenuItemDTO sectorTasksGrid = new MenuItemDTO();
			sectorTasksGrid.setIcon("home");
			sectorTasksGrid.setItemClassName("com.advenio.medere.emr.ui.SectorsTasksView");
			sectorTasksGrid.setItemId(Long.valueOf(i));
			sectorTasksGrid.setItemName("Tareas de sector");
			sectorTasksGrid.setOrder(i++);
			menu.add(sectorTasksGrid);
		}
		if(profileId == Profiles.AREA_MANAGER.getValue()) {
			MenuItemDTO sectorsGrid = new MenuItemDTO();
			sectorsGrid.setIcon("home");
			sectorsGrid.setItemClassName("com.advenio.medere.emr.ui.SectorsView");
			sectorsGrid.setItemId(Long.valueOf(i));
			sectorsGrid.setItemName("Lista de sectores");
			sectorsGrid.setOrder(i++);
			menu.add(sectorsGrid);
		}
		
		return menu;
	}
	
	@Override
	public boolean changeUserPassword(Object userid,String hashedOldPassword,String hashedNewPassword) {
		return true;
	}

	@Override
	public void changeUserLanguage(Object userid, long languageId) {
	
	}

}
