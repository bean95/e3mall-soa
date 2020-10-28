package com.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.e3mall.common.pojo.TreeNode;
import com.e3mall.common.utils.E3Result;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.mapper.TbContentCategoryMapper;
import com.e3mall.pojo.TbContentCategory;
import com.e3mall.pojo.TbContentCategoryExample;
import com.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<TreeNode> getContentCatTreeList(long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);	
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		
		List<TreeNode> tree = new ArrayList<>();
		for(TbContentCategory cat:catList) {
			TreeNode node = new TreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
			tree.add(node);
		}
		return tree;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setParentId(parentId);
		category.setName(name);
		category.setStatus(1);  //1正常  0删除
		category.setSortOrder(1);
		category.setIsParent(false);
		category.setCreated(new Date());
		category.setUpdated(category.getCreated());
		contentCategoryMapper.insert(category);
		//
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok(category);
	}

	@Override
	public E3Result updateContentCategory(long id, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setId(id);
		category.setName(name);
		category.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKeySelective(category);
		return E3Result.ok();
	}

	@Override
	public E3Result removeContentCategory(long id) {
		
		//删除
		TbContentCategory category = new TbContentCategory();
		category.setId(id);
		category.setStatus(0);
		category.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKeySelective(category);
		//判断父节点是否还有子节点

		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(category.getParentId()).andStatusEqualTo(0);
		List<TbContentCategory> child = contentCategoryMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(child)) {
			TbContentCategory parent = new TbContentCategory();
			parent.setId(category.getParentId());
			parent.setIsParent(false);
			parent.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
		
		return E3Result.ok();
	}

}
