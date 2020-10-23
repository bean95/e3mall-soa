package com.e3mall.service;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.pojo.TbItem;

public interface TbItemService {
	
	public TbItem get(Long id);
	public DataGridResult<TbItem> getItemPageList(int page,int rows);
}
