package com.enlight.game.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Log;


public interface LogDao extends PagingAndSortingRepository<Log, Long>, JpaSpecificationExecutor<Log>{


	
}
