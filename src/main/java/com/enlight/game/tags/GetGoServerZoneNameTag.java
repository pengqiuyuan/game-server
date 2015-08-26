package com.enlight.game.tags;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.enlight.game.entity.ServerZone;
import com.enlight.game.service.serverZone.ServerZoneService;

public class GetGoServerZoneNameTag extends TagSupport {

	/**
	 * 
	 */
	@Autowired
	private ServerZoneService serverZoneService;
	
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
			ServerZoneService serverZoneService = (ServerZoneService) ctx.getBean("serverZoneService");

		    ServerZone serverZone =  serverZoneService.findById(Long.parseLong(id));
			if(serverZone!=null){
				pageContext.getOut().write(serverZone.getServerName()+" ");
			}else{
				pageContext.getOut().write(id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
