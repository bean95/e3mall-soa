package com.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.TbItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class TbItemServiceImpl implements TbItemService {
	
	@Autowired
	private TbItemMapper tbItemMapper;

	@Override
	public TbItem get(Long id) {
		return tbItemMapper.selectByPrimaryKey(id);
	}

	@Override
	public DataGridResult<TbItem> getItemPageList(int page, int rows) {
		
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		return new DataGridResult<>(pageInfo.getTotal(), list);
	}

}
