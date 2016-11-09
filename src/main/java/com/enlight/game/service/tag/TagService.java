package com.enlight.game.service.tag;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enlight.game.entity.Tag;
import com.enlight.game.repository.TagDao;


@Component
@Transactional
public class TagService {

	@Autowired
	private TagDao tagDao;
	
	public List<Tag> findByTagIdAndCategoryAndStoreId(Long tagId,String category,String storeId){
		return tagDao.findByTagIdAndCategoryAndStoreId(tagId,category,storeId);
	}
	
	public List<Tag> findByTagIdAndCategoryAndStoreName(Long tagId,String category,String storeName){
		return tagDao.findByTagIdAndCategoryAndStoreName(tagId,category,storeName);
	}
	
	public List<Tag> findByCategoryAndStoreName(String category,String storeName){
		return tagDao.findByCategoryAndStoreName(category,storeName);
	}
	
	
	public List<Tag> findByTagNameAndCategoryAndStoreId(String tagName,String category,String storeId){
		return tagDao.findByTagNameAndCategoryAndStoreId(tagName, category,storeId);
	}
	
	public void save(Tag tag){
		tagDao.save(tag);
	}
	
	public void update(Tag tag){
		Tag t = tagDao.findOne(tag.getId());
		t.setCategory(tag.getCategory());
		t.setTagId(tag.getTagId());
		t.setTagName(tag.getTagName());
		t.setStoreId(tag.getStoreId());
		tagDao.save(t);
	}
	
	public List<Tag> findByCategoryAndStoreId(String category,String storeId){
		return  tagDao.findByCategoryAndStoreId(category,storeId);
	}
	
	public List<Tag> findByQuery(String query,String category,String gameId){
		return tagDao.findByQuery(query,category,gameId);
	}
	
	public List<Tag> placeExcelParseDB(HSSFWorkbook wb, InputStream is) {
		List<Tag> tags = new ArrayList<Tag>(0);
		
		String tagId="";
		String tagName="";
		try {
			//得到第一个工作表
			HSSFSheet sheet=wb.getSheetAt(0);
			//第一行
			HSSFRow row=sheet.getRow(0);
			//历表格中所有工作表i表示工作表的数量 getNumberOfSheets表示工作表的总数
			System.out.println("工作表的总数:"+wb.getNumberOfSheets());
			for(int i=0;i<wb.getNumberOfSheets();i++){
				sheet=wb.getSheetAt(i);
				System.out.println("行的总数:"+sheet.getPhysicalNumberOfRows());
				//历该工作表中的所有行j表示行数 getPhysicalNumberOfRows行的总数
				for(int j=1;j<sheet.getPhysicalNumberOfRows();j++){
					Tag tag = new Tag();
					row=sheet.getRow(j);
					//判断是否存在还需要导入的数据
					if(row==null){
						System.out.println("这里已没有数据，在第"+i+"页,第"+j+"行");
						break;
					}
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					if (row.getCell(0) != null) {
						tagId = row.getCell(0).getStringCellValue();
					}

					if (row.getCell(1) != null) {
						tagName = row.getCell(1).getStringCellValue();
					}

					tag.setTagId(Long.valueOf(tagId));
					tag.setTagName(tagName);
					tag.setCategory(Tag.CATEGORY_ITEM);
					tags.add(tag);
					System.out.println(j + " " + tag.getTagId() + "  " + tag.getTagName() + "  "  + tag.getCategory());
				}
			}
		} catch (Exception e) {
			System.out.println("ttttttttttttt" +e);
			e.getStackTrace();
		}
		System.out.println("实际导入条数："+tags.size());
		return tags;
	}
	
}
