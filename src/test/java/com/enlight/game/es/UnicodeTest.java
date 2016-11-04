package com.enlight.game.es;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.util.JsonBinder;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class UnicodeTest extends SpringTransactionalTestCase{
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//@Test
	public void test0() throws IOException, ParseException {	
		System.out.println("11111");
		String sJava="\u0048\u0065\u006C";
		System.out.println("StringEscapeUtils.unescapeJava(sJava):\n" + StringEscapeUtils.unescapeJava(sJava));
		System.out.println("--------------------------");
		String sJava2="\u00E5\u008F\u0091\u00E8\u00B4\u00A7\u00E7\u009A\u0084\u00E8\u00B4\u00B9\u00E5\u0093\u0088\u00E5\u00B8\u0082\u00E7\u009A\u0084\u00E9\u00A3\u008E\u00E6\u00A0\u00BC";
		System.out.println("StringEscapeUtils.unescapeJava(sJava):\n" + StringEscapeUtils.unescapeJava(sJava2));
		
		System.out.println("44444   "  + URLEncoder.encode(sJava2, "gbk"));
	}

	//@Test
	public void test1() throws IOException, ParseException {	
		String nowDate = "2016-10-21 14:13:40";
		System.out.println(nowDate);

		System.out.println(getDateByWeek("1",nowDate));
		System.out.println(getDateByWeek("2",nowDate));
		System.out.println(getDateByWeek("3",nowDate));
		System.out.println(getDateByWeek("4",nowDate));
		System.out.println(getDateByWeek("5",nowDate));
		System.out.println(getDateByWeek("6",nowDate));
		if(nowDate.compareTo(getDateByWeek("7",nowDate)) >= 0 ){
			System.out.println("222222"  + nowDate +"  "  + getDateByWeek("7",nowDate));
			System.out.println(getDateByWeek("7",getByDay(nowDate, "7")));
		}else{
			System.out.println("11111"  + nowDate +"  "  + getDateByWeek("7",nowDate));
		}
		System.out.println(getDateByWeek("7",nowDate));
		
		System.out.println(getByDay(nowDate(),"10"));
		
		System.out.println("测试 "  + getByHour(nowDate, "10"));
		
		System.out.println("测试2 "+ getByHour("2016-07-08 23:59:59", "-1"));
		System.out.println("测试2 "+ getByDay("2016-07-08 23:59:59", "0"));
	}
	@Test
	public void test3() throws IOException, ParseException {	
		System.out.println(sdf.format(new Date()));
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf1.format(new Date()));
	}
	
	public static String getDateByWeek(String week,String date) throws ParseException{
		String w = week.equals("1") ? getMonday(sdf.parse(date)) 
				:week.equals("2") ? getTuesday(sdf.parse(date)) 
				:week.equals("3") ? getWednesday(sdf.parse(date)) 
				:week.equals("4") ? getThursday(sdf.parse(date)) 
				:week.equals("5") ? getFriday(sdf.parse(date)) 
				:week.equals("6") ? getSaturday(sdf.parse(date)) 
				:week.equals("7") ? getSunday(sdf.parse(date)):"未知"; 
		return w;
	}
	
	public boolean compare(String time1, String time2) throws ParseException {
		// 如果想比较日期则写成"yyyy-MM-dd"就可以了
		// 将字符串形式的时间转化为Date类型的时间
		Date a = sdf.parse(time1);
		Date b = sdf.parse(time2);
		// Date类的一个方法，如果a早于b返回true，否则返回false
		if (a.before(b))
			return true;
		else
			return false;
		/*
		 * 如果你不喜欢用上面这个太流氓的方法，也可以根据将Date转换成毫秒 if(a.getTime()-b.getTime()<0)
		 * return true; else return false;
		 */
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
