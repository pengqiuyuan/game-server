package com.enlight.game.web.controller.mgr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.Tag;
import com.enlight.game.service.tag.TagService;


@Controller
@RequestMapping(value="/manage/tag")
public class TagController {
	
	private static final Logger logger = LoggerFactory.getLogger(TagController.class);
	
	@Autowired
	private TagService tagService;
	
	
	@RequestMapping(value="/uploadExcel",method=RequestMethod.GET)
	public ModelAndView addExcel() {
		ModelAndView mav=new ModelAndView();
		mav.setViewName("/tag/addExcel");
		return mav;
	}
	
	/**
	 * 处理标签excel文件的导入
	 * @param file
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @throws IOException
	 * @throws AppBizException
	 */
	@RequestMapping(value="/uploadingExcel",method=RequestMethod.POST)
	public ModelAndView addExcel(@RequestParam("fileInput") CommonsMultipartFile file, 
			HttpServletRequest request, HttpServletResponse response,  RedirectAttributes redirectAttributes) throws IOException, AppBizException {
		String category = request.getParameter("category");
		ModelAndView mav=new ModelAndView();
		logger.debug("上传标签文件..."+ file.getName()  +  "  " + file.getOriginalFilename());
		HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
		List<Tag> tags = tagService.placeExcelParseDB(wb, file.getInputStream());
		List<Tag> tagsUpdate = new ArrayList<Tag>(); //上传excel修改多少条
		List<Tag> tagsFails  = new ArrayList<Tag>(); //上传excel失败多少条
		List<Tag> tagsAdd    = new ArrayList<Tag>(); //上传excel新增多少条
		for (Tag tag : tags) {
			List<Tag> tas = tagService.findByTagIdAndCategory(tag.getTagId(),category);
			try {
				if(tas.size()>0){ //更新
					for (Tag ta : tas) {
						if(!tag.getTagName().equals(ta.getTagName())){
							ta.setTagName(tag.getTagName());
							tagService.update(ta);
							tagsUpdate.add(ta);
						}
					}
				}else{
					tag.setCategory(category);
					tagService.save(tag);
					tagsAdd.add(tag);
				}
			} catch (Exception e) {
				tagsFails.add(tag);
			}
		}
		mav.addObject("tags",tags);
		mav.addObject("tagsUpdate",tagsUpdate);
		mav.addObject("tagsFails",tagsFails);
		mav.addObject("tagsAdd",tagsAdd);
		String lastTag = tags.get(tags.size()-1).getTagName();
		mav.addObject("lastTag",lastTag);
		mav.addObject("tagsAll",tagService.findByCategory(category));
		mav.setViewName("/tag/addExcel");
		return mav;
	}

	@RequestMapping(value="/findItemNameAndId",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<Tag> findItemNameAndId(@RequestParam(value="query",required=true) String query) throws AppBizException{
		query = '%' + query + '%' ;
		List<Tag> ts = tagService.findByQuery(query);
		for (Tag tag : ts) {
			tag.setTagName(tag.getId() + ":  " + tag.getTagName());
		}
		return ts;
	}
	
}
