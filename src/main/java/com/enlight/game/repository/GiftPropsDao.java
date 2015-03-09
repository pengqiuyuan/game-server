package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.GiftProps;


public interface GiftPropsDao extends PagingAndSortingRepository<GiftProps, Long> , JpaSpecificationExecutor<GiftProps>{
	
	List<GiftProps> findByGameId(String gameId);
	
	GiftProps findByItemId(Long itemId);

}
