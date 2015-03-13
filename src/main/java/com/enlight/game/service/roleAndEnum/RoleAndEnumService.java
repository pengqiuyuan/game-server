package com.enlight.game.service.roleAndEnum;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.entity.RoleAndEnum;
import com.enlight.game.repository.RoleAndEnumDao;

/**
 * @author dell
 *
 */
@Component
@Transactional
public class RoleAndEnumService {
	
	@Autowired
	private RoleAndEnumDao roleAndEnumDao;
	
	public List<RoleAndEnum> findByRoleRunctionId(Long roleRunctionId){
		return roleAndEnumDao.findByRoleRunctionId(roleRunctionId);
	}
	
	public void delById(Long id){
		roleAndEnumDao.delete(id);
	}
	
	public void deleteByRoleRunctionId(Long id){
		roleAndEnumDao.deleteByRoleRunctionId(id);
	}
	
	public List<Integer> findByRole(Long id){
		return roleAndEnumDao.findByRole(id);
	}
	
	public void save(RoleAndEnum roleAndEnum){
		RoleAndEnum r = new RoleAndEnum();
		r.setCrDate(new Date());
		r.setUpdDate(new Date());
		r.setStatus(RoleAndEnum.STATUS_VALIDE);
		r.setRoleRunctionId(roleAndEnum.getRoleRunctionId());
		r.setEnumRole(roleAndEnum.getEnumRole());
		roleAndEnumDao.save(r);
	}
}
