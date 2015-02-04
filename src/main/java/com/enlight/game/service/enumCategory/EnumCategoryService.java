package com.enlight.game.service.enumCategory;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.EnumCategory;
import com.enlight.game.repository.EnumCategoryDao;

@Component
@Transactional
public class EnumCategoryService {
	
	@Autowired
	private EnumCategoryDao enumCategoryDao;
	
	public List<EnumCategory> findAll(){
		return enumCategoryDao.findAll();
	}
	
	public EnumCategory find(Long id){
		return enumCategoryDao.findOne(id);
	}
}
