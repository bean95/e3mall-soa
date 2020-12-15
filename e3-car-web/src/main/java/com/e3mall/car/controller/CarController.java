package com.e3mall.car.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.TbItemService;

@Controller
public class CarController {
	
	@Autowired
	private TbItemService tbItemServiceImpl;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;

	
	@RequestMapping("/cart/add/{itemId}")
	public String addCar(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num, HttpServletRequest request, HttpServletResponse response) {
		
		/**
		 	未登录：
		 		查询cookie，是否已存在carList
		 		存在：数量相加
		 		不存在： 查询数据，存入carList
		 */
		List<TbItem> carList = getCarListFromCookie(request);
		boolean exist = false;
		for(TbItem item : carList) {
			if(item.getId() == itemId.longValue()) {
				exist = true;
				item.setNum(item.getNum() + num);
				break;
			}
		}
		if(!exist) {
			TbItem tbItem = tbItemServiceImpl.get(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			carList.add(tbItem);
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(carList), COOKIE_CART_EXPIRE, true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	public List<TbItem> getCarListFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}
		return JsonUtils.jsonToList(json, TbItem.class);
	}
	
	@RequestMapping("/cart/cart")
	public String carList(HttpServletRequest request, HttpServletResponse response) {
		List<TbItem> carList = getCarListFromCookie(request);
		request.setAttribute("cartList", carList);
		return "cart";
	}
	
	/**
	 * 更新购物车商品数量-ajax
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request ,HttpServletResponse response) {
		
		//从cookie中取购物车列表
		List<TbItem> carList = getCarListFromCookie(request);
		//遍历商品列表找到对应的商品
		for (TbItem tbItem : carList) {
			if (tbItem.getId().longValue() == itemId) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(carList), COOKIE_CART_EXPIRE, true);
		//返回成功
		return E3Result.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		
		List<TbItem> carList = getCarListFromCookie(request);
		//遍历列表，找到要删除的商品
		for (TbItem tbItem : carList) {
			if (tbItem.getId().longValue() == itemId) {
				//删除商品
				carList.remove(tbItem);
				//跳出循环
				break;
			}
		}
		//把购物车列表写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(carList), COOKIE_CART_EXPIRE, true);
		//返回逻辑视图
		return "redirect:/cart/cart.html";
	}
}
