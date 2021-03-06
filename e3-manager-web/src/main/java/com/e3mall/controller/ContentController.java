package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class ContentController {
	
	@Autowired
	private ContentService contentServiceImpl;

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public E3Result addContent(TbContent content) {
		return contentServiceImpl.addContent(content);
	}
	
	@RequestMapping("/query/list")
	public DataGridResult<TbContent> getContentPageList(int page,int rows) {
		return contentServiceImpl.getcontentPageList(page, rows);
	}
	
	@RequestMapping("/delete")
	public E3Result delContent(String ids) {
		String[] idArr = ids.split(",");
		for(String id : idArr) {
			contentServiceImpl.removeContent(Long.parseLong(id));
		}
		return E3Result.ok();
	}
	
	@RequestMapping("/edit")
	public E3Result editContent(TbContent content) {
		return contentServiceImpl.updateContent(content);
	}
}
