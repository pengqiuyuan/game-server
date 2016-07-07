package com.enlight.game.tags;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * 
 * @author pengqiuyuan
 * 
 */
public class GetSecondToDayTag extends TagSupport {

	
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
			if(!id.equals("-1")){
				Integer ss = 1;  
			    Integer mi = ss * 60;  
			    Integer hh = mi * 60;  
			    Integer dd = hh * 24;  
			    Long day = Long.valueOf(id) / dd;  
			    Long hour = (Long.valueOf(id) - day * dd) / hh;  
			    Long minute = (Long.valueOf(id) - day * dd - hour * hh) / mi;  
			    Long second = (Long.valueOf(id) - day * dd - hour * hh - minute * mi) / ss;  
			    Long milliSecond = Long.valueOf(id) - day * dd - hour * hh - minute * mi - second * ss;  
			    StringBuffer sb = new StringBuffer();  
			    if(day > 0) {  
			        sb.append(day+"天");  
			    }  
			    if(hour > 0) {  
			        sb.append(hour+"小时");  
			    }  
			    if(minute > 0) {  
			        sb.append(minute+"分");  
			    }  
			    if(second > 0) {  
			        sb.append(second+"秒");  
			    }  
			    if(milliSecond > 0) {  
			        sb.append(milliSecond+"毫秒");  
			    }  
			    pageContext.getOut().write(sb.toString());
			}else{
				pageContext.getOut().write("永久禁言");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
