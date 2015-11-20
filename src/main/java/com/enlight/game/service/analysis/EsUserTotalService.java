package com.enlight.game.service.analysis;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.analysis.EsUserTotal;
import com.enlight.game.repository.analysis.EsUserTotalDao;



@Component
@Transactional
public class EsUserTotalService {

	@Autowired
	private EsUserTotalDao esUserTotalDao;
	
	public List<EsUserTotal> findEsUserTotal(Long gameId , Date fromTotalDate ,Date toTotalDate){
		return esUserTotalDao.findEsUserTotal(gameId, fromTotalDate, toTotalDate);
	}
	
	public EsUserTotal findByGameIdAndTotalDate(Long gameId , Date totalDate){
		return esUserTotalDao.findByGameIdAndTotalDate(gameId, totalDate);
	}
	
	public EsUserTotal findById(Long gameId){
		return esUserTotalDao.findOne(gameId);
	}
	
	public void save(EsUserTotal esUserTotal){
		EsUserTotal userTotal = new EsUserTotal();
		
		userTotal.setGameId(esUserTotal.getGameId());
		userTotal.setGameName(esUserTotal.getGameName());
		userTotal.setTotalDate(esUserTotal.getTotalDate());
		userTotal.setTotalUser(esUserTotal.getTotalUser());
		userTotal.setTotalPayUser(esUserTotal.getTotalPayUser());
		userTotal.setTotalIncome(esUserTotal.getTotalIncome());
		esUserTotalDao.save(userTotal);
	}
	
	public void update(EsUserTotal esUserTotal){
		EsUserTotal userTotal  = esUserTotalDao.findOne(esUserTotal.getId());
		
		userTotal.setGameId(esUserTotal.getGameId());
		userTotal.setGameName(esUserTotal.getGameName());
		userTotal.setTotalDate(esUserTotal.getTotalDate());
		userTotal.setTotalUser(esUserTotal.getTotalUser());
		userTotal.setTotalPayUser(esUserTotal.getTotalPayUser());
		userTotal.setTotalIncome(esUserTotal.getTotalIncome());
		esUserTotalDao.save(userTotal);
	}
	
}
