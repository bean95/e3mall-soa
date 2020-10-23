package com.e3mall.service;

import java.util.List;

import com.e3mall.common.pojo.TreeNode;

public interface ItemCatService {

	List<TreeNode> getItemCatTreeList(long parentId);
	
}
