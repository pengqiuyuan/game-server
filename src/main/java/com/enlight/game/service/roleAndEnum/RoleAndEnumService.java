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
	
	public List<String> findByRole(Long id){
		return roleAndEnumDao.findByRole(id);
	}
	
	public void save(RoleAndEnum roleAndEnum){
		RoleAndEnum r = new RoleAndEnum();
		r.setCrDate(roleAndEnum.getCrDate());
		r.setUpdDate(new Date());
		r.setStatus(RoleAndEnum.STATUS_VALIDE);
		r.setRoleRunctionId(roleAndEnum.getRoleRunctionId());
		r.setEnumRole(roleAndEnum.getEnumRole());
		r.setEnumName(roleAndEnum.getEnumName());
		roleAndEnumDao.save(r);
	}
	
	public void update(RoleAndEnum roleAndEnum){
		RoleAndEnum r = roleAndEnumDao.findOne(roleAndEnum.getId());
		r.setCrDate(roleAndEnum.getCrDate());
		r.setUpdDate(new Date());
		r.setStatus(RoleAndEnum.STATUS_VALIDE);
		r.setRoleRunctionId(roleAndEnum.getRoleRunctionId());
		r.setEnumRole(roleAndEnum.getEnumRole());
		r.setEnumName(roleAndEnum.getEnumName());
		roleAndEnumDao.save(r);
	}
	
	

	
}
