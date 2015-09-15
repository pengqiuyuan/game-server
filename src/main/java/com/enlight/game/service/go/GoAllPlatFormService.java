package com.enlight.game.service.go;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.enlight.game.entity.go.GoAllPlatForm;
import com.enlight.game.repository.go.GoAllPlatFormDao;

@Component
@Transactional
public class GoAllPlatFormService {
	
	@Autowired
	private GoAllPlatFormDao goAllPlatFormDao;
	
	public List<String> findPlatFormIds(Integer storeId , Integer serverZoneId){
		return goAllPlatFormDao.findPlatFormId(storeId, serverZoneId);
	}
	
	public List<GoAllPlatForm> findAllByPlatFormIdAndStoreIdAndServerZoneId(Integer storeId , Integer serverZoneId,String platFormId){
		return goAllPlatFormDao.findAllByPlatFormIdAndStoreIdAndServerZoneId(storeId, serverZoneId, platFormId);
	}
	
}
