package com.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3mall.common.jedis.JedisApiClient;
import com.e3mall.common.utils.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.pojo.TbUserExample.Criteria;
import com.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	
	public static void main(String[] args) {
		System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
	}
	
	
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisApiClient jedisClient;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public E3Result login(String username, String password) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> result = userMapper.selectByExample(example);
		if(result==null||result.size()==0) {
			return E3Result.build(400, "username or password unvalid.");
		}
		TbUser user = result.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return E3Result.build(400, "username or password unvalid.");
		}
		String token = UUID.randomUUID().toString().replace("-", "");
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		return E3Result.ok(token);
	}

}
