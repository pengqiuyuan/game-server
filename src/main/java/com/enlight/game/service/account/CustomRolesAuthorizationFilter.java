package com.enlight.game.service.account;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 自定义认证过滤器，扩展为支持多个角色
 * @author laidingqing
 *
 */
public class CustomRolesAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		
		Subject subject = getSubject(request, response);  
        String[] rolesArray = (String[]) mappedValue;  
  
        if (rolesArray == null || rolesArray.length == 0) 
        {  
            return true;  
        }  
        for(int i=0;i<rolesArray.length;i++)
        {
            if(subject.hasRole(rolesArray[i]))
            {  
                return true;  
            }  
        }  
        return false; 
	}

}
