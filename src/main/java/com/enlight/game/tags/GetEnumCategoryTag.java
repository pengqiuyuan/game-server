package com.enlight.game.tags;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.enlight.game.entity.EnumCategory;
import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.RoleFunction;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.roleFunction.RoleFunctionService;

public class GetEnumCategoryTag extends TagSupport {

	/**
	 * 
	 */
	@Autowired
	private RoleFunctionService roleFunctionService;
	
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
			RoleFunctionService roleFunctionService = (RoleFunctionService) ctx.getBean("roleFunctionService");
            EnumFunctionService enumFunctionService = (EnumFunctionService) ctx.getBean("enumFunctionService");
            EnumCategoryService enumCategoryService = (EnumCategoryService) ctx.getBean("enumCategoryService");
            
			RoleFunction roleFunction = roleFunctionService.findById(Long.parseLong(id));
			List<RoleFunction> roleFunctions = roleFunctionService.findByGameIdAndRole(roleFunction.getGameId(), roleFunction.getRole());
			
			Set<EnumCategory> enumCategories = new HashSet<EnumCategory>();
			for (RoleFunction r : roleFunctions) {
				EnumFunction enumFunction  = enumFunctionService.findByEnumRole(r.getFunction());
				EnumCategory enumCategory = enumCategoryService.find((long)enumFunction.getCategoryId());
				enumCategories.add(enumCategory);
			}
			
			if(enumCategories!=null){
				for (EnumCategory enumCategory : enumCategories) {
					pageContext.getOut().write(enumCategory.getCategoryName()+"，");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
