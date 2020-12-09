package com.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e3mall.common.jedis.JedisApiClient;
import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemDescMapper;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemDescExample;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.service.TbItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(readOnly = true)
public class TbItemServiceImpl implements TbItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisApiClient jedisClient;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	

	@Override
	public TbItem get(Long id) {
		TbItem item = null;
		String json = jedisClient.get(REDIS_ITEM_PRE+":"+id+":BASE");
		if(StringUtils.isNotBlank(json)) {
			item = JsonUtils.jsonToPojo(json, TbItem.class);
			return item;
		}
		item = itemMapper.selectByPrimaryKey(id);
		jedisClient.set(REDIS_ITEM_PRE + ":" + id + ":BASE", JsonUtils.objectToJson(item));
		jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":BASE", ITEM_CACHE_EXPIRE);
		return item;
	}

	@Override
	public DataGridResult<TbItem> getItemPageList(int page, int rows) {
		
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		example.setOrderByClause("UPDATED desc");
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		return new DataGridResult<>(pageInfo.getTotal(), list);
	}

	@Override
	@Transactional(readOnly = false)
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
		
		//发送消息到mq
		//可能这边添加item的事务未提交成功，listener那边就根据id取数据了，会报空指针
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(item.getId().toString());
			}
		});
		
		//返回成功
		return E3Result.ok();
	}

	@Override
	@Transactional(readOnly = false)
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
		//TbItemDescExample example = new TbItemDescExample();
		//Criteria criteria = example.createCriteria();
		//criteria.andItemIdEqualTo(itemId);
		//TbItemDesc desc = itemDescMapper.selectByExample(example).get(0);
		
		TbItemDesc desc = null;
		String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
		if(StringUtils.isNotBlank(json)) {
			desc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
			return new E3Result(desc);
		}
		desc = itemDescMapper.selectByPrimaryKey(itemId);
		jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(desc));
		jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
		return new E3Result(desc);
	}

	@Override
	@Transactional(readOnly = false)
	public E3Result deleteItem(long itemId) {
		//物理删除
		//itemMapper.deleteByPrimaryKey(itemId);
		//itemDescMapper.deleteByPrimaryKey(itemId);
		
		//逻辑删除
		TbItem item = new TbItem();
		item.setId(itemId);
		item.setStatus((byte)3);
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		return E3Result.ok();
	}

	@Override
	@Transactional(readOnly = false)
	public E3Result instock(long itemId) {
		TbItem item = new TbItem();
		item.setId(itemId);
		item.setStatus((byte)2);
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		return E3Result.ok();
	}

	@Override
	@Transactional(readOnly = false)
	public E3Result reshelf(long itemId) {
		TbItem item = new TbItem();
		item.setId(itemId);
		item.setStatus((byte)1);
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		return E3Result.ok();
	}

}
