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
import com.advenio.medere.objects.Language;
import com.advenio.medere.objects.dto.users.IPLoginAttemptDTO;
import com.advenio.medere.objects.dto.users.UserDTO;
import com.advenio.medere.objects.user.Profile;
import com.advenio.medere.objects.user.User;
import com.advenio.medere.objects.user.i18n.ProfileI18n;
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
	
	public User findUserById(Long userid){			
		User user =loadCompleteUser(userid);
		return user;
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

		MenuItemDTO siteGrid = new MenuItemDTO();
		siteGrid.setIcon("home");
		siteGrid.setItemClassName("com.advenio.medere.emr.ui.CRUDSitesView");
		siteGrid.setItemId(Long.valueOf(i));
		siteGrid.setItemName("Mis tareas asignadas");
		siteGrid.setOrder(i++);
			
		MenuItemDTO Cie10Grid = new MenuItemDTO();
		Cie10Grid.setIcon("home");
		Cie10Grid.setItemClassName("com.advenio.medere.emr.ui.CRUDCie10View");
		Cie10Grid.setItemId(Long.valueOf(i));
		Cie10Grid.setItemName("Lista de tareas");
		Cie10Grid.setOrder(i++);
		MenuItemDTO nutritionGrid = new MenuItemDTO();
		nutritionGrid.setIcon("home");
		nutritionGrid.setItemClassName("com.advenio.medere.emr.ui.CRUDNutritionView");
		nutritionGrid.setItemId(Long.valueOf(i));
		nutritionGrid.setItemName("Lista de sectores");
		nutritionGrid.setOrder(i++);
		menu.add(siteGrid);
		menu.add(Cie10Grid);
		menu.add(nutritionGrid);;
		return menu;
	}
	
	@Override
	public boolean changeUserPassword(Object userid,String hashedOldPassword,String hashedNewPassword) {
		return true;
	}

	@Override
	public void changeUserLanguage(Object userid, long languageId) {
	
	}
	
	@Transactional(readOnly=true)
	public User loadCompleteUser(Long userID){

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> rootuser = query.from(User.class);
		query.where(builder.equal(rootuser.get("_User"), userID));

		TypedQuery<User> tQuery = entityManager.createQuery(query);		
		tQuery.setHint("org.hibernate.cacheable", "true");
		
		List<User> l = tQuery.getResultList();
		
		User user = l.size() > 0 ? l.get(0) : null;
		return user;
	}
	
	@Transactional(readOnly=true)
	public Profile findProfile(int profileID, Language language) {
		Session hibernateSession = entityManager.unwrap(Session.class);
		hibernateSession.enableFilter("language").setParameter("language", language.getLanguage().longValue());
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Profile> criteria = builder.createQuery(Profile.class);
		Root<Profile> root = criteria.from(Profile.class);
		Join<Profile, ProfileI18n> joinMeasureUnit = root.join("profileI18n");
		criteria.where(builder.equal(root.get("profile"), profileID));
		TypedQuery<Profile> query = entityManager.createQuery(criteria).setHint("org.hibernate.cacheable", "true");
		List<Profile> list = query.getResultList();
		Profile result = null;
		if (list.size()>0){
			result = list.get(0);
		}
		return result;
	}

}
