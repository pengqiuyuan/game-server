package com.enlight.game.repository.analysis;

import java.util.Date;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.analysis.EsUserTotal;


public interface EsUserTotalDao extends PagingAndSortingRepository<EsUserTotal, Long>,JpaSpecificationExecutor<EsUserTotal>{

	@Modifying
	@Query("from EsUserTotal esUserTotal where esUserTotal.gameId =?1 and esUserTotal.totalDate between ?2 and ?3")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })  
	List<EsUserTotal> findEsUserTotal(Long gameId , Date fromTotalDate , Date toTotalDate);
	
	EsUserTotal findByGameIdAndTotalDate(Long gameId , Date totalDate);
	
	
}
