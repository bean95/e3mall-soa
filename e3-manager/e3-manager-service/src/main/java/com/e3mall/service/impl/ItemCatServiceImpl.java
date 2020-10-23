package com.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.TreeNode;
import com.e3mall.mapper.TbItemCatMapper;
import com.e3mall.pojo.TbItemCat;
import com.e3mall.pojo.TbItemCatExample;
import com.e3mall.pojo.TbItemCatExample.Criteria;
import com.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<TreeNode> getItemCatTreeList(long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		List<TreeNode> treeList = new ArrayList<>();
		for(TbItemCat cat : list) {
			TreeNode node = new TreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
			treeList.add(node);
		}
		return treeList;
	}

}
