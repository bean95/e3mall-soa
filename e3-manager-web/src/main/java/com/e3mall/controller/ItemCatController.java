package com.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.TreeNode;
import com.e3mall.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatServiceImpl;
	
	@ResponseBody
	@RequestMapping("/list")
	public List<TreeNode> treeList(@RequestParam(name="id",defaultValue = "0") long parentId){
		return itemCatServiceImpl.getItemCatTreeList(parentId);
	}
}
