package com.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.common.utils.E3Result;
import com.e3mall.search.mapper.ItemMapper;
import com.e3mall.search.service.SearchItemService;


@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public E3Result importAllItems() {
		try {
		solrServer.deleteByQuery("*:*");  //清空原有数据--重置
		List<SearchItem> itemList = itemMapper.getItemList();
			for(SearchItem searchItem : itemList) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSellPoint());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_update_date", searchItem.getUpdateDate());
				document.addField("item_category_name", searchItem.getCategoryName());
				solrServer.add(document);
		}
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "数据导入时发生异常");
		}
	}

}
