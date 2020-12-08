package com.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTest {

	
	@Test
	public void addDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/mycore");
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		solrServer.add(document);
		solrServer.commit();
		
		//solr没有更新，  只需id一致， solr会先删除原数据，再新增新数据
	}
	
	@Test
	public void delDocument() throws Exception  {
		SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/mycore");
		//solrServer.deleteById("doc01");
		solrServer.deleteByQuery("id:doc01");  //根据查询 删除
		solrServer.commit();
	}
	
	@Test
	public void queryIndex() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/mycore");
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		//query.setParam("rows", "13");
		query.setStart(0);
		query.setRows(13);
		QueryResponse response = solrServer.query(query);
		SolrDocumentList documentList = response.getResults();
		System.out.println("总记录数： "+documentList.getNumFound());
		for(SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
		
	}
	
	@Test
	public void queryHignLine() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/mycore");
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		
		query.setQuery("手机");
		query.setStart(0);
		query.setRows(20);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");

		
		QueryResponse response = solrServer.query(query);
		SolrDocumentList documentList = response.getResults();
		System.out.println("总记录数： "+documentList.getNumFound());
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list !=null && list.size() > 0 ) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
		
	}
}
