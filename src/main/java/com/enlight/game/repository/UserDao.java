/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.enlight.game.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.User;


public interface UserDao extends PagingAndSortingRepository<com.enlight.game.entity.User, Long>, JpaSpecificationExecutor<User> {
	User findByLoginName(String loginName);

	
	@Query("select count(roles)  from User  where status='1' and storeId=?1 and roles=?2" )
	int countRole(String storeId, String role);
}
