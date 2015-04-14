package com.enlight.game.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EsUtil {
	
	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
	SimpleDateFormat s =   new SimpleDateFormat("yyyy-MM-dd" ); 
	Calendar calendar = new GregorianCalendar(); 

	public String nowDate(){
		String nowDate = sdf.format(new Date());
		return nowDate;
	}
	
	public String oneDayAgoFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-1);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String twoDayAgoFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-2);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String twoDayAgoF(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-2);
	    Date date=calendar.getTime();
	    String da = s.format(date); 
		return da;
	}
	
	public String twoDayAgoTo(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-1);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String eightDayAgoFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-8);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String eightDayAgoTo(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-7);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String thirtyOneDayAgoFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-31);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String thirtyOneDayAgoTo(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-30);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	//--------首日 首周 首月付费率 ----------
	
	public String dayFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-1);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String weekFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-7);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String weekTo(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-6);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String mouthFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-30);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String mouthTo(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-29);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	//--------首日 首周 首月付费率 ----------
	
	

	public String presentfirstday(){
        //获取前月的第一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String firstDay = format.format(cal_1.getTime());
        return firstDay;
 
	}	
	
	public String lastmouthfirstday(){
        //获取当月的第一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
        return first;
	}	
	
	public String lastmouth(){
		//获取上月
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM"); 
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String fd= ft.format(cal_1.getTime());
        return fd;
	}
	
	
	
}
