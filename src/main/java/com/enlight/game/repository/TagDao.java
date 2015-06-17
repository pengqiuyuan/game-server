package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Tag;

public interface TagDao extends PagingAndSortingRepository<Tag, Long>,JpaSpecificationExecutor<Tag>{
	
	List<Tag> findByTagIdAndCategoryAndStoreId(Long tagId,String category,String storeId);
	
	List<Tag> findByTagIdAndCategoryAndStoreName(Long tagId,String category,String storeName);
	
	List<Tag> findByCategoryAndStoreName(String category,String storeName);
	
	List<Tag> findByTagNameAndCategoryAndStoreId(String tagName,String category,String storeId);

	
	@Modifying
	@Query("from Tag tag where tag.category=?1 and tag.storeId=?2")
	List<Tag> findByCategoryAndStoreId(String category,String storeId);
	
	@Modifying
	@Query("from Tag tag where tag.tagName like ?1 and tag.category=?2 and tag.storeId=?3 or tag.tagId like ?1 and tag.category=?2 and tag.storeId=?3 order by tag.tagId asc")
	List<Tag> findByQuery(String query , String category ,String storeId);
}
