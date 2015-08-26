package com.enlight.game.service.go;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.enlight.game.entity.go.GoStore;
import com.enlight.game.repository.go.GoStoreDao;

@Component
@Transactional
public class GoStoreService {
	
	@Autowired
	private GoStoreDao goStoreDao;
	
	public List<GoStore> findAll(){
		return goStoreDao.findAll();
	}
	
	public GoStore findByStoreId(int storeId){
		return goStoreDao.findByStoreId(storeId);
	}
	
}
