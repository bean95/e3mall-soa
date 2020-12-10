package com.e3mall.item.mq;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.TbItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener {

	
	@Autowired
	private TbItemService tbItemServiceImpl;
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@Override
	public void onMessage(Message message) {
		
		try {
			TextMessage textMessage = (TextMessage) message;
			Long itemId = new Long(textMessage.getText());
			Thread.sleep(1000);
			TbItem tbItem = tbItemServiceImpl.get(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = (TbItemDesc)tbItemServiceImpl.getDescByItemId(itemId).getData();
			Map<String,Object> data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
			template.process(data, out);
			//关闭流
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 	创建模板
		 	id
		 	查询数据，组装map： 
		 	
		 */
		
	}

}
