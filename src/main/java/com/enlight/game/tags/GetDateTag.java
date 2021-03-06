package com.enlight.game.tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.enlight.game.util.TimeZoneUtil;

/**
 * 
 * @author pengqiuyuan
 * 
 */
public class GetDateTag extends TagSupport {

	
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
			SimpleDateFormat tt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date =new Date(Long.valueOf(id));
			pageContext.getOut().write(tt.format(TimeZoneUtil.changeTimeZone(date,TimeZone.getTimeZone("UTC"),TimeZone.getDefault())));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
