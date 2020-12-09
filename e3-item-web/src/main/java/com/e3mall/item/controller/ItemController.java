package com.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.TbItemService;

@Controller
public class ItemController {

	@Autowired
	private TbItemService tbItemServiceImpl;
	
	@RequestMapping("/item/{itemId}")
	public String itemDetail(@PathVariable("itemId") Long itemId,Model model) {
		TbItem tbItem = tbItemServiceImpl.get(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = (TbItemDesc)tbItemServiceImpl.getDescByItemId(itemId).getData();
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		//返回逻辑视图
		return "item";
	}
}
