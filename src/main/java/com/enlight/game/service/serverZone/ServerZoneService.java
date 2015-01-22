package com.enlight.game.service.serverZone;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.ServerZone;
import com.enlight.game.repository.ServerZoneDao;

@Component
@Transactional
public class ServerZoneService {
	
	@Autowired
	private ServerZoneDao serverZoneDao;
	
	public List<ServerZone> findAll(){
		return serverZoneDao.findAll();
	}
	
	public ServerZone findById(Long id){
		return serverZoneDao.findOne(id);
	}
}
