package com.enlight.game.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.enlight.game.entity.Role;




/**
 * 用于服务调用的上下文对象，从Security中实现该对象
 * @author skylai
 *
 */
public class SessionContext extends Hashtable<String, Object> implements UserDetails {

	private static final long serialVersionUID = 1L;

	public final static String CONTEXT_USER_ID = "USER_ID";
	public final static String CONTEXT_USER_NAME = "USER_NAME";
	public final static String CONTEXT_USER_PASSWORD = "USER_PASSWORD";
	public final static String CONTEXT_USER_ROLES = "USER_ROLES";
	public final static String CONTEXT_USER_EMAIL = "USER_EMAIL";
	
	public Integer getContextUserId(){
		return (Integer) this.get(CONTEXT_USER_ID);
	}
	
	public void setContextUserId(Integer userId){
		this.put(CONTEXT_USER_ID, userId);
	}
	
	public String getContextUserName(){
		return (String)this.get(CONTEXT_USER_NAME);
	}
	
	public void setContextUserName(String name){
		this.put(CONTEXT_USER_NAME, name);
	}
	
	
	public String getContextUserPassword(){
		return (String)this.get(CONTEXT_USER_PASSWORD);
	}
	
	public void setContextUserPassword(String password){
		this.put(CONTEXT_USER_PASSWORD, password);
	}

	public void setContextUserRoles(List<Role> roles){
		this.put(CONTEXT_USER_ROLES, roles);
	}
	
	public void setContextUserEmail(String email){
		this.put(CONTEXT_USER_EMAIL, email);
	}
	
	public String getEmail(){
		return (String)this.get(CONTEXT_USER_EMAIL);
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getContextUserRoles(){
		return (List<Role>) this.get(CONTEXT_USER_ROLES);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(getContextUserRoles().size());
        for (Role role : getContextUserRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
	}
	
	@Override
	public String getPassword() {
		return getContextUserPassword();
	}

	@Override
	public String getUsername() {
		return getContextUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
}
