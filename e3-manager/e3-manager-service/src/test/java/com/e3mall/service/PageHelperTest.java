package com.e3mall.service;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class PageHelperTest {

	@Test
	public void testPageHelper() {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper dao = app.getBean(TbItemMapper.class);
		
		PageHelper.startPage(1, 15);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = dao.selectByExample(example);
		PageInfo<TbItem> page = new PageInfo<>(list);
		System.out.println(page.getTotal());
		System.out.println(page.getPages());
		System.out.println(list.size());
	}
	
}
