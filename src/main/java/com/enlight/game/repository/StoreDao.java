package com.enlight.game.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Stores;



public interface StoreDao extends PagingAndSortingRepository<Stores, Long>, JpaSpecificationExecutor<Stores> {

	
	
	@Modifying
	@Query(" from Stores store where store.status=1 order by sort desc")
	List<Stores> findList();

	Stores findBySort(int sort);

    @Query(" from Stores store where sort =(select max(store1.sort) from Stores store1) ")
	Stores findByMax();
    
	@Query(" from Stores store where store.status=1 and store.id in (?1)")
	List<Stores> findInIds(String ids);
	
}
