package com.enlight.game.service.gm.xyj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.enlight.game.entity.User;
import com.enlight.game.entity.gm.xyj.EventPrototype;
import com.enlight.game.repository.gm.xyj.EventPrototypeDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class XyjEventPrototypeService {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private EventPrototypeDao eventPrototypeDao;
	
	@Autowired
	private AccountService accountService;
	
	public EventPrototype findById(Long id){
		return eventPrototypeDao.findOne(id);
	}
	
	public List<EventPrototype> findAll(){
		return eventPrototypeDao.findAll();
	}
	
	public List<EventPrototype> findAllByGameIdAndServerZoneId(String gameId,String serverZoneId){
		return eventPrototypeDao.findAllByGameIdAndServerZoneId(gameId, serverZoneId);
	}
	
	
	public Page<EventPrototype> findEventPrototypesByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,sortType);
		Specification<EventPrototype> spec = buildSpecification(userId, searchParams);
		return eventPrototypeDao.findAll(spec, pageRequest);
	}
	
	/**
	 * 保存活动
	 */
	public void save(EventPrototype eventPrototype){
		eventPrototypeDao.save(eventPrototype);
	}
	
	public EventPrototype saveRetureEventPrototype(EventPrototype eventPrototype){
		return eventPrototypeDao.save(eventPrototype);
	}
	
	/**
	 * 保存活动并返回活动
	 */
	public EventPrototype saveReturnEvent(EventPrototype eventPrototype){
		
		return  eventPrototypeDao.save(eventPrototype);
	}
	
	/**
	 * 查询所有未关闭的活动
	 * @return
	 */
	public List<EventPrototype> findByTimes(){
		return eventPrototypeDao.findByTimes();
	}
	
	public void deleteByStatusInvalide(Long eventId){
		eventPrototypeDao.deleteByStatusInvalide(eventId);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<EventPrototype> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		filters.put("status", new SearchFilter("status", Operator.EQ,EventPrototype.STATUS_VALIDE));
		Specification<EventPrototype> spec = DynamicSpecifications.bySearchFilter(filters.values(), EventPrototype.class);
		return spec;
	}
	
	public String nowDate(){
		String nowDate = sdf.format(new Date());
		return nowDate;
	}
	
	/** 某个时间d延时激活Hour小时
	 * @throws ParseException */
	public String getByHour(String d , String hour) throws ParseException{
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(sdf.parse(d)); 
	    calendar.add(calendar.HOUR_OF_DAY,Integer.valueOf(hour));
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	/** 某个时间d延时激活day天 
	 * @throws ParseException */
	public String getByDay(String d , String day) throws ParseException{
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(sdf.parse(d)); 
	    calendar.add(calendar.DATE,Integer.valueOf(day));
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String getDateByWeek(String week,String date) throws ParseException{
		String w = week.equals("1") ? getMonday(sdf.parse(date)) 
				:week.equals("2") ? getTuesday(sdf.parse(date)) 
				:week.equals("3") ? getWednesday(sdf.parse(date)) 
				:week.equals("4") ? getThursday(sdf.parse(date)) 
				:week.equals("5") ? getFriday(sdf.parse(date)) 
				:week.equals("6") ? getSaturday(sdf.parse(date)) 
				:week.equals("7") ? getSunday(sdf.parse(date)):"未知"; 
		return w;
	}
	
	// 获得周日的日期
	public static String getSunday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return sdf.format(c.getTime());
	}

	// 获得周一的日期
	public static String getMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return sdf.format(c.getTime());
	}

	// 获得周二的日期
	public static String getTuesday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		return sdf.format(c.getTime());
	}

	// 获得周三的日期
	public static String getWednesday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		return sdf.format(c.getTime());
	}

	// 获得周四的日期
	public static String getThursday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		return sdf.format(c.getTime());
	}

	// 获得周五的日期
	public static String getFriday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		return sdf.format(c.getTime());
	}

	// 获得周六的日期
	public static String getSaturday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return sdf.format(c.getTime());
	}
}
