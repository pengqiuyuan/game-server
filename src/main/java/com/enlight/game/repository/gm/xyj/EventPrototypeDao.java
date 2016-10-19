package com.enlight.game.repository.gm.xyj;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.gm.xyj.EventPrototype;

public interface EventPrototypeDao extends PagingAndSortingRepository<EventPrototype, Long>,JpaSpecificationExecutor<EventPrototype>{

	@Modifying
	@Query("from EventPrototype eventPrototype where eventPrototype.gameId=?1 and eventPrototype.serverZoneId=?2")
	List<EventPrototype> findAllByGameIdAndServerZoneId(String gameId,String serverZoneId);
	
	@Modifying
	@Query("from EventPrototype")
	List<EventPrototype> findAll();
	
	@Modifying
	@Query("from EventPrototype eventPrototype where eventPrototype.times != 0")
	List<EventPrototype> findByTimes();
	
	@Modifying
	@Query("delete from EventPrototype eventPrototype where eventPrototype.id=?1 and eventPrototype.status = 0")
	void deleteByStatusInvalide(Long eventId);
}
