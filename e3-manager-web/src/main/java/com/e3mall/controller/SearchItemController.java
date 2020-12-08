package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.utils.E3Result;
import com.e3mall.search.service.SearchItemService;

@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemServiceImpl;

	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemList() {
		return searchItemServiceImpl.importAllItems();
	}
}
