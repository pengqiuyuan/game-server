package com.enlight.game.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Monitor;


public interface MonitorDao extends PagingAndSortingRepository<Monitor, Long>,JpaSpecificationExecutor<Monitor>{

}
