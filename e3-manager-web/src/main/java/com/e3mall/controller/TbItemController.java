package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.TbItemService;

@Controller
@RequestMapping("/item")
public class TbItemController {
	
	@Autowired
	private TbItemService itemServiceImpl;
	
	@ResponseBody
	@RequestMapping("/get/{id}")
	public TbItem get(@PathVariable("id") Long id) {
		return itemServiceImpl.get(id);
	}
	
	@RequestMapping("/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemServiceImpl.get(itemId);
		return tbItem;
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public DataGridResult<TbItem> getItemPageList(int page,int rows){
		return itemServiceImpl.getItemPageList(page, rows);
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		E3Result result = itemServiceImpl.addItem(item, desc);
		return result;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc) {
		E3Result result = itemServiceImpl.updateItem(item, desc);
		return result;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteItem(String ids) {
		//E3Result result = itemServiceImpl.deleteItem(ids);
		//TODO  删除fastDFS中对应的图片
		String[] idArr = ids.split(",");
		for(String id : idArr) {
			itemServiceImpl.deleteItem(Long.parseLong(id));
		}
		return E3Result.ok();
	}
	
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result desc(@PathVariable("itemId") long itemId) {
		return itemServiceImpl.getDescByItemId(itemId);
	}
	
	@RequestMapping(value="/instock", method=RequestMethod.POST)
	@ResponseBody
	public E3Result instock(String ids) {
		//E3Result result = itemServiceImpl.instock(ids);
		String[] idArr = ids.split(",");
		for(String id : idArr) {
			itemServiceImpl.instock(Long.parseLong(id));
		}
		return E3Result.ok();
		//return result;
	}
	
	@RequestMapping(value="/reshelf", method=RequestMethod.POST)
	@ResponseBody
	public E3Result reshelf(String ids) {
		//E3Result result = itemServiceImpl.reshelf(ids);
		//return result;
		String[] idArr = ids.split(",");
		for(String id : idArr) {
			itemServiceImpl.reshelf(Long.parseLong(id));
		}
		return E3Result.ok();
	}

}
