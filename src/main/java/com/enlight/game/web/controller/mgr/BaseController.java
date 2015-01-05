package com.enlight.game.web.controller.mgr;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import com.enlight.game.base.AppBizException;
import com.enlight.game.base.SessionContext;
import com.enlight.game.util.RandomUtils;


public abstract class BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	public static final String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";
	
	@ExceptionHandler({Exception.class, AppBizException.class})
	public ModelAndView handleException(Exception ex, HttpServletRequest request) {
	  
		logger.error("Exception:", ex);
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", ex.getMessage());
		
		mav.setViewName("error");
		
		return mav;
	}
	@Value("#{envProps.imagepath}")
	private String filePath;
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm"), true));
	}
	
	public SessionContext getSessionContext(){
		return (SessionContext)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	protected String genFileName(String name){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		StringBuilder finFileName = new StringBuilder(sdf.format(new Date()));
		finFileName.append( RandomUtils.get(5));
		finFileName.append(name);
		return finFileName.toString();
	}
	protected String genPath(String projectName){
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		String fullPath= projectName + "/" + sdfYear.format(new Date()) + "/" + sdfMonth.format(new Date());
		File file=new File(fullPath);
		if(!file.exists()){
			file.mkdirs();
		}
		return fullPath;
	}
	public final static Map<String, String> SUCCESS_RESULT = new HashMap<String, String>();
	
	static{
		SUCCESS_RESULT.put("status", "success");
	}
}
