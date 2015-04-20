package com.enlight.game.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enlight.game.base.AppBizException;



/**
 * curl -i -X POST -H "Accept: application/json" -H "Content-Type: application/json;charset=UTF-8" -d '{"email":"test1@test.com"}' http://localhost:8080/film/api/v3/users/sessions
 * @author laidingqing
 *
 */
public class BaseApiController {

	final static Logger logger = LoggerFactory.getLogger(BaseApiController.class);

	public final static Map<String, Object> SUCCESS_RESULT = new HashMap<String, Object>();
	
	static{
		SUCCESS_RESULT.put("success", "true");
	}
//	@ExceptionHandler(value = {AppBizException.class, Exception.class})
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "错误的请求.")
//	@ResponseBody
//	public Map<String, Object> badRequest(Exception ex, HttpServletResponse response) {
//		final Map<String, Object> map = new HashMap<String, Object>();
//		map.put("success", "false");
///		if( ex instanceof AppBizException){
//			map.put("code", ((AppBizException)ex).getCode());
//		}
//		map.put("message", ex.getMessage());
//		response.setStatus(500);
//		logger.error("", ex);
//		return map;
//	}
	
	@ExceptionHandler(value = {AppBizException.class, Exception.class})
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "错误的请求.")
	@ResponseBody
	public String badRequest(Exception ex, HttpServletResponse response) {
		response.setStatus(500);
		logger.error("", ex);
		return ex.getMessage();
	}
	
}
