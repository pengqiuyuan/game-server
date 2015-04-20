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
}
