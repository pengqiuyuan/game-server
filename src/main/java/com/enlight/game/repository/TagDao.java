package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Tag;

public interface TagDao extends PagingAndSortingRepository<Tag, Long>,JpaSpecificationExecutor<Tag>{
	
	List<Tag> findByTagIdAndCategory(Long tagId,String category);
	
	@Modifying
	@Query("from Tag tag where tag.category=?1")
	List<Tag> findByCategory(String category);
	
	@Modifying
	@Query("from Tag tag where tag.tagName like ?1 and tag.category=?2")
	List<Tag> findByQuery(String query , String category);

}
