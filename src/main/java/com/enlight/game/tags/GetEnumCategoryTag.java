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
import com.enlight.game.entity.RoleAndEnum;
import com.enlight.game.entity.RoleFunction;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.roleAndEnum.RoleAndEnumService;
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
            EnumFunctionService enumFunctionService = (EnumFunctionService) ctx.getBean("enumFunctionService");
            EnumCategoryService enumCategoryService = (EnumCategoryService) ctx.getBean("enumCategoryService");
            RoleAndEnumService   roleAndEnumService = (RoleAndEnumService) ctx.getBean("roleAndEnumService");
            
			Set<EnumCategory> enumCategories = new HashSet<EnumCategory>();
			
			List<RoleAndEnum> roleAndEnums = roleAndEnumService.findByRoleRunctionId(Long.parseLong(id));
			for (RoleAndEnum roleAndEnum : roleAndEnums) {
				EnumFunction enumFunction =  enumFunctionService.findByEnumRole(roleAndEnum.getEnumRole());
				EnumCategory enumCategory = enumCategoryService.find(Long.valueOf(enumFunction.getCategoryId()));
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
