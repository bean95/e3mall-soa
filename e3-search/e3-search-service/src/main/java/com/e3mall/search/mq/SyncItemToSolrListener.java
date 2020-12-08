package com.e3mall.search.mq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.ItemMapper;

public class SyncItemToSolrListener implements MessageListener {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage)message;
		try {
			String itemId = textMessage.getText();
			Long id = new Long(itemId);
			
			//避免添加数据的事务未提交
			//或者消息提到controller处执行发送
			Thread.sleep(1000);
			
			SearchItem searchItem = itemMapper.getItemById((long)id);
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSellPoint());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategoryName());
			//把文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
			System.out.println("同步数据成功!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
