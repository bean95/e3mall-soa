package com.e3mall.content.service;

import java.util.List;

import com.e3mall.common.pojo.TreeNode;
import com.e3mall.common.utils.E3Result;

public interface ContentCategoryService {

	List<TreeNode> getContentCatTreeList(long parentId);
	E3Result addContentCategory(long parentId,String name);
	E3Result updateContentCategory(long id,String name);
	E3Result removeContentCategory(long id);
}
