package com.enlight.game.repository.gm.xyj;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.gm.xyj.EventDataPrototypeInstruction;


public interface EventDataPrototypeInstructionDao extends PagingAndSortingRepository<EventDataPrototypeInstruction, Long>,JpaSpecificationExecutor<EventDataPrototypeInstruction>{

	@Modifying
	@Query("from EventDataPrototypeInstruction eventDataPrototypeInstruction where eventDataPrototypeInstruction.id=?1")
	List<EventDataPrototypeInstruction> findAllById(Long Id);
	
	@Query("from EventDataPrototypeInstruction")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })  
	List<EventDataPrototypeInstruction> findAllCached();

}
