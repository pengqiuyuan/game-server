package com.enlight.game.util;

import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
	public static void main(String[] args) throws IOException {
		long startTime=System.currentTimeMillis();  
		for (int i = 0; i < 1200000; i++) {
			String temp="create|2014-12-11 11:54:35.107|1|1|1|2|4|92947747373056|player_5|{\"createuser\":\"0\",\"role_id\":\"92947747373056\",\"level\":\"1\",\"ip\":\"183.60.92.253\",\"from\":\"0\"}\r\n";  
	        FileOutputStream fos = new FileOutputStream("/Users/apple/Desktop/log/2015-10-12/gs1_user_2014-12-14.log",true);//true表示在文件末尾追加  
	        fos.write(temp.getBytes());  
	        fos.close();//流要及时关闭  
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}
}
