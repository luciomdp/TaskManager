package com.advenio.medere.emr.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
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
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.User;
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
	
	public User findUserFull(String username) {
		return entityManager.createQuery("From User WHERE username = :username",User.class).setParameter("username", username).getSingleResult();
	}
	public List<User> loadUserBySectorAndProfile(Profile profile, Sector sector) {
		return entityManager.createQuery("From User WHERE sector = :sector and profile >= :profile",User.class).setParameter("sector", sector).setParameter("profile", profile).getResultList();
	}
	public List<User> loadUserBySector(Sector sector) {
		return entityManager.createQuery("From User WHERE sector = :sector ",User.class).setParameter("sector", sector).getResultList();
	}
	public List<User> loadAllUsers() {
		return entityManager.createQuery("From User",User.class).getResultList();
	}
	public List<User> loadUserByProfile(Profile profile) {
		return entityManager.createQuery("From User WHERE profile >= :profile ",User.class).setParameter("profile", profile).getResultList();
	}
	public void updateUser (User user){
		entityManager.merge(user);
	}
	
	@Override
	public UserDTO findUser(String username) {
	
		User u = findUserFull(username);

		UserDTO user = new UserDTO();
		user.setActive(true);
		user.setBlocked(false);
		user.setFirstname(u.getName());
		user.setLanguageCode("es");
		user.setLastname("");
		user.set_user(u.get_user().toString());
		user.setProfile(u.getProfile().getDescription());
		user.setProfileid(BigInteger.valueOf(u.getProfile().getProfile()));
		user.setUsername(username);
		
		SecurityConfig securityConfig = context.getBean(SecurityConfig.class);
		
		user.setPassword(securityConfig.passwordEncoder().encode(u.getPassword()));
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
