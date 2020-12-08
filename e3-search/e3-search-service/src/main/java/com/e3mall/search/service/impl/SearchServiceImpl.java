package com.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.dao.SearchDao;
import com.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		
		SolrQuery query = new SolrQuery();
		query.setQuery(keyword);
		if(page <= 0) page = 1;
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_title");
		//高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		
		SearchResult result = searchDao.search(query);
		
		long recordCount = result.getRecordCount();
		int totalPage = (int)(recordCount/rows);
		if (recordCount % rows > 0) 
			totalPage ++;
		result.setTotalPages(totalPage);
		return result;
	}

}
