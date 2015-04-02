package com.enlight.game.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneUtil {
	
	public static void main(String[] args) throws ParseException {
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
