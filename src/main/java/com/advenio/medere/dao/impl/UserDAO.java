package com.advenio.medere.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.advenio.medere.dao.IUserDAO;
import com.advenio.medere.objects.dto.users.IPLoginAttemptDTO;
import com.advenio.medere.objects.dto.users.UserDTO;
import com.advenio.medere.objects.user.User;
import com.advenio.medere.security.SecurityConfig;
import com.advenio.medere.ui.components.menu.MenuItemDTO;

@Service
@Primary
public class UserDAO implements IUserDAO {

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
		
		user.setPassword(securityConfig.passwordEncoder().encode("1234"));
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
		MenuItemDTO siteGrid = new MenuItemDTO();
		siteGrid.setIcon("home");
		siteGrid.setItemClassName("com.advenio.medere.emr.ui.CRUDSitesView");
		siteGrid.setItemId(Long.valueOf(1));
		siteGrid.setItemName("Lista de sitios");
		siteGrid.setOrder(1);
		menu.add(siteGrid);
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
		
		User user = l.get(0);
		return user;
	}

}
