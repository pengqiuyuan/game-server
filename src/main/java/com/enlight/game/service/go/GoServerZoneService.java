package com.enlight.game.service.go;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.go.GoServerZone;
import com.enlight.game.repository.go.GoServerZoneDao;

@Component
@Transactional
public class GoServerZoneService {
	
	@Autowired
	private GoServerZoneDao goServerZoneDao;
	
/*	public List<GoServerZone> findAll(){
		return goServerZoneDao.findAll();
	}*/
	
/*	public GoServerZone findByServerZoneId(int serverZoneId){
		return goServerZoneDao.findByServerZoneId(serverZoneId);
	}*/
	
	public List<GoServerZone> findByStoreId(int storeId){
		return goServerZoneDao.findByStoreId(storeId);
	}
	
	public GoServerZone findByServerZoneIdAndStoreId(int serverZoneId,int storeId){
		return goServerZoneDao.findByServerZoneIdAndStoreId(serverZoneId, storeId);
	}
	
}
