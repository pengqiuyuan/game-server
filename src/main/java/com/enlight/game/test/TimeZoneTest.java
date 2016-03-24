package com.enlight.game.test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * HTTP工具类
 * 
 */
public class TimeZoneTest {

	public static void main(String arg[]) throws Exception {
        
        
/*		TimeZone tim = TimeZone.getTimeZone("GMT+8"); //设置为东八区
		tim = TimeZone.getDefault();// 这个是国际化所用的
		System.out.println(tim);
		TimeZone.setDefault(tim);// 设置时区
		Calendar calend = Calendar.getInstance();// 获取实例
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//构造格式化模板
		Date date = calend.getTime(); //获取Date对象
		String str = new String();
		str = format1.format(date);//对象进行格式化，获取字符串格式的输出
		System.out.println(str);
		
		
        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("foo:"+foo.format(new Date()));
        
        Calendar gc = GregorianCalendar.getInstance();
        System.out.println("gc.getTime():"+gc.getTime());
        System.out.println("gc.getTimeInMillis():"+new Date(gc.getTimeInMillis()));
        
        //当前系统默认时区的时间：
        Calendar calendar=new GregorianCalendar();
        System.out.print("时区："+calendar.getTimeZone().getID()+"  ");
        System.out.println("时间："+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        System.out.println(calendar.getTime());
        System.out.println(calendar.getTimeInMillis()); 
        //美国洛杉矶时区
        TimeZone tz=TimeZone.getTimeZone("America/Los_Angeles");
        //时区转换
        calendar.setTimeZone(tz);
        System.out.print("时区："+calendar.getTimeZone().getID()+"  ");
        System.out.println("时间："+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        System.out.println(calendar.getTime());
        System.out.println(calendar.getTimeInMillis()); 
        Date time=new Date();
        
        //1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();

        //2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

        //3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        //之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        System.out.println("UTC:"+new Date(cal.getTimeInMillis()));
        
        Calendar calendar1 = Calendar.getInstance();
        TimeZone tztz = TimeZone.getTimeZone("GMT");       
        calendar1.setTimeZone(tztz);*/
        
        
        
/*		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d= df.parse("2014-01-28 14:38:54");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(java.util.Calendar.MILLISECOND, -(c.get(java.util.Calendar.ZONE_OFFSET) + c.get(java.util.Calendar.DST_OFFSET)));
		System.out.println(c.getTimeInMillis());
		System.out.println("111UTC:"+new Date(c.getTimeInMillis()));
		System.out.println("222UTC:"+df.format(new Date(c.getTimeInMillis())));*/
        
/*        System.out.println("ttttttttttt   " +TimeZone.getDefault().getRawOffset());
        System.out.println("TimeZone.getDefault().getID()=" +TimeZone.getDefault().getID());*/
	    
/*		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date beginDate = df.parse("2015-02-02 16:42:50");
		Date endDate   = df.parse("2015-04-05 13:05:40");
		System.out.println(beginDate.toString()  + "   " +endDate.toString());
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(beginDate);
		c2.setTime(endDate);
        TimeZone tz=TimeZone.getTimeZone("America/Dawson");
        c1.setTimeZone(tz);
        c2.setTimeZone(tz);
		c1.add(java.util.Calendar.MILLISECOND, -(c1.get(java.util.Calendar.ZONE_OFFSET) + c1.get(java.util.Calendar.DST_OFFSET)));
		c2.add(java.util.Calendar.MILLISECOND, -(c2.get(java.util.Calendar.ZONE_OFFSET) + c2.get(java.util.Calendar.DST_OFFSET)));
		
		System.out.println(c1.getTimeInMillis()   +  "            "  +  c2.getTimeInMillis() );
	      
		System.out.println(df.format(new Date(c1.getTimeInMillis()))    +  "            "  +  df.format(new Date(c2.getTimeInMillis())) );
      
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(df.parse("2015-02-02 16:42:51"));
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        
        Calendar calendar2=new GregorianCalendar();
        calendar2.setTime(df.parse("2015-02-02 16:42:51"));
        calendar2.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        
        System.out.print("时区："+calendar.getTimeZone().getID()+"  ");
        System.out.println("时间："+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        
        System.out.println("时区："+calendar.getTimeInMillis());
        System.out.println("时区："+calendar2.getTimeInMillis());*/
        //System.out.print("时区222："+df.format(new Date(calendar.getTimeInMillis())));
        /*        System.out.println("时间："+df.format(new Date(calendar.getTimeInMillis())));
        System.out.println(df.format(new Date(df.parse("2015-02-02 16:42:50").getTime())));
        1422866571000
        */
		
		//2015-三月-03 10:04:22 SimpleDateFormat默认获取的是本地时区

		
		SimpleDateFormat tt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mm = tt.parse("2015-02-02 16:42:51");
        System.out.println(mm);
        System.out.println("11  " +mm.getTime());
        //mm = changeTimeZone(mm, TimeZone.getTimeZone("Asia/Shanghai"), TimeZone.getTimeZone("Australia/Sydney"));
        mm = changeTimeZone(mm, TimeZone.getDefault(), TimeZone.getTimeZone("UTC"));
        //mm = changeTimeZone(mm, TimeZone.getTimeZone("UTC"), TimeZone.getDefault());
		System.out.println(mm);
		System.out.println(mm.getTime());
	  }
	
	public static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
		Date dateTmp = null;
		if (date != null) {
			int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
			dateTmp = new Date(date.getTime() - timeOffset);
		}
		return dateTmp;
	}

	  
}
