package com.enlight.game.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.enlight.game.entity.Tag;
import com.enlight.game.service.tag.TagService;

public class GetItemNameTag extends TagSupport {
	
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
			TagService tagService = (TagService) ctx.getBean("tagService");
			List<Tag> tags = tagService.findByTagIdAndCategoryAndStoreId(Long.valueOf(id.split(",")[0]), Tag.CATEGORY_ITEM, id.split(",")[1]);
			if(tags.size()!=0){
				for (Tag tag : tags) {
					pageContext.getOut().write(tag.getTagName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

}
