package com.enlight.game.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Server;

public interface ServerDao extends PagingAndSortingRepository<Server, Long>,JpaSpecificationExecutor<Server>{

	Server findByServerId(String serverId);
}
