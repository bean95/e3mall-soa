package com.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

@Controller
public class IndexController {
	
	@Value("${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	
	@Autowired
	private ContentService contentServiceImpl;

	/*
	 * @RequestMapping("/index") public String index() { return "index"; }
	 */
	
	@RequestMapping("/index")
	public String contentList(Model model){
		List<TbContent> contentList = contentServiceImpl.getContentListByCid(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", contentList);
		return "index";
	}
	
}
