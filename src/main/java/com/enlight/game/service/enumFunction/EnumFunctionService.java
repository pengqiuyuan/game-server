package com.enlight.game.service.enumFunction;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.EnumFunction;
import com.enlight.game.repository.EnumFunctionDao;

@Component
@Transactional 
public class EnumFunctionService {
	
	@Autowired
	private EnumFunctionDao enumFunctionDao;
	
	public EnumFunction findById(long id){
		return enumFunctionDao.findOne(id);
	}
	
	public List<EnumFunction> findAll(){
		return enumFunctionDao.findList();
	}
	
	public EnumFunction findByEnumRole(String enumRole){
		return enumFunctionDao.findByEnumRole(enumRole);
	}
	
	public Set<EnumFunction> findByCategoryId(int categoryId){
		return enumFunctionDao.findByCategoryId(categoryId);
	}
	
	public Set<EnumFunction> findByGameId(Long gameId){
		return enumFunctionDao.findByGameId(gameId);
	}
}

