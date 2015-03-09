package com.enlight.game.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.enlight.game.entity.User;
import com.enlight.game.service.user.UserService;


/**
 * 
 * @author pengqiuyuan
 * 
 */
public class GetUserNameTag extends TagSupport {
	
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
			UserService userService = (UserService) ctx.getBean("userService");
			User user = userService.findById(Long.valueOf(id));
			if(user!=null){
				pageContext.getOut().write(user.getName());
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
