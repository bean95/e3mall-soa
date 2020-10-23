package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.TbItemService;

@Controller
@RequestMapping("/item")
public class TbItemController {
	
	@Autowired
	private TbItemService tbItemServiceImpl;
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public TbItem get(@PathVariable("id") Long id) {
		return tbItemServiceImpl.get(id);
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public DataGridResult<TbItem> getItemPageList(int page,int rows){
		return tbItemServiceImpl.getItemPageList(page, rows);
	}

}
