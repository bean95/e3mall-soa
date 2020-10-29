package com.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e3mall.common.jedis.JedisApiClient;
import com.e3mall.common.pojo.DataGridResult;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.e3mall.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisApiClient jedisClient;
	
	private static final String CONTENT_REDIS_KEY = "content_redis_key";
	
	@Override
	@Transactional(readOnly = false)
	public E3Result addContent(TbContent content) {
		
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		contentMapper.insert(content);
		//删除该cid对应的数据
		jedisClient.hdel(CONTENT_REDIS_KEY, content.getCategoryId().toString());//getContentListByCid()
		
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		
		String json = jedisClient.hget(CONTENT_REDIS_KEY, String.valueOf(cid));
		if(StringUtils.isNotBlank(json)) {  // (" ")-not Empty,but blank
			List<TbContent> jsonToList = JsonUtils.jsonToList(json, TbContent.class);
			return jsonToList;
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		jedisClient.hset(CONTENT_REDIS_KEY, String.valueOf(cid), JsonUtils.objectToJson(list));
		
		return list;
	}

	@Override
	public DataGridResult<TbContent> getcontentPageList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		PageInfo<TbContent> pageList = new PageInfo<>(list);
		return new DataGridResult<>(pageList.getTotal(),list);
	}

	@Override
	@Transactional(readOnly = false)
	public E3Result updateContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		jedisClient.hdel(CONTENT_REDIS_KEY, content.getCategoryId().toString());
		return E3Result.ok();
	}

}
