package com.enlight.game.tags;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.enlight.game.entity.EnumFunction;
import com.enlight.game.service.enumFunction.EnumFunctionService;

public class GetFunctionNameTag extends TagSupport {

	/**
	 * 
	 */
	@Autowired
	private EnumFunctionService enumFunctionService;
	
	private static final long serialVersionUID = 1L;

	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int doStartTag() throws JspTagException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspTagException {
		try {
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());//获取SPRING的上下文
			EnumFunctionService enumFunctionService = (EnumFunctionService) ctx.getBean("enumFunctionService");

		    EnumFunction enumFunction =  enumFunctionService.findByEnumRole(id);
			if(enumFunction!=null){
				pageContext.getOut().write(enumFunction.getEnumName()+" ");
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
