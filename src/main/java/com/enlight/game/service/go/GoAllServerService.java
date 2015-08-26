package com.enlight.game.service.go;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.repository.go.GoAllServerDao;

@Component
@Transactional
public class GoAllServerService {
	
	@Autowired
	private GoAllServerDao goAllServerDao;
	
	public List<GoAllServer> findAll(Integer storeId, Integer serverZoneId){
		return goAllServerDao.findAllByPlatFormIdAndStoreIdAndServerZoneId(storeId, serverZoneId);
	}
	
}
