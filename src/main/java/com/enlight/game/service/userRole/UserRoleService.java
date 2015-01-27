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
	
	public List<UserRole> findByUserIdAndStoreId(Long userId,Long storeId){
		return userRoleDao.findByUserIdAndStoreId(userId,storeId);
	}
	
	public List<UserRole> findByStoreIdAndRole(Long storeId,String role){
		return userRoleDao.findByStoreIdAndRole(storeId, role);
	}
	
	public void delByStoreIdAndRole(Long storeId,String role){
		userRoleDao.delByStoreIdAndRole(storeId, role);
	}
	
	public void delUserRole(Long storeId,Long userId){
		userRoleDao.delByStoreId(storeId,userId);
	}
	
	public void delByUserId(Long userId){
		userRoleDao.delByUserId(userId);
	}
	
	public void delByStoreId(Long storeId){
		userRoleDao.delByStoreId(storeId);
	}
}
