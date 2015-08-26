package com.enlight.game.repository.go;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.go.GoStore;

public interface GoStoreDao extends PagingAndSortingRepository<GoStore, Long>,JpaSpecificationExecutor<GoStore>{
	
	@Modifying
	@Query("from GoStore")
	List<GoStore> findAll();
	
	GoStore findByStoreId(int storeId);
	
}
