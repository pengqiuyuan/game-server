package com.enlight.game.service.userRole;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.UserRole;
import com.enlight.game.repository.UserRoleDao;

/**
 * Gm用户权限管理
 * @author dell
 *
 */
@Component
@Transactional
public class UserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	public void save(UserRole userRole){
		userRoleDao.save(userRole);
	}
	
	public List<UserRole> findByUserId(Long userId){
		return userRoleDao.findByUserId(userId);
	}
	
	public List<UserRole> findByStoreId(Long storeId){
		return userRoleDao.findByStoreId(storeId);
	}
}
