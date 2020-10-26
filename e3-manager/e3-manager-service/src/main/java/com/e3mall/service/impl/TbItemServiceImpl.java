package com.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.mapper.TbItemDescMapper;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemDescExample;
import com.e3mall.pojo.TbItemDescExample.Criteria;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.TbItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class TbItemServiceImpl implements TbItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Override
	public TbItem get(Long id) {
		return itemMapper.selectByPrimaryKey(id);
	}

	@Override
	public DataGridResult<TbItem> getItemPageList(int page, int rows) {
		
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		return new DataGridResult<>(pageInfo.getTotal(), list);
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象。
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//返回成功
		return E3Result.ok();
	}

	@Override
	public E3Result updateItem(TbItem item, String desc) {
		itemMapper.updateByPrimaryKeySelective(item);
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		return E3Result.ok();
	}
	
	@Override
	public E3Result getDescByItemId(long itemId) {
		TbItemDescExample example = new TbItemDescExample();
		//Criteria criteria = example.createCriteria();
		//criteria.andItemIdEqualTo(itemId);
		//TbItemDesc desc = itemDescMapper.selectByExample(example).get(0);
		TbItemDesc desc = itemDescMapper.selectByPrimaryKey(itemId);
		return new E3Result(desc);
	}

	@Override
	public E3Result deleteItem(long itemId) {
		//物理删除
		//itemMapper.deleteByPrimaryKey(itemId);
		//itemDescMapper.deleteByPrimaryKey(itemId);
		
		//逻辑删除
		TbItem item = new TbItem();
		item.setId(itemId);
		item.setStatus((byte)3);
		itemMapper.updateByPrimaryKeySelective(item);
		
		return E3Result.ok();
	}

	@Override
	public E3Result instock(long itemId) {
		TbItem item = new TbItem();
		item.setId(itemId);
		item.setStatus((byte)2);
		itemMapper.updateByPrimaryKeySelective(item);
		
		return E3Result.ok();
	}

	@Override
	public E3Result reshelf(long itemId) {
		TbItem item = new TbItem();
		item.setId(itemId);
		item.setStatus((byte)1);
		itemMapper.updateByPrimaryKeySelective(item);
		
		return E3Result.ok();
	}

}
