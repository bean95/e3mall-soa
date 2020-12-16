package com.e3mall.car.service;

import java.util.List;

import com.e3mall.common.utils.E3Result;
import com.e3mall.pojo.TbItem;

public interface CarService {
	
	E3Result addCart(long userId, long itemId, int num);
	E3Result mergeCart(long userId, List<TbItem> itemList);
	List<TbItem> getCartList(long userId);
	E3Result updateCartNum(long userId, long itemId, int num);
	E3Result deleteCartItem(long userId, long itemId);
	
}
