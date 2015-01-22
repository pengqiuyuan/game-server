package com.enlight.game.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.enlight.game.entity.Stores;
import com.enlight.game.service.store.StoreService;
import com.google.common.collect.ImmutableList;


/**
 * 
 * @author pengqiuyuan
 * 
 */
public class GetStoreNameTag extends TagSupport {

	/**
	 * 
	 */
	@Autowired
	private StoreService storeService;
	
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
			StoreService storeService = (StoreService) ctx.getBean("storeService");
			List<String> storeIds  = ImmutableList.copyOf(StringUtils.split(id, ","));
			for (String id : storeIds) {
				Stores stores =  storeService.findById(Long.parseLong(id));
				if(stores!=null){
					pageContext.getOut().write(stores.getName()+" ");
					}else{
						pageContext.getOut().write("<b>超级管理员</b>");

				}
			}
			


		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
