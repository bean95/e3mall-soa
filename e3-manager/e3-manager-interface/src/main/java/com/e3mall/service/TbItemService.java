package com.e3mall.service;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbItem;

public interface TbItemService {
	
	public TbItem get(Long id);
	public DataGridResult<TbItem> getItemPageList(int page,int rows);
	E3Result addItem(TbItem item,String desc);
	E3Result updateItem(TbItem item,String desc);
	E3Result getDescByItemId(long itemId);
	E3Result deleteItem(long itemId);
	//下架
	E3Result instock(long itemId);
	//上架
	E3Result reshelf(long itemId);
}
