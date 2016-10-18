package com.enlight.game.repository.gm.xyj;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.gm.xyj.EventDataPrototype;


public interface EventDataPrototypeDao extends PagingAndSortingRepository<EventDataPrototype, Long>,JpaSpecificationExecutor<EventDataPrototype>{

	@Modifying
	@Query("from EventDataPrototype eventDataPrototype where eventDataPrototype.eventId=?1")
	List<EventDataPrototype> findAllByEventId(Long eventId);

}
