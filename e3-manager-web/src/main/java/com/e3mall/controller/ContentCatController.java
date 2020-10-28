package com.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.TreeNode;
import com.e3mall.common.utils.E3Result;
import com.e3mall.content.service.ContentCategoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<TreeNode> getContentCatTreeList(@RequestParam(name = "id",defaultValue = "0") long parentId){
		return contentCategoryService.getContentCatTreeList(parentId);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCategory(long parentId,String name) {
		return contentCategoryService.addContentCategory(parentId, name);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public E3Result updateContentCategory(long id,String name) {
		return contentCategoryService.updateContentCategory(id, name);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public E3Result removeContentCategory(long id) {
		return contentCategoryService.removeContentCategory(id);
	}
	
	
}
