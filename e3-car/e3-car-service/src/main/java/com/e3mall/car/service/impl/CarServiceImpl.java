package com.e3mall.car.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.car.service.CarService;
import com.e3mall.common.jedis.JedisApiClient;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;

@Service
public class CarServiceImpl implements CarService {
	
	@Autowired
	private JedisApiClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;

	
	@Override
	public E3Result addCart(long userId, long itemId, int num) {
		
		String key = REDIS_CART_PRE + ":" + userId;
		String field = itemId + "";
		TbItem item = null;
		Boolean exists = jedisClient.hexists(key, field);
		if(exists) {
			String json = jedisClient.hget(key, itemId+"");
			item = JsonUtils.jsonToPojo(json, TbItem.class);
		}else {
			item = itemMapper.selectByPrimaryKey(itemId);
			String image = item.getImage();
			if (StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
		}
		item.setNum(item.getNum()+num);
		jedisClient.hset(key, field, JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		for(TbItem item : itemList) {
			this.addCart(userId, item.getId(), item.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		
		List<TbItem> cartList = new ArrayList<>();
		List<String> hvals = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		for(String json : hvals) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			cartList.add(item);
		}
		return cartList;
	}

	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

}
